package com.squareup.kotlinpoet.jvm


/**
 *
 * @author ForteScarlet
 */
public actual interface JvmElement {
  public actual fun getModifiers(): Set<JvmModifier>
}

/**
 *
 * @author ForteScarlet
 */
public actual interface JvmExecutableElement : JvmElement
