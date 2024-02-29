import kotlin.system.*
import kotlinx.coroutines.*
import kotlin.AssertionError

fun main() {
    runBlocking {
        println("Weather forecast")
//        try {
//            println(getWeatherReport2())
//        } catch (e: AssertionError) {
//            println("Caught exception in runBlocking(): $e")
//            println("Report unavailable at this time")
//        }

//        println(getWeatherReport3())
        println(getWeatherReportCancel())
        println("Have a good day!")
    }
}

// 자식 코루틴에서 예외 발생 시 위쪽으로 전파됨
// 부모 코루틴이 취소 되면 자식 코루틴들은 자동 취소됨
// try {} catch() 로 예외 처리 가능하다. 시퀀스 코드가 여전히 동기식 코드 이므로 try-catch 블록이 여전히 예상되는 방식이므로
// 사용 가능하다.
suspend fun getWeatherReport2() = coroutineScope {
    val forecast = async { getForecast3() }
    val temperature = async { getTemperature3() }
    "${forecast.await()} ${temperature.await()}"
}

// getWeatherReport2함수는 기온의 예외발생으로 인해 날씨도 출력이 되지 않았다.
// 날씨는 출력을 하고 싶다면
suspend fun getWeatherReport3() = coroutineScope {
    val forecast = async { getForecast3() }
    val temperature = async {
        try {
            getTemperature3()
        } catch (e: AssertionError) {
            println("Caught exception $e")
            "{ No temperature found}"
        }
    }
    "${forecast.await()} ${temperature.await()}"
}

// 0.2초 후 기온 코루틴 취소, 같은 범위의 코루틴에 영향을 미치지 않고 부모 코루틴은 취소되지 않음
suspend fun getWeatherReportCancel() = coroutineScope {
    val forecast = async { getForecast3() }
    val temperature = async { getTemperature3() }

    delay(200)
    temperature.cancel()
    "${forecast.await()}"
}

suspend fun getForecast3(): String {
    delay(1000)
    return "Sunny"
}

// 예외 발생하도록 코드 추가
suspend fun getTemperature3(): String {
    delay(500)
    throw AssertionError("Temperature is invalid")
    return "30\u00b0C"
}

/**
 * 예외는 launch()로 시작된 코루틴과 async()로 시작된 코루틴에서 다르게 전파된다.
 * launch()로 시작된 코루틴 내에서는 예외가 즉시 발생한다.
 */
