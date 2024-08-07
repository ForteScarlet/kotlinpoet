package com.squareup.kotlinpoet.jvm


/**
 *
 * @author ForteScarlet
 */
public expect interface JvmElement {
  public fun getModifiers(): Set<JvmModifier>
}

/**
 *
 * @author ForteScarlet
 */
public expect interface JvmExecutableElement : JvmElement
