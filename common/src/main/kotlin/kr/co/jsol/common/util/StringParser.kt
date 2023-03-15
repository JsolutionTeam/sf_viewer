package kr.co.jsol.common.util

fun String.parseLong(): Long {
    return this.replace(Regex("[^-0-9]"), "").toLong()
}

fun String.parseLongOrNull(): Long? {
    return this.replace(Regex("[^-0-9]"), "").toLongOrNull()
}

fun String.parseDouble(): Double {
    return this.replace(Regex("[^-0-9.]"), "").toDouble()
}

fun String.parseDoubleOrNull(): Double? {
    return this.replace(Regex("[^-0-9.]"), "").toDoubleOrNull()
}

fun String.parseInt(): Int {
    return this.replace(Regex("[^-0-9]"), "").toInt()
}

fun String.parseIntOrNull(): Int? {
    return this.replace(Regex("[^-0-9]"), "").toIntOrNull()
}
