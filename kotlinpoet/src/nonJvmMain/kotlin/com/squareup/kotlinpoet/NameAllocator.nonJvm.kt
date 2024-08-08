package com.squareup.kotlinpoet

import kotlin.random.Random
import kotlin.random.nextULong

internal actual fun defaultNewNameTag(): Any = Random.nextULong().toString()
