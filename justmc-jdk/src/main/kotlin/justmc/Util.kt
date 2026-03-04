@file:Suppress("NOTHING_TO_INLINE")

package justmc

import justmc.enums.BooleanEnum

fun vector(x: Double, y: Double, z: Double): Vector = Vector.of(x, y, z)
fun location(x: Double, y: Double, z: Double): Location = Location.of(x, y, z)
fun location(x: Double, y: Double, z: Double, yaw: Float, pitch: Float): Location = Location.of(x, y, z, yaw, pitch)

infix fun <A : Primitive, B : Primitive> A.to(other: B): Pair<A, B> = Pair.of(this, other)
operator fun <A : Primitive> Pair<A, *>.component1(): A = this.first
operator fun <B : Primitive> Pair<*, B>.component2(): B = this.second

fun Boolean.toEnum(): BooleanEnum = BooleanEnum.of(this)
