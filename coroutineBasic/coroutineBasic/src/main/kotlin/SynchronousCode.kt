import kotlin.system.*
import kotlinx.coroutines.*

fun main() {
    // measureTimeMillis 는 블록 안에 내용을 다 수행후 경과 시간을 반환한다.
    val time = measureTimeMillis {
        runBlocking {
            println("Weather forecast")
            printForecast()
            printTemperature()
        }
    }
    println("Execution time : ${time / 1000.0} seconds")
}

suspend fun printForecast() {
    delay(1000)
    println("sunny")
}

suspend fun printTemperature() {
    delay(1000)
    println("30\u00b0C")
}