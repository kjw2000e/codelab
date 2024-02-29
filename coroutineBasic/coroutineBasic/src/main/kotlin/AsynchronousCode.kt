import kotlin.system.*
import kotlinx.coroutines.*

fun main() {
    val time = measureTimeMillis {
        runBlocking {
            println("Weather forecast")
            // launch keyword 사용
            // 코루틴을 실행하기 위해 launch() function 사용
            // 멀티 동시 실행을 하기 위해 2개의 launch() 사용

            // Synchronous code에서는 "sunny" 출력 후 1초간 block 되고
            // 그 다음 30이 출력됐지만 현재 코드에서는 동시에 처리됐다.
            // 즉 순차적으로 코드를 구성 했지만 동시에 작업이 실행한것처럼 보인다.
            // 코루틴으로 실행하면 여러 작업을 동시에 실행할 수 있다.

//            launch {
//                printForecast2()
//            }
//            launch {
//                printTemperature2()
//            }


//            val forecast: Deferred<String> = async { getForecast() }
//            val temperature: Deferred<String> = async { getTemperature() }
//
//            // 객체 접근 시 await() 사용
//            println("${forecast.await()} ${temperature.await()}")


            println(getWeatherReport())
            println("Have a good day!")
        }
    }

    println("Execution time : ${time / 1000.0} seconds")
}

suspend fun printForecast2() {
    delay(1000)
    println("sunny")
}

suspend fun printTemperature2() {
    delay(1000)
    println("30\u00b0C")
}

// 네트워크 통신을 통해 결과값을 return 받아야 하는 경우 launch()로 충분하지 않다.
// async() 함수를 사용하라.
// async()로 함수 호출 시 Deferred 객체가 반환된다.
// Deferred 객체에 접근하려면 await() 를 사용해야한다.
suspend fun getForecast(): String {
    delay(1000)
    return "sunny"
}

suspend fun getTemperature(): String {
    delay(1000)
    return "30\u00b0C"
}

suspend fun getWeatherReport() = coroutineScope {
    // coroutineScope()은 실행한 모든 코루틴을 포함한 모든 작업이 완료된 후에만 반환된다.
    // 즉 coroutineScope()을 사용하면 함수가 내부적으로 동시에 수행하더라도 모든 작업이 완료 될때까지
//    coroutineScope이 반환되지 않으므로 호출자는 동기 연산으로 보임.
    // 여기서 구조화된 동시성에 대한 핵심이 나오는데
    // 여러 개의 동시 연산을 하나의 동기 연산에 넣을 수 있다.
    val forecast = async { getForecast() }
    val temperature = async { getTemperature() }
    "${forecast.await()} ${temperature.await()}"
}