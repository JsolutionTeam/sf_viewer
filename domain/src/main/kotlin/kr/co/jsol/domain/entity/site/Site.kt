package kr.co.jsol.domain.entity.site

import kr.co.jsol.domain.entity.site.dto.request.SiteUpdateRequest
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "tb_site")
class Site(
    @Column(name = "site_nm")
    var name: String = "",

    @Column(name = "logger_delay", nullable = false)
    var delay: Long = 1000L,

    @Id
    @Column(name = "site_seq", updatable = false)
    val id: Long,
) {
    fun update(
        name: String?,
        delay: Long?,
    ) {
        this.name = name ?: this.name
        this.delay = delay ?: this.delay
    }
}
