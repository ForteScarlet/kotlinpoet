package com.squareup.kotlinpoet.jvm


internal actual fun Char.isJavaIdentifierStart(): Boolean =
    // TODO Impl check Java identifier start in JS (or in common?)
    false

internal actual fun Char.isJavaIdentifierPart(): Boolean =
    // TODO Impl check Java identifier start in JS (or in common?)
    false
