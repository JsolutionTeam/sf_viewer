package kr.co.jsol.domain.entity.sensor

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.transaction.annotation.Transactional

interface SensorRepository : JpaRepository<Sensor, Long> {
    fun findAllByIsSendIsFalse(): List<Sensor>

    // delete, update 시 Transactional, Modifying 필수
    @Transactional
    @Modifying
    @Query("update Sensor sensor set sensor.isSend=true where sensor.id in ?1")
    fun updateSendStatus(ids: List<Long>)
}
