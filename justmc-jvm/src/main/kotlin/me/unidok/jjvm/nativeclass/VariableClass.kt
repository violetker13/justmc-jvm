package me.unidok.jjvm.nativeclass

//object VariableClass {
//    fun register() {
//        NativeClasses.registerMethods("justmc/Variable", hashMapOf(
////            "save" to hashMapOf(
////                "(Ljava/lang/String;)Ljustmc/Variable;" to {
////                    stack.addFirst(Variable(stack.removeFirst().toString(), Variable.Scope.SAVE))
////                }
////            ),
////            "game" to hashMapOf(
////                "(Ljava/lang/String;)Ljustmc/Variable;" to {
////                    stack.addFirst(Variable(stack.removeFirst().toString(), Variable.Scope.GAME))
////                }
////            ),
////            "local" to hashMapOf(
////                "(Ljava/lang/String;)Ljustmc/Variable;" to {
////                    stack.addFirst(Variable(stack.removeFirst().toString(), Variable.Scope.LOCAL))
////                }
////            ),
////            "line" to hashMapOf(
////                "(Ljava/lang/String;)Ljustmc/Variable;" to {
////                    stack.addFirst(Variable(stack.removeFirst().toString(), Variable.Scope.LINE))
////                }
////            ),
////            "temp" to hashMapOf(
////                "()Ljustmc/Variable;" to {
////                    stack.addFirst(tempVar())
////                }
////            )
//        ))
//    }
//}