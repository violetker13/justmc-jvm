package me.unidok.jjvm.nativemethod

//object KotlinIntrinsics {
//    fun register() {
//        NativeMethods.register("kotlin/jvm/internal/Intrinsics.checkNotNullParameter(Ljava/lang/Object;Ljava/lang/String;)V") { method, context ->
//            val value = method.args[0].translate(context, null)
//            val paramName = method.args[1].requireConstString()
//            val source = context.source
//            val className = source.clazz.name
//            val method = source.method
//            val name = method.name
//            val desc = method.desc
//            context.addOperation(JustOperation(
//                "if_variable_equals",
//                mapOf(
//                    "value" to value,
//                    "compare" to DynamicConstant.CONST_0.value,
//                ),
//                listOf(JustOperation(
//                    "control_call_exception", mapOf(
//                        "type" to EnumValue("ERROR"),
//                        "message" to TextValue("Parameter specified as non-null is null: method $className.$name$desc, parameter $paramName")
//                    )
//                ))
//            ))
//            null
//        }
//    }
//}