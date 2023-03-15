package kr.co.jsol.domain.entity.opening

import kr.co.jsol.domain.config.WithAccount
import kr.co.jsol.domain.entity.site.Site
import kr.co.jsol.domain.entity.site.SiteRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@SpringBootTest
@DisplayName("005. 개폐데이터 조회 테스트")
class OpeningQuerydslRepositoryTest @Autowired constructor(
    private val openingRepository: OpeningRepository,
    private val openingQuerydslRepository: OpeningQuerydslRepository,
    private val siteRepository: SiteRepository,
) {

    @Test
    @DisplayName(value = "사이트 번호로 가장 최신 개폐 데이터가 조회된다.")
    @WithAccount
    @Transactional
    fun getRealTimeBySiteSeq() {
        // given
        val siteSeq = 2L

        val site = siteRepository.save(Site(id = 2, name = "테스트-양파-제솔"))

        // 5분 데이터 생성
        val first = LocalDateTime.of(2023, 2, 6, 11, 5, 30)
        // 5분 + 5분 => 10분 데이터 생성
        val second = first.plusMinutes(5)

        openingRepository.saveAll(
            listOf(
                Opening(
                    rateOfOpening = 0.0,
                    openSignal = 0,
                    site = site,
                    regTime = first
                ),
                Opening(
                    rateOfOpening = 0.0,
                    openSignal = 0,
                    site = site,
                    regTime = second
                ),
            )
        )

        // when
        val realTimeBySiteSeq = openingQuerydslRepository.findInGSystemBySiteSeq(2)

        // then
        // 제일 최신 데이터이므로 10분 데이터가 나와야 한다.
        assertThat(realTimeBySiteSeq?.regTime?.minute?.equals(10))
        println(realTimeBySiteSeq)
    }

    @Test
    fun getSummaryBySearchCondition() {
    }
}
