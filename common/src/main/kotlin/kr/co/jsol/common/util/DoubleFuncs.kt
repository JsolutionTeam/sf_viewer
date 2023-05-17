package kr.co.jsol.common.util

inline fun safeDoubleValue(block: () -> Double): Double {
    return try {
        block()
    } catch (_: Exception) {
        0.0
    }
}
