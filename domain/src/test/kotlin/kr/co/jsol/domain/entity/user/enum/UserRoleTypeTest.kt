package kr.co.jsol.domain.entity.user.enum

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class UserRoleTypeTest {

    @Test
    @DisplayName("User Role Type 값을 가져온다")
    fun getUserRoleTypeName() {
        val admin: UserRoleType = UserRoleType.ROLE_ADMIN
        val user: UserRoleType = UserRoleType.ROLE_USER

        println("admin.name = ${admin.name}")
        println("user.name = ${user.name}")

        assertThat(admin.name).isEqualTo("ROLE_ADMIN")
        assertThat(user.name).isEqualTo("ROLE_USER")
    }
}
