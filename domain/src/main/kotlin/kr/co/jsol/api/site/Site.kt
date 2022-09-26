package site

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "tb_site")
class Site(

    @Column(name = "site_nm", insertable = false, updatable = false)
    val name: String,

    @Id
    @Column(name = "site_seq", insertable = false, updatable = false)
    val id: Long,
)