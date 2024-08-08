package com.squareup.kotlinpoet

import com.squareup.kotlinpoet.jvm.alias.JvmFiler
import com.squareup.kotlinpoet.jvm.alias.JvmIOException
import com.squareup.kotlinpoet.jvm.alias.JvmJavaFileObject
import com.squareup.kotlinpoet.jvm.alias.JvmPath

@JvmTypeAliasKotlinPoetApi
@Throws(JvmIOException::class)
internal actual fun FileSpec.writeToPath(directory: JvmPath): JvmPath =
  throw UnsupportedOperationException()

@JvmTypeAliasKotlinPoetApi
@Throws(JvmIOException::class)
internal actual fun FileSpec.writeToFiler(filer: JvmFiler, extension: String) {
  throw UnsupportedOperationException()
}

@JvmTypeAliasKotlinPoetApi
internal actual inline fun FileSpec.toJavaFileObjectInternal(
  crossinline toString: () -> String,
): JvmJavaFileObject {
  throw UnsupportedOperationException()
}
