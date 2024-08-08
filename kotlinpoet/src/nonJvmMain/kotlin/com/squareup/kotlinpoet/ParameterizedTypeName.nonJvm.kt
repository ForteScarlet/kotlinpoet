package com.squareup.kotlinpoet

import kotlin.reflect.KClass
import kotlin.reflect.KTypeProjection
import kotlin.reflect.KVariance

internal actual fun ParameterizedTypeName.Companion.get(
  type: KClass<*>,
  nullable: Boolean,
  typeArguments: List<KTypeProjection>,
): TypeName {
  if (typeArguments.isEmpty()) {
    return type.asTypeName().run { if (nullable) copy(nullable = true) else this }
  }

  val effectiveType = if (type.isInstance(Array(0){})) Array::class else type
  val enclosingClass: KClass<*>? = effectiveType.enclosingClass() // type.java.enclosingClass?.kotlin

  return ParameterizedTypeName(
    enclosingClass?.let {
      get(it, false, typeArguments) // .drop(effectiveType.typeParameters.size)
    },
    effectiveType.asTypeName(),
    typeArguments /* .take(effectiveType.typeParameters.size) */ .map { (paramVariance, paramType) ->
      val typeName = paramType?.asTypeName() ?: return@map STAR
      when (paramVariance) {
        null -> STAR
        KVariance.INVARIANT -> typeName
        KVariance.IN -> WildcardTypeName.consumerOf(typeName)
        KVariance.OUT -> WildcardTypeName.producerOf(typeName)
      }
    },
    nullable,
    // effectiveType.annotations.map { AnnotationSpec.get(it) },
  )
}
