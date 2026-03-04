package me.unidok.jjvm.nativeclass

//object LocationClass {
//    fun register() {
//        NativeClasses.registerMethods("justmc/Location", hashMapOf(
////            "of" to hashMapOf(
////                "(DDD)Ljustmc/Location;" to {
////                    val z = stack.removeFirst()
////                    val y = stack.removeFirst()
////                    val x = stack.removeFirst()
////                    if (x is NumberValue && y is NumberValue && z is NumberValue) {
////                        stack.addFirst(LocationValue(x.number, y.number, z.number, 0.0, 0.0))
////                    } else {
////                        val variable = tempVar()
////                        operations.add(Operation("set_variable_set_all_coordinates", mapOf(
////                            "variable" to variable,
////                            "x" to x,
////                            "y" to y,
////                            "z" to z
////                        )))
////                        stack.addFirst(variable)
////                    }
////                },
////                "(DDDFF)Ljustmc/Location;" to {
////                    val pitch = stack.removeFirst()
////                    val yaw = stack.removeFirst()
////                    val z = stack.removeFirst()
////                    val y = stack.removeFirst()
////                    val x = stack.removeFirst()
////                    if (x is NumberValue && y is NumberValue && z is NumberValue && yaw is NumberValue && pitch is NumberValue) {
////                        stack.addFirst(LocationValue(x.number, y.number, z.number, yaw.number, pitch.number))
////                    } else {
////                        val variable = tempVar()
////                        operations.add(Operation("set_variable_set_all_coordinates", mapOf(
////                            "variable" to variable,
////                            "x" to x,
////                            "y" to y,
////                            "z" to z,
////                            "yaw" to yaw,
////                            "pitch" to pitch
////                        )))
////                        stack.addFirst(variable)
////                    }
////                }
////            ),
////            "getX" to hashMapOf(
////                "()D" to {
////                    val location = stack.removeFirst()
////                    if (location is LocationValue) {
////                        stack.addFirst(NumberValue(location.x))
////                    } else {
////                        val variable = tempVar()
////                        operations.add(Operation("set_variable_get_coordinate", mapOf(
////                            "variable" to variable,
////                            "location" to location,
////                            "type" to EnumValue("X")
////                        )))
////                        stack.addFirst(variable)
////                    }
////                }
////            ),
////            "getY" to hashMapOf(
////                "()D" to {
////                    val location = stack.removeFirst()
////                    if (location is LocationValue) {
////                        stack.addFirst(NumberValue(location.y))
////                    } else {
////                        val variable = tempVar()
////                        operations.add(Operation("set_variable_get_coordinate", mapOf(
////                            "variable" to variable,
////                            "location" to location,
////                            "type" to EnumValue("Y")
////                        )))
////                        stack.addFirst(variable)
////                    }
////                }
////            ),
////            "getZ" to hashMapOf(
////                "()D" to {
////                    val location = stack.removeFirst()
////                    if (location is LocationValue) {
////                        stack.addFirst(NumberValue(location.z))
////                    } else {
////                        val variable = tempVar()
////                        operations.add(Operation("set_variable_get_coordinate", mapOf(
////                            "variable" to variable,
////                            "location" to location,
////                            "type" to EnumValue("Z")
////                        )))
////                        stack.addFirst(variable)
////                    }
////                }
////            ),
////            "getYaw" to hashMapOf(
////                "()F" to {
////                    val location = stack.removeFirst()
////                    if (location is LocationValue) {
////                        stack.addFirst(NumberValue(location.yaw))
////                    } else {
////                        val variable = tempVar()
////                        operations.add(Operation("set_variable_get_coordinate", mapOf(
////                            "variable" to variable,
////                            "location" to location,
////                            "type" to EnumValue("YAW")
////                        )))
////                        stack.addFirst(variable)
////                    }
////                }
////            ),
////            "getPitch" to hashMapOf(
////                "()F" to {
////                    val location = stack.removeFirst()
////                    if (location is LocationValue) {
////                        stack.addFirst(NumberValue(location.pitch))
////                    } else {
////                        val variable = tempVar()
////                        operations.add(Operation("set_variable_get_coordinate", mapOf(
////                            "variable" to variable,
////                            "location" to location,
////                            "type" to EnumValue("PITCH")
////                        )))
////                        stack.addFirst(variable)
////                    }
////                }
////            ),
//        ))
//    }
//}