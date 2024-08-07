/*
 * Copyright (C) 2023 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.squareup.kotlinpoet

import com.squareup.kotlinpoet.jvm.JvmClass
import kotlin.reflect.KClass

/** A spec or type which can be annotated. */
public interface Annotatable {
  public val annotations: List<AnnotationSpec>

  public interface Builder<out T : Builder<T>> {
    public val annotations: MutableList<AnnotationSpec>

    @Suppress("UNCHECKED_CAST")
    public fun addAnnotation(annotationSpec: AnnotationSpec): T = apply {
      annotations += annotationSpec
    } as T

    @Suppress("UNCHECKED_CAST")
    public fun addAnnotations(annotationSpecs: Iterable<AnnotationSpec>): T = apply {
      for (annotationSpec in annotationSpecs) {
        addAnnotation(annotationSpec)
      }
    } as T

    public fun addAnnotation(annotation: ClassName): T = addAnnotation(AnnotationSpec.builder(annotation).build())

    @DelicateKotlinPoetApi(
      message = "Java reflection APIs don't give complete information on Kotlin types. Consider " +
        "using the kotlinpoet-metadata APIs instead.",
    )
    public fun addAnnotation(annotation: JvmClass<*>): T = addAnnotation(annotation.asClassName())

    public fun addAnnotation(annotation: KClass<*>): T = addAnnotation(annotation.asClassName())
  }
}
