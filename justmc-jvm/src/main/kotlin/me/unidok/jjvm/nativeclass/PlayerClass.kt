package me.unidok.jjvm.nativeclass

//object PlayerClass {
//    val CURRENT = NativeConstant("current")
//    val DEFAULT = NativeConstant("default_player")
//    val KILLER = NativeConstant("killer_player")
//    val DAMAGER = NativeConstant("damager_player")
//    val SHOOTER = NativeConstant("shooter_player")
//    val VICTIM = NativeConstant("victim_player")
//    val RANDOM = NativeConstant("random_player")
//    val ALL = NativeConstant("all_players")
//
//    fun register() {
//        NativeClasses.registerFields("justmc/Player", hashMapOf(
//            "CURRENT" to CURRENT,
//            "DEFAULT" to DEFAULT,
//            "KILLER" to KILLER,
//            "DAMAGER" to DAMAGER,
//            "SHOOTER" to SHOOTER,
//            "VICTIM" to VICTIM,
//            "RANDOM" to RANDOM,
//            "ALL" to ALL
//        ))
//        NativeClasses.registerMethods("justmc/Player", hashMapOf(
//            "sendMessage(Ljava/lang/String;)V" to {
//                translateFuture {
//                    val message = arg(0).translate(this, null)
//                    val selector = self as NativeConstant
//                    addOperation(JustOperation(
//                        "player_send_message", mapOf(
//                            "messages" to message
//                        ), selection = selector.value
//                    ))
//                }
//                null
//            }
//        ))
//    }
//}