package kr.co.jsol.domain.entity.co2

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.transaction.annotation.Transactional

interface Co2LoggerRepository : JpaRepository<Co2Logger, Long> {

    // delete, update 시 Transactional, Modifying 필수
    @Transactional
    @Modifying
    @Query("update Co2Logger co2Logger set co2Logger.isSend=true where co2Logger.id in :ids")
    fun updateSendStatus(ids: List<Long>)
}
