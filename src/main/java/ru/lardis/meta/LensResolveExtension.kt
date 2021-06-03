package ru.lardis.meta

import org.jetbrains.kotlin.descriptors.ClassDescriptor
import org.jetbrains.kotlin.descriptors.PropertyDescriptor
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.extensions.SyntheticResolveExtension
import ru.lardis.meta.extensions.dataClassParameters
import ru.lardis.meta.extensions.getOpticsForCompanion

internal class LensResolveExtension : SyntheticResolveExtension {

    override fun getPossibleSyntheticNestedClassNames(thisDescriptor: ClassDescriptor): List<Name>? {
        return emptyList()
    }

    override fun getSyntheticCompanionObjectNameIfNeeded(thisDescriptor: ClassDescriptor): Name =
        Names.DEFAULT_COMPANION

    override fun getSyntheticPropertiesNames(thisDescriptor: ClassDescriptor) =
        thisDescriptor.getOpticsForCompanion()
            ?.dataClassParameters
            ?.flatMap {
                listOf(
                    it.name,
                ) }
            ?: emptyList()

    override fun generateSyntheticProperties(
        thisDescriptor: ClassDescriptor,
        name: Name,
        bindingContext: BindingContext,
        fromSupertypes: ArrayList<PropertyDescriptor>,
        result: MutableSet<PropertyDescriptor>
    ) {
        val lensClassDescriptor = thisDescriptor.getOpticsForCompanion() ?: return
        createLensDescriptor(thisDescriptor, lensClassDescriptor, name)?.let(result::add)
        createLensComposeDescriptor(thisDescriptor, lensClassDescriptor, name)?.let(result::add)
    }
}
