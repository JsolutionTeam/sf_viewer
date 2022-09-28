package kr.co.jsol.api.entity.user

import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, String>{
    fun findByIdAndLockedIsFalse(id: String): User?
}
