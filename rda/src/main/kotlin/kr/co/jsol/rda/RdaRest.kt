package kr.co.jsol.rda

import kr.co.jsol.domain.entity.sensorDevice.dto.request.SensorDeviceCreateRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/sensor")
class RdaRest(
    private val rdaService: RdaService,
) {
    @PostMapping("/sensor-device")
    @ResponseBody
    @ResponseStatus(value = HttpStatus.CREATED)
    fun createSensorDevice(
        @RequestBody sensorDeviceRequest: SensorDeviceCreateRequest,
    ): Long {
        return rdaService.saveSensorDevice(sensorDeviceRequest)
    }
}
