package kr.co.jsol.api.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import kr.co.jsol.domain.entity.sensorDevice.dto.request.SensorDeviceCreateRequest
import kr.co.jsol.domain.entity.sensorDevice.SensorDeviceService
import kr.co.jsol.domain.entity.sensorDevice.dto.request.SensorDeviceSearchCondition
import kr.co.jsol.domain.entity.sensorDevice.dto.request.SensorDeviceUpdateRequest
import kr.co.jsol.domain.entity.sensorDevice.dto.response.SensorDeviceResponse
import org.hibernate.validator.constraints.Length
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1")
class SensorDeviceController(
    private val sensorDeviceService: SensorDeviceService,
) {

    @PostMapping("/sensorDevice")
    @ResponseBody
    @ResponseStatus(value = HttpStatus.CREATED)
    fun createSensorDevice(
        @RequestBody sensorDeviceRequest: SensorDeviceCreateRequest,
    ):Unit {
        sensorDeviceService.saveSensorDevice(sensorDeviceRequest)
    }

    @GetMapping("/sensorDevices/{sensorDeviceId}")
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    @Operation(
        summary = "센서 장치 정보 단일 조회",
        description = "센서 장치 정보를 하나만 조회합니다.",
        responses = [
            ApiResponse(responseCode = "200", description = "센서 장치 정보 조회 성공"),
            ApiResponse(responseCode = "404", description = "센서 장치 정보를 찾을 수 없습니다."),
        ]
    )
    fun getSensorDevice(
        @PathVariable sensorDeviceId: Long,
    ): SensorDeviceResponse? {
        return sensorDeviceService.getSensorDeviceById(sensorDeviceId)
    }

    @GetMapping("/sensorDevices")
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    fun getSensorDeviceList(
        siteSearchCondition: SensorDeviceSearchCondition?,
    ): List<SensorDeviceResponse> {
        return sensorDeviceService.getSensorDevices(siteSearchCondition)
    }

    @PutMapping("/sensorDevices/{sensorDeviceId}")
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    fun updateSensorDevice(
        @Length(min = 1, message = "sensorDeviceId를 제대로 입력해주세요")
        @PathVariable sensorDeviceId: Long,
        @Validated @RequestBody sensorDeviceRequest: SensorDeviceUpdateRequest,
    ): Unit {
        return sensorDeviceService.updateSensorDevice(sensorDeviceId, sensorDeviceRequest)
    }

    @DeleteMapping("/sensorDevices/{sensorDeviceId}")
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    fun deleteSensorDevice(
        @Length(min = 1, message = "sensorDeviceId를 제대로 입력해주세요")
        @PathVariable sensorDeviceId: Long,
    ): Unit {
        return sensorDeviceService.deleteSensorDevice(sensorDeviceId)
    }
}
