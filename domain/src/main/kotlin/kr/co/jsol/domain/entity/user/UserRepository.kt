package kr.co.jsol.domain.entity.user

import kr.co.jsol.domain.entity.user.dto.response.UserResponse
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, String> {
    fun findByIdAndLockedIsFalse(id: String): User?

}
