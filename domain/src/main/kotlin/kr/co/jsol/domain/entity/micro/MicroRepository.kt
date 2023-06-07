package kr.co.jsol.domain.entity.micro

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.transaction.annotation.Transactional

interface MicroRepository : JpaRepository<Micro, Long> {

    // delete, update 시 Transactional, Modifying 필수
    @Transactional
    @Modifying
    @Query("update Micro micro set micro.isSend=true where micro.id in :ids")
    fun updateSendStatus(ids: List<Long>)
}
