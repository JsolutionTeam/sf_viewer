package kr.co.jsol.domain.entity.ascii_parser

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class AsciiToData {


    // 맥시멈이 134 펄스
    // 짧은시간 안에 이동하는 만 큼만 펄스로 측정.
    // 아스키코드(텍스트) 값 형식 : *xxxxxxxxxx.yyyy#
    // 인지시스템 회사에서 얘기하는 아스키코드 값 : 값 그 자체의 텍스트라고 함
    // x 부분이 0~1000
    // y 부분이 -1(역방향), 0(정지), 1(정방향)

    @Test
    fun `정지_인식이_정상적으로_처리된다`() {
        val str = "*0,0#" // 10 펄스, 역방향
        var pulse: Double = 0.0;
        var direction: Int = 0;

        // 메세지 형식이 제대로 들어왔는가?
        if(!isValidMessage(str)) assertThat(false);

        val split = str.split(",")
        var pulseStr: String = split[0]
        var directionMessage = split[1]

        pulse = parsePulse(pulseStr)
        direction = parseDirection(directionMessage)

        println("pulse = ${pulse}")
        println("direction = ${direction}")
        assertThat(pulse == 0.0) // 0.0 * 1.5
        assertThat(direction == 0)
    }

    @Test
    fun `정방향_인식이_정상적으로_처리된다`() {
        val str = "*20,1#" // 20 펄스, 정방향
        var pulse: Double = 0.0;
        var direction: Int = 0;

        // 메세지 형식이 제대로 들어왔는가?
        if(!isValidMessage(str)) assertThat(false);

        val split = str.split(",")
        var pulseStr: String = split[0]
        var directionMessage = split[1]

        pulse = parsePulse(pulseStr)
        direction = parseDirection(directionMessage)

        println("pulse = ${pulse}")
        println("direction = ${direction}")
        assertThat(pulse == 30.0) // 20 * 1.5
        assertThat(direction == 1)
    }

    @Test
    fun `역방향_인식이_정상적으로_처리된다`() {
        val str = "*133,-1#" // 133 펄스, 역방향
        var pulse: Double = 0.0;
        var direction: Int = 0;

        // 메세지 형식이 제대로 들어왔는가?
        if(!isValidMessage(str)) assertThat(false);

        val split = str.split(",")
        var pulseStr: String = split[0]
        var directionMessage = split[1]

        pulse = parsePulse(pulseStr)
        direction = parseDirection(directionMessage)

        println("pulse = ${pulse}")
        println("direction = ${direction}")
        assertThat(pulse == 199.5) // 133 * 1.5
        assertThat(direction == -1)
    }
    private fun isValidMessage(str: String): Boolean {
        if (isIotMessage(str)) return false// '*'으로 시작하고 '#'으로 끝나는가?
        if (isContainsComma(str)) return false// ','가 포함되어 있는가?
        return str.split(",").size == 2 // , 구분자로 2개로 나뉘어 지는가?
    }

    private fun isIotMessage(str: String) = str.startsWith("*") && str.endsWith("#")

    private fun isContainsComma(str: String) = str.contains(",")

    private fun parsePulse(pulseStr: String): Double {
        // 숫자 외 제거
        val pulseNumberStr = pulseStr.replace(Regex("[^0-9]"), "")
        println("pulseMessage = $pulseNumberStr")

        // 1.5 곱하기
        return pulseNumberStr.toDouble() * 1.5
    }

    private fun parseDirection(directionStr: String): Int {
        val directionNumberStr = directionStr.replace(Regex("[^-0-9]"), "")
        println("directionMessage = $directionNumberStr")

        return directionNumberStr.toInt()
    }
}
