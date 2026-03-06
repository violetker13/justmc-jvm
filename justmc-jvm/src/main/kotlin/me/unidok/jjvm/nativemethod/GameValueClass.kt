package me.unidok.jjvm.nativemethod

//object GameValueClass {
//    val CURRENT = NativeConstant("current")
//    val DEFAULT = NativeConstant("default")
//    val DEFAULT_ENTITY = NativeConstant("default_entity")
//    val KILLER = NativeConstant("killer")
//    val DAMAGER = NativeConstant("damager")
//    val VICTIM = NativeConstant("victim")
//    val SHOOTER = NativeConstant("shooter")
//    val PROJECTILE = NativeConstant("projectile")
//    val LAST_ENTITY = NativeConstant("last_entity")
//
//    fun register() {
//        NativeClasses.registerFields("justmc/GameValue", hashMapOf(
//            "CURRENT" to CURRENT,
//            "DEFAULT" to DEFAULT,
//            "DEFAULT_ENTITY" to DEFAULT_ENTITY,
//            "KILLER" to KILLER,
//            "DAMAGER" to DAMAGER,
//            "VICTIM" to VICTIM,
//            "SHOOTER" to SHOOTER,
//            "PROJECTILE" to PROJECTILE,
//            "LAST_ENTITY" to LAST_ENTITY
//        ))
////        NativeClasses.registerMethods("justmc/GameValue", hashMapOf(
////            "ofPlayer(Ljustmc/Player;)Ljustmc/GameValue;" to {
////                when (val player = (args[0] as NativeConstant).value) {
////                    "current" -> CURRENT
////                    "default_player" -> DEFAULT
////                    "killer_player" -> KILLER
////                    "damager_player" -> DAMAGER
////                    "victim_player" -> VICTIM
////                    "shooter_player" -> SHOOTER
////                    else -> throw IllegalArgumentException("Player '$player' cannot have game values")
////                }
////            },
////            "ofEntity(Ljustmc/Entity;)Ljustmc/GameValue;" to {
////                when (val entity = (args[0] as NativeConstant).value) {
////                    "current" -> CURRENT
////                    "default_entity" -> DEFAULT_ENTITY
////                    "killer_entity" -> KILLER
////                    "damager_entity" -> DAMAGER
////                    "victim_entity" -> VICTIM
////                    "shooter_entity" -> SHOOTER
////                    "projectile_entity" -> PROJECTILE
////                    "last_entity" -> LAST_ENTITY
////                    else -> throw IllegalArgumentException("Entity '$entity' cannot have game values")
////                }
////            },
////            "get(Ljava/lang/String;)Ljava/lang/Object;" to {
////                val id = args[0].asString(it)
////                DynamicConstant(GameValue(id))
////            },
////            "getValue(Ljava/lang/String;)Ljava/lang/Object;" to {
////                val id = args[0].asString(it)
////                val selector = GameValue.Selector.valueOf(self!!.asString(it).uppercase())
////                DynamicConstant(GameValue(id, selector))
////            }
////        ))
//    }
//}