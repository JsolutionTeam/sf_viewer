package kr.co.jsol.domain.entity.ingsystem

import org.springframework.stereotype.Service

@Service
class InGSystemService() {
    var inGSystemRepository: InGSystemRepository = TODO()

    constructor(inGSystemRepository: InGSystemRepository) : this() {
        this.inGSystemRepository = inGSystemRepository
    }

    fun saveInGSystem(input: String): String{
        var rateOfOpening: Double = 0.0;
        var openSignal: Int = 0;

        // 메세지 형식이 제대로 들어왔는가?
        if(!isValidMessage(input)) return "메세지 형식이 잘못되었습니다.";

        val split = input.split(",")
        var rateOfOpeningMessage: String = split[0]
        var openSignalMessage = split[1]

        rateOfOpening = parsePulse(rateOfOpeningMessage)
        openSignal = parseDirection(openSignalMessage)

        inGSystemRepository.save(InGSystem(
            rateOfOpening = rateOfOpening,
            openSignal = openSignal,
        ))
        return "200 OK, save success"
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
