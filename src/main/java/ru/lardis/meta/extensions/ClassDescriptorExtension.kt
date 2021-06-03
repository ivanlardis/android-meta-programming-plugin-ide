package ru.lardis.meta.extensions

import org.jetbrains.kotlin.descriptors.ClassDescriptor
import org.jetbrains.kotlin.descriptors.ValueParameterDescriptor
import org.jetbrains.kotlin.descriptors.findClassAcrossModuleDependencies
import org.jetbrains.kotlin.resolve.descriptorUtil.module
import ru.lardis.meta.ClassIds
import ru.lardis.meta.FqNames

val ClassDescriptor.lensClass: ClassDescriptor
    get() = module.findClassAcrossModuleDependencies(
        ClassIds.LENS
    ) ?: throw IllegalStateException("lensClass is not available")

val ClassDescriptor.dataClassParameters: List<ValueParameterDescriptor>
    get() = constructors
        .firstOrNull()
        ?.valueParameters ?: emptyList()

val ClassDescriptor.isLensCompanion
    get() = isCompanionObject && (containingDeclaration as ClassDescriptor).isLens

val ClassDescriptor.isLens
    get() = annotations.hasAnnotation(FqNames.OPTICS_ANNOTATION)


fun ClassDescriptor.getOpticsForCompanion(): ClassDescriptor? =
    if (isLensCompanion) {
        containingDeclaration as? ClassDescriptor
    } else {
        null
    }


