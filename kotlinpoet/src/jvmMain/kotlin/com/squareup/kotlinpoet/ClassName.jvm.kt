@file:JvmName("ClassNames")
@file:JvmMultifileClass

package com.squareup.kotlinpoet

import com.squareup.kotlinpoet.jvm.alias.JvmClass
import com.squareup.kotlinpoet.jvm.alias.JvmTypeElement
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.NestingKind.MEMBER
import javax.lang.model.element.NestingKind.TOP_LEVEL
import javax.lang.model.element.PackageElement
import javax.lang.model.element.TypeElement
import kotlin.collections.plusAssign
import kotlin.collections.reverse
import kotlin.reflect.KClass
import kotlin.text.substring

@DelicateKotlinPoetApi(
    message = "Java reflection APIs don't give complete information on Kotlin types. Consider using" +
            " the kotlinpoet-metadata APIs instead.",
)
@JvmName("get")
public actual fun JvmClass<*>.asClassName(): ClassName {
  require(!isPrimitive) { "primitive types cannot be represented as a ClassName" }
  require(Void.TYPE != this) { "'void' type cannot be represented as a ClassName" }
  require(!isArray) { "array types cannot be represented as a ClassName" }
  val names = mutableListOf<String>()
  var c = this
  while (true) {
    names += c.simpleName
    val enclosing = c.enclosingClass ?: break
    c = enclosing
  }
  // Avoid unreliable Class.getPackage(). https://github.com/square/javapoet/issues/295
  val lastDot = c.name.lastIndexOf('.')
  if (lastDot != -1) names += c.name.substring(0, lastDot)
  names.reverse()
  return ClassName(names)
}

/** Returns the class name for `element`. */
@DelicateKotlinPoetApi(
  message = "Element APIs don't give complete information on Kotlin types. Consider using" +
    " the kotlinpoet-metadata APIs instead."
)
@JvmName("get")
public actual fun JvmTypeElement.asClassName(): ClassName {
  fun isClassOrInterface(e: Element) = e.kind.isClass || e.kind.isInterface

  fun getPackage(type: Element): PackageElement {
    var t = type
    while (t.kind != ElementKind.PACKAGE) {
      t = t.enclosingElement
    }
    return t as PackageElement
  }

  val names = mutableListOf<String>()
  var e: Element = this
  while (isClassOrInterface(e)) {
    val eType = e as TypeElement
    require(eType.nestingKind.isOneOf(TOP_LEVEL, MEMBER)) {
      "unexpected type testing"
    }
    names += eType.simpleName.toString()
    e = eType.enclosingElement
  }
  names += getPackage(this).qualifiedName.toString()
  names.reverse()
  return ClassName(names)
}

internal actual fun KClass<*>.qualifiedNameInternal(): String? =
  qualifiedName


internal actual fun Enum<*>.declaringClassName(): ClassName =
  this.declaringJavaClass.asClassName()
