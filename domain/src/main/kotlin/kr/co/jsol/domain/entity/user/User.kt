package kr.co.jsol.domain.entity.user

import kr.co.jsol.domain.entity.site.Site
import kr.co.jsol.domain.entity.user.enum.UserRoleType
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.*

@Entity
@Table(name = "tb_user")
class User(
    @Column(name = "password", length = 255, nullable = false)
    private var password: String,

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    var role: UserRoleType,

    @ManyToOne(
        fetch = FetchType.LAZY,
        cascade = [CascadeType.ALL],
    )
    @JoinColumn(
        name = "site_seq",
        foreignKey = ForeignKey(name = "tb_user_site_seq_fk"),
    )
    var site: Site? = null,

    @Id
    private val id: String,
) : UserDetails {

    @Column(name = "is_locked")
    private var locked = false
    @Column(name = "is_enabled")
    private var enabled = true
    @Column(name = "is_expired")
    private var expired = false
    @Column(name = "is_credentials_expired")
    private var credentialExpired = false

    // ------------------------------------
    fun updateInfo(
        role: UserRoleType = this.role,
        site: Site? = this.site,
    ) {
        this.role = role
        this.site = site
    }

    fun disable() {
        this.locked = true
        this.enabled = false
        this.expired = true
        this.credentialExpired = true
    }

    fun useable() {
        this.locked = false
        this.enabled = true
        this.expired = false
        this.credentialExpired = false
    }

    override fun getAuthorities(): Collection<GrantedAuthority>? {
        val authorities: MutableList<GrantedAuthority> = ArrayList()
        authorities.add(SimpleGrantedAuthority(this.role.name))
        return authorities
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return id
    }

    /**
     * ?????? ?????? ??????
     * true : ?????? ??????
     * false : ??????
     * @return
     */
    override fun isAccountNonExpired(): Boolean {
        return !expired
    }

    /**
     * ?????? ?????? ??????
     * true : ????????? ??????
     * false : ??????
     * @return
     */
    override fun isAccountNonLocked(): Boolean {
        return !locked
    }

    /**
     * ???????????? ?????? ??????
     * true : ?????? ??????
     * false : ??????
     * @return
     */
    override fun isCredentialsNonExpired(): Boolean {
        return !credentialExpired
    }

    /**
     * ????????? ????????? ??????
     * ture : ?????????
     * false : ????????????
     * @return
     */
    override fun isEnabled(): Boolean {
        // ???????????? ???????????? ?????? ????????? ???????????? ????????? true
        return enabled
    }
}
