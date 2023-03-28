package kr.co.jsol.domain.entity.user

import kr.co.jsol.domain.entity.site.Site
import kr.co.jsol.domain.entity.user.enums.UserRoleType
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

    @OneToOne(
        fetch = FetchType.LAZY,
        cascade = [CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH],
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
        role: UserRoleType? = this.role,
        locked: Boolean? = this.locked,
    ) {
        this.role = role ?: this.role
        this.locked = locked ?: this.locked
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
     * 계정 만료 여부
     * true : 만료 안됨
     * false : 만료
     * @return
     */
    override fun isAccountNonExpired(): Boolean {
        return !expired
    }

    /**
     * 계정 잠김 여부
     * true : 잠기지 않음
     * false : 잠김
     * @return
     */
    override fun isAccountNonLocked(): Boolean {
        return !locked
    }

    /**
     * 비밀번호 만료 여부
     * true : 만료 안됨
     * false : 만료
     * @return
     */
    override fun isCredentialsNonExpired(): Boolean {
        return !credentialExpired
    }

    /**
     * 사용자 활성화 여부
     * ture : 활성화
     * false : 비활성화
     * @return
     */
    override fun isEnabled(): Boolean {
        // 이메일이 인증되어 있고 계정이 잠겨있지 않으면 true
        return enabled
    }

    fun updatePassword(newPassword: String) {
        this.password = newPassword
    }
}
