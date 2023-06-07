package kr.co.jsol.rda

enum class ERdaType {
    MICRO,
    CO2,
    SENSOR,
    ;

    fun getCollectorType(): ECollectorType {
        return when (this) {
            MICRO, CO2, -> ECollectorType.HOBOWARE
            SENSOR, -> ECollectorType.INGSYSTEM
        }
    }
}
