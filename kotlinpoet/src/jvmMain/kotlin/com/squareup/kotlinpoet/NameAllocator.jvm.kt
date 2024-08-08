package com.squareup.kotlinpoet

import java.util.*

internal actual fun defaultNewNameTag(): Any =
  UUID.randomUUID().toString()
