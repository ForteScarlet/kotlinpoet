package com.squareup.kotlinpoet.jvm.alias

import java.nio.file.Path


/**
 *
 * @author ForteScarlet
 */
public actual typealias JvmPath = Path

public actual fun JvmPath.toJvmFile(): JvmFile =
  toFile()
