package kr.co.jsol.domain.entity.site.dto.response

import com.fasterxml.jackson.annotation.JsonFormat
import io.swagger.v3.oas.annotations.media.Schema
import kr.co.jsol.domain.entity.co2.dto.Co2Dto
import kr.co.jsol.domain.entity.opening.dto.OpeningResDto
import kr.co.jsol.domain.entity.micro.dto.MicroDto
import java.time.LocalDateTime

@Schema(description = "데이터베이스 조회 결과")
data class RealTimeResponse(
    @Schema(description = "site id")
    val siteSeq: Long = 0L,

    @Schema(description = "이산화탄소 농도 / 단위 ppm")
    var co2: Double? = 0.0,

    @Schema(description = "온도 / 단위 ℃")
    var temperature: Double? = 0.0,

    @Schema(description = "습도 / 단위 %")
    var relativeHumidity: Double? = 0.0,

    @Schema(description = "작물 근접 온도 / 단위 ℃")
    var cropTemperature: Double? = 0.0,

    @Schema(description = "작물 근접 습도 / 단위 %")
    var cropHumidity: Double? = 0.0,

    @Schema(description = "일사량 / 단위 W/㎡")
    var solarRadiation: Double? = 0.0,

    @Schema(description = "강우량 / 단위 mm")
    var rainfall: Double? = 0.0,

    @Schema(description = "대지 습도 / 단위 ℃")
    var earthHumidity: Double? = 0.0,

    @Schema(description = "대지 온도 / 단위 ℃")
    var earthTemperature: Double? = 0.0,

    @Schema(description = "풍향 / 단위 ˚(각도)")
    var windDirection: Double? = 0.0,

    @Schema(description = "풍속 / 단위 m/s")
    var windSpeed: Double? = 0.0,

    @Schema(description = "개폐장치 개폐 정도")
    var rateOfOpening: Double? = 0.0,

    @Schema(description = "개폐장치 작동 구분자, -1=역방향, 0=멈춤, 1=정방향", allowableValues = ["-1", "0", "1"])
    var openSignal: Int? = 0,

    @Schema(description = "co2 외 데이터 수집시간", format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    var microRegTime: LocalDateTime? = null,

    @Schema(description = "co2 데이터 수집시간", format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    var co2RegTime: LocalDateTime? = null,

    @Schema(description = "개폐장치 데이터 수집시간", format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    var openDataRegTime: LocalDateTime? = null,
) {

    fun updateCo2(dto: Co2Dto) {
        this.co2 = dto.co2
        this.co2RegTime = dto.regTime
    }

    fun setMicro(dto: MicroDto) {
        this.temperature = dto.temperature
        this.relativeHumidity = dto.relativeHumidity
        this.cropTemperature = dto.cropTemperature
        this.cropHumidity = dto.cropHumidity
        this.solarRadiation = dto.solarRadiation
        this.rainfall = dto.rainfall
        this.earthHumidity = dto.earthHumidity
        this.earthTemperature = dto.earthTemperature
        this.windDirection = dto.windDirection
        this.windSpeed = dto.windSpeed
        this.microRegTime = dto.regTime
    }

    fun setOpening(inGDto: OpeningResDto?) {
        this.rateOfOpening = inGDto?.rateOfOpening
        this.openSignal = inGDto?.openSignal
        this.openDataRegTime = inGDto?.regTime
    }
}
