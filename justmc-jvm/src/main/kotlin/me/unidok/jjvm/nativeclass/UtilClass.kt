package me.unidok.jjvm.nativeclass

import me.unidok.jjvm.util.JustOperation
import me.unidok.jjvm.util.NativeMethod
import me.unidok.jjvm.util.requireConstString

object UtilClass {
    fun register() {
        val nothingToDo: NativeMethod = { arg(0) }
        NativeClasses.registerMethods("justmc/Util", hashMapOf(
            "operation(Ljava/lang/String;[Ljustmc/Primitive;)V" to {
                val id = arg(0).requireConstString("Operation id must be const string")
                addOperation(JustOperation(id))
                null
            },
            "asByte(Ljava/lang/Object;)B" to nothingToDo,
            "asShort(Ljava/lang/Object;)S" to nothingToDo,
            "asChar(Ljava/lang/Object;)C" to nothingToDo,
            "asInt(Ljava/lang/Object;)I" to nothingToDo,
            "asLong(Ljava/lang/Object;)J" to nothingToDo,
            "asFloat(Ljava/lang/Object;)F" to nothingToDo,
            "asDouble(Ljava/lang/Object;)D" to nothingToDo,
            "asString(Ljava/lang/Object;)Ljava/lang/String;" to nothingToDo,
            "asText(Ljava/lang/Object;)Ljustmc/Text;" to nothingToDo,
            "asItem(Ljava/lang/Object;)Ljustmc/Item;" to nothingToDo,
            "asBlock(Ljava/lang/Object;)Ljustmc/Block;" to nothingToDo,
            "asLocation(Ljava/lang/Object;)Ljustmc/Location;" to nothingToDo,
            "asVector(Ljava/lang/Object;)Ljustmc/Vector;" to nothingToDo,
            "asCopyableList(Ljava/lang/Object;)Ljustmc/CopyableList;" to nothingToDo,
            "asMutableCopyableList(Ljava/lang/Object;)Ljustmc/MutableCopyableList;" to nothingToDo,
            "asCopyableMap(Ljava/lang/Object;)Ljustmc/CopyableMap;" to nothingToDo,
            "asMutableCopyableMap(Ljava/lang/Object;)Ljustmc/MutableCopyableMap;" to nothingToDo,
        ))
    }
}