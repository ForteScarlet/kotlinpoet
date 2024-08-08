package com.squareup.kotlinpoet.jvm.alias

import com.squareup.kotlinpoet.JvmTypeAliasKotlinPoetApi


/**
 *
 * @author ForteScarlet
 */
@JvmTypeAliasKotlinPoetApi
public actual interface JvmPath

@JvmTypeAliasKotlinPoetApi
public actual fun JvmPath.toJvmFile(): JvmFile =
  throw UnsupportedOperationException()
