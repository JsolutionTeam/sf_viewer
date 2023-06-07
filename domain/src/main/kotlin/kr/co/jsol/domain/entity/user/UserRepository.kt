package kr.co.jsol.domain.entity.user

import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, String> {
    fun findByIdAndLockedIsFalse(id: String): User?
}
