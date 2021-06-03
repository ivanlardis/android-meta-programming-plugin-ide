package ru.lardis.meta

import org.jetbrains.kotlin.descriptors.*
import org.jetbrains.kotlin.descriptors.annotations.Annotations
import org.jetbrains.kotlin.descriptors.impl.*
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.resolve.DescriptorFactory
import org.jetbrains.kotlin.storage.LockBasedStorageManager
import org.jetbrains.kotlin.types.KotlinTypeFactory
import org.jetbrains.kotlin.types.SimpleType
import org.jetbrains.kotlin.types.TypeProjectionImpl
import org.jetbrains.kotlin.types.Variance
import ru.lardis.meta.extensions.dataClassParameters
import ru.lardis.meta.extensions.lensClass

/**
val myString
get() = Lens(::myStringGet, ::myStringSet)
_________________________________________________________________
PROPERTY name:myString visibility:public modality:FINAL [val]
FUN name:<get-myString> visibility:public modality:FINAL <> ($this:ru.lardis.meta.model.C.Companion) returnType:ru.lardis.lens_core.Lens<ru.lardis.meta.model.C, kotlin.String>
correspondingProperty: PROPERTY name:myString visibility:public modality:FINAL [val]
$this: VALUE_PARAMETER name:<this> type:ru.lardis.meta.model.C.Companion
 */
internal fun createLensDescriptor(
    opticsCompanionDescriptor: ClassDescriptor,
    opticsDescriptor: ClassDescriptor,
    name: Name,
): PropertyDescriptor? {

    val opticsParameter: ValueParameterDescriptor = opticsDescriptor.dataClassParameters
        .firstOrNull { it.name == name } ?: return null

    val lensObjectType: SimpleType = KotlinTypeFactory.simpleNotNullType(
        Annotations.EMPTY,
        opticsDescriptor,
        listOf()
    )

    val returnType: SimpleType = KotlinTypeFactory.simpleNotNullType(
        Annotations.EMPTY,
        opticsDescriptor.lensClass,
        listOf(
            TypeProjectionImpl(lensObjectType), TypeProjectionImpl(opticsParameter.type)
        )
    )

    val lensProperty: PropertyDescriptorImpl = PropertyDescriptorImpl.create(
        opticsCompanionDescriptor,
        Annotations.EMPTY,
        Modality.FINAL,
        DescriptorVisibilities.PUBLIC,
        false,
        name,
        CallableMemberDescriptor.Kind.SYNTHESIZED,
        opticsCompanionDescriptor.source,
        false,
        false,
        false,
        false,
        false,
        false,
    ).apply {
        setType(
            returnType,
            listOf(),
            opticsCompanionDescriptor.thisAsReceiverParameter,
            null
        )
    }

    val lensPropertyGetter = PropertyGetterDescriptorImpl(
        lensProperty,
        Annotations.EMPTY,
        Modality.FINAL,
        DescriptorVisibilities.PUBLIC,
        false,
        false,
        false,
        CallableMemberDescriptor.Kind.SYNTHESIZED,
        null,
        opticsCompanionDescriptor.source
    ).apply {
        initialize(returnType)
    }

    lensProperty.initialize(lensPropertyGetter, null)

    return lensProperty
}

/**
val <A> Lens<A, C>.myStsTest: Lens<A, String>
get() = this andThen C.myStsTest
_________________________________________________________________
PROPERTY name:myStsTest visibility:public modality:FINAL [val]
FUN name:<get-myStsTest> visibility:public modality:FINAL <A> ($this:ru.lardis.meta.model.C.Companion, $receiver:ru.lardis.lens_core.Lens<A of ru.lardis.meta.model.C.Companion.<get-myStsTest>, ru.lardis.meta.model.C>) returnType:ru.lardis.lens_core.Lens<A of ru.lardis.meta.model.C.Companion.<get-myStsTest>, kotlin.String>
correspondingProperty: PROPERTY name:myStsTest visibility:public modality:FINAL [val]
TYPE_PARAMETER name:A index:0 variance: superTypes:[kotlin.Any?]
$this: VALUE_PARAMETER name:<this> type:ru.lardis.meta.model.C.Companion
$receiver: VALUE_PARAMETER name:<this> type:ru.lardis.lens_core.Lens<A of ru.lardis.meta.model.C.Companion.<get-myStsTest>, ru.lardis.meta.model.C>
 */
internal fun createLensComposeDescriptor(
    opticsCompanionDescriptor: ClassDescriptor,
    opticsDescriptor: ClassDescriptor,
    name: Name,
): PropertyDescriptor? {

    val opticsParameter: ValueParameterDescriptor = opticsDescriptor.dataClassParameters
        .firstOrNull { it.name == name } ?: return null

    val lensProperty: PropertyDescriptorImpl = PropertyDescriptorImpl.create(
        opticsCompanionDescriptor,
        Annotations.EMPTY,
        Modality.FINAL,
        DescriptorVisibilities.PUBLIC,
        false,
        name,
        CallableMemberDescriptor.Kind.SYNTHESIZED,
        opticsCompanionDescriptor.source,
        false,
        false,
        false,
        false,
        false,
        false,
    )

    val lensPropertyGetter = PropertyGetterDescriptorImpl(
        lensProperty,
        Annotations.EMPTY,
        Modality.FINAL,
        DescriptorVisibilities.PUBLIC,
        false,
        false,
        false,
        CallableMemberDescriptor.Kind.SYNTHESIZED,
        null,
        opticsCompanionDescriptor.source
    )

    lensProperty.initialize(lensPropertyGetter, null)

    val aDescriptor: TypeParameterDescriptor = TypeParameterDescriptorImpl.createWithDefaultBound(
        lensProperty,
        Annotations.EMPTY,
        false,
        Variance.INVARIANT,
        Name.identifier("A"),
        0,
        LockBasedStorageManager.NO_LOCKS
    )

    val lensClass = opticsDescriptor.lensClass
    val returnType: SimpleType = KotlinTypeFactory.simpleNotNullType(
        Annotations.EMPTY,
        lensClass,
        listOf(
            TypeProjectionImpl(aDescriptor.defaultType), TypeProjectionImpl(opticsParameter.type)
        )
    )

    val receiverType: SimpleType = KotlinTypeFactory.simpleNotNullType(
        Annotations.EMPTY,
        lensClass,
        listOf(
            TypeProjectionImpl(aDescriptor.defaultType), TypeProjectionImpl(opticsDescriptor.defaultType)
        )
    )

    val extensionReceiverParameter = DescriptorFactory.createExtensionReceiverParameterForCallable(
        lensClass.thisAsReceiverParameter,
        receiverType,
        Annotations.EMPTY
    )

    lensProperty.setType(
        returnType,
        listOf(aDescriptor),
        opticsCompanionDescriptor.thisAsReceiverParameter,
        extensionReceiverParameter

    )

    lensPropertyGetter.initialize(returnType)


    return lensProperty
}