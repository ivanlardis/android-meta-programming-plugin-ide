package ru.lardis.meta

import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name

internal object Names {
    val DEFAULT_COMPANION = Name.identifier("Companion")
}

internal object FqNames {
    val OPTICS_ANNOTATION = FqName("ru.lardis.lens_core.Optics")
}

internal object ClassIds {
    val LENS = ClassId(FqName("ru.lardis.lens_core"), Name.identifier("Lens"))
}