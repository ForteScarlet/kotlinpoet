package com.squareup.kotlinpoet

import com.squareup.kotlinpoet.jvm.alias.JvmFiler
import com.squareup.kotlinpoet.jvm.alias.JvmIOException
import com.squareup.kotlinpoet.jvm.alias.JvmJavaFileObject
import com.squareup.kotlinpoet.jvm.alias.JvmPath
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.net.URI
import javax.tools.JavaFileObject
import javax.tools.SimpleJavaFileObject
import javax.tools.StandardLocation
import kotlin.io.path.createDirectories
import kotlin.io.path.isDirectory
import kotlin.io.path.notExists
import kotlin.io.path.outputStream
import kotlin.text.Charsets.UTF_8

@Throws(JvmIOException::class)
internal actual fun FileSpec.writeToPath(directory: JvmPath): JvmPath {
  require(directory.notExists() || directory.isDirectory()) {
    "path $directory exists but is not a directory."
  }
  val outputPath = directory.resolve(relativePath)
  outputPath.parent.createDirectories()
  outputPath.outputStream().bufferedWriter().use(::writeTo)
  return outputPath
}

@Throws(JvmIOException::class)
internal actual fun FileSpec.writeToFiler(filer: JvmFiler, extension: String) {
  val originatingElements = members.asSequence()
    .filterIsInstance<OriginatingElementsHolder>()
    .flatMap { it.originatingElements.asSequence() }
    .toSet()
  val filerSourceFile = filer.createResource(
    StandardLocation.SOURCE_OUTPUT,
    packageName,
    "$name.$extension",
    *originatingElements.toTypedArray(),
  )
  try {
    filerSourceFile.openWriter().use { writer -> writeTo(writer) }
  } catch (e: Exception) {
    try {
      filerSourceFile.delete()
    } catch (_: Exception) {
    }
    throw e
  }
}

internal actual inline fun FileSpec.toJavaFileObjectInternal(
  crossinline toString: () -> String,
): JvmJavaFileObject {
  val uri = URI.create(relativePath)
  return object : SimpleJavaFileObject(uri, JavaFileObject.Kind.SOURCE) {
    private val lastModified = System.currentTimeMillis()
    override fun getCharContent(ignoreEncodingErrors: Boolean): String {
      return toString()
    }

    override fun openInputStream(): InputStream {
      return ByteArrayInputStream(getCharContent(true).toByteArray(UTF_8))
    }

    override fun getLastModified() = lastModified
  }
}
