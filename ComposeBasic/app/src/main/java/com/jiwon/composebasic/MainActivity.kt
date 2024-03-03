package com.jiwon.composebasic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jiwon.composebasic.ui.theme.ComposeBasicTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeBasicTheme {
                MyApp(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
fun MyApp(modifier: Modifier = Modifier,
          names: List<String> = listOf("word", "compose")) {

    // Column 중첩 ,
    Column(modifier = modifier.padding(vertical = 4.dp)) {
        for (name in names) {
            Greeting(name)
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    // 상태값이 변경이 되었을때 해당 부분만 변경한다.
    // 컴포즈에서 상태값 추적을 하기 위해서는 MutableStateOf 함수를 사용해야한다.
    // 재구성이 여러번 발생할 수 있어 단독으로 할당하진 못하고 remeber키워드가 필요.
    val expanded = remember {
        mutableStateOf(false)
    }

    val extrapadding = if (expanded.value) 48.dp else 0.dp

    Surface(
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        // row : horizontal 배치
        // Column : vertical 배치

        // 행 끝으로 배치하는 방법 : weight 가중치 만들기
        // weight 수정자는 사용 가능한 공간 전체를 채운다. 가중치가 없는 다른 요소를 효과적으로 밀어냄.
        Row(modifier = Modifier.padding(24.dp)) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = extrapadding)
            ) {
                Text( text = "Hello" )
                Text( text = "$name!")
            }

            ElevatedButton(onClick = { expanded.value = !expanded.value }) {
                Text(if (expanded.value) "Show less" else "Show more")
            }
        }
    }
}

@Composable
private fun Greetings(
    modifier: Modifier = Modifier,
    names: List<String> = listOf("World", "Compose")
) {
    Column(modifier = modifier.padding(vertical = 4.dp)) {
        for (name in names) {
            Greeting(name = name)
        }
    }
}

@Composable
fun OnboardingScreen(modifier: Modifier = Modifier) {
    // TODO: This state should be hoisted
    var shouldShowOnboarding by remember { mutableStateOf(true) }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome to the Basics Codelab!")
        Button(
            modifier = Modifier.padding(vertical = 24.dp),
            onClick = { shouldShowOnboarding = false }
        ) {
            Text("Continue")
        }
    }
}

@Composable
fun OnboardingScreenByHoisting(
    onContinueClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome to the Basics Codelab!")
        Button(
            modifier = Modifier.padding(vertical = 24.dp),
            onClick = onContinueClicked
        ) {
            Text("Continue")
        }
    }
}

@Composable
fun MyAppHoisting(modifier: Modifier = Modifier) {
    // by 사용시 위임을 하기 때문에 .value 호출 없이 값 접근시 사용할 수 있음.
    // 가장 상위에 state값 hoisting. onClick 콜백 함수에서 상태값 변경.
    // recomposition 발생 후 Greetings() 실행됨
    var shouldShowOnBoarding by remember { mutableStateOf(true) }
    Surface(modifier) {
        if (shouldShowOnBoarding) {
            OnboardingScreenByHoisting(onContinueClicked = { shouldShowOnBoarding = false })
        } else {
            Greetings()
        }
    }
}



@Preview(showBackground = true, name = "미리보기", widthDp = 320)
@Composable
fun GreetingPreview() {
    ComposeBasicTheme {
        Greetings()
//        MyApp()
//        OnboardingScreen()
    }
}

@Preview()
@Composable
fun MyAppPreview() {
    ComposeBasicTheme {
        MyAppHoisting()
    }
}