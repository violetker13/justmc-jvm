package me.unidok.jjvm.nativemethod

//object EntityClass {
//    val CURRENT = NativeConstant("current")
//    val DEFAULT = NativeConstant("default_entity")
//    val KILLER = NativeConstant("killer_entity")
//    val DAMAGER = NativeConstant("damager_entity")
//    val VICTIM = NativeConstant("victim_entity")
//    val SHOOTER = NativeConstant("shooter_entity")
//    val PROJECTILE = NativeConstant("projectile_entity")
//    val RANDOM = NativeConstant("random_entity")
//    val ALL_MOBS = NativeConstant("all_mobs")
//    val ALL = NativeConstant("all_entities")
//    val LAST = NativeConstant("last_entity")
//
//    fun register() {
//        NativeClasses.registerFields(
//            "justmc/Entity", hashMapOf(
//                "CURRENT" to CURRENT,
//                "DEFAULT" to DEFAULT,
//                "KILLER" to KILLER,
//                "DAMAGER" to DAMAGER,
//                "VICTIM" to VICTIM,
//                "SHOOTER" to SHOOTER,
//                "PROJECTILE" to PROJECTILE,
//                "RANDOM" to RANDOM,
//                "ALL_MOBS" to ALL_MOBS,
//                "ALL" to ALL,
//                "LAST" to LAST
//            )
//        )
//    }
//}