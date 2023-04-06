package kr.co.jsol.api.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import kr.co.jsol.domain.entity.sensorDevice.SensorDeviceService
import kr.co.jsol.domain.entity.sensorDevice.dto.request.SensorDeviceCreateRequest
import kr.co.jsol.domain.entity.sensorDevice.dto.request.SensorDeviceSearchCondition
import kr.co.jsol.domain.entity.sensorDevice.dto.request.SensorDeviceUpdateRequest
import kr.co.jsol.domain.entity.sensorDevice.dto.response.SensorDeviceResponse
import org.hibernate.validator.constraints.Length
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/v1")
class SensorDeviceController(
    private val sensorDeviceService: SensorDeviceService,
) {

    @PostMapping("/sensor-device")
    @ResponseBody
    @ResponseStatus(value = HttpStatus.CREATED)
    fun createSensorDevice(
        @RequestBody sensorDeviceRequest: SensorDeviceCreateRequest,
    ): Long {
        return sensorDeviceService.saveSensorDevice(sensorDeviceRequest)
    }

    @GetMapping("/sensor-devices/{sensorDeviceId}")
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

    @GetMapping("/sensor-devices")
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    fun getSensorDeviceList(
        siteSearchCondition: SensorDeviceSearchCondition?,
    ): List<SensorDeviceResponse> {
        return sensorDeviceService.getSensorDevices(siteSearchCondition)
    }

    @PutMapping("/sensor-devices/{sensorDeviceId}/image")
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    @Operation(
        summary = "센서 장치 이미지 수정",
        description = "센서 장치 정보 중 이미지를 수정합니다.",
        responses = [
            ApiResponse(responseCode = "200", description = "수정 성공"),
            ApiResponse(responseCode = "404", description = "센서 장치 정보를 찾을 수 없습니다."),
        ],
        parameters = [
            Parameter(
                name = "imgFile",
                description = "이미지 파일",
                required = true,
                schema = Schema(implementation = MultipartFile::class)
            ),
        ]
    )
    fun updateSensorDeviceImage(
        @Length(min = 1, message = "sensorDeviceId를 제대로 입력해주세요")
        @PathVariable sensorDeviceId: Long,
        @RequestPart("imgFile") imgFile: MultipartFile,
    ) {
        if (imgFile == null) throw IllegalArgumentException("파일이 없습니다.")
        return sensorDeviceService.updateSensorDeviceImage(sensorDeviceId, imgFile)
    }

    @PutMapping("/sensor-devices/{sensorDeviceId}")
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    fun updateSensorDevice(
        @Length(min = 1, message = "sensorDeviceId를 제대로 입력해주세요")
        @PathVariable sensorDeviceId: Long,
        @Validated @RequestBody sensorDeviceRequest: SensorDeviceUpdateRequest,
    ) {
        return sensorDeviceService.updateSensorDevice(sensorDeviceId, sensorDeviceRequest)
    }

    @DeleteMapping("/sensor-devices/{sensorDeviceId}")
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    fun deleteSensorDevice(
        @Length(min = 1, message = "sensorDeviceId를 제대로 입력해주세요")
        @PathVariable sensorDeviceId: Long,
    ) {
        return sensorDeviceService.deleteSensorDevice(sensorDeviceId)
    }
}
