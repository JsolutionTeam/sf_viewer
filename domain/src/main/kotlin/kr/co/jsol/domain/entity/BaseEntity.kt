package kr.co.jsol.domain.entity

import org.hibernate.annotations.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.*

// hibernate 자체에서 inheritance를 지원하지 않는다.
// @Where(clause = "deleted_at IS NULL")

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
open class BaseEntity {
    @field:CreatedDate
    @Column(name = "created_at", updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now()

    @Column(name = "created_by")
    private var createdBy: String? = null

    @field:LastModifiedDate
    @Column(name = "updated_at")
    var updatedAt: LocalDateTime = LocalDateTime.now()

    @Column(name = "updated_by")
    private var updatedBy: String? = null

    @Column(name = "deleted_at")
    private var deletedAt: LocalDateTime? = null

    @Column(name = "deleted_by")
    private var deletedBy: String? = null

    fun createdBy(username: String) {
        this.createdBy = username
    }

    fun updatedBy(username: String) {
        this.updatedBy = username
    }

    fun softDelete(username: String) {
        this.deletedAt = LocalDateTime.now()
        this.deletedBy = username
    }

    fun isDeleted(): Boolean {
        return this.deletedAt != null
    }
}
