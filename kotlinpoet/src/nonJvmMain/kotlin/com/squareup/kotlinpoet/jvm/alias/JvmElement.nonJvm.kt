package com.squareup.kotlinpoet.jvm.alias

import com.squareup.kotlinpoet.JvmTypeAliasKotlinPoetApi


/**
 *
 * @author ForteScarlet
 */
@JvmTypeAliasKotlinPoetApi
public actual interface JvmElement

/**
 *
 * @author ForteScarlet
 */
@JvmTypeAliasKotlinPoetApi
public actual interface JvmTypeElement : JvmElement

/**
 *
 * @author ForteScarlet
 */
@JvmTypeAliasKotlinPoetApi
public actual interface JvmExecutableElement : JvmElement

/**
 *
 * @author ForteScarlet
 */
@JvmTypeAliasKotlinPoetApi
public actual interface JvmVariableElement : JvmElement
