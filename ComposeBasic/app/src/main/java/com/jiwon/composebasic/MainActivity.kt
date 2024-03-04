package com.jiwon.composebasic

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.font.FontWeight
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
//    val expanded = remember {
//        mutableStateOf(false)
//    }

    var expanded by rememberSaveable {
        mutableStateOf(false)
    }

//    val extrapadding = if (expanded.value) 48.dp else 0.dp

    // animateDpAsState: 애니메이션이 완료될 때까지 값이 계속 업데이트되는 State 객체를 반환
    // 타입은 dp
    val extrapadding by animateDpAsState(
        if (expanded) 48.dp else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

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
                    .padding(bottom = extrapadding.coerceAtLeast(0.dp))
                // extrapadding.coerceAtLeast(0.dp) : 이거 없으면 에러발생함. 패딩값이 -값이 되는거 방지하는듯.
            ) {
                Text( text = "Hello" )
                // 기존 글꼴 스타일에서 조금 더 변형을 하는 방법으로 copy하여 내부 속성을 커스텀할 수 있다.
                Text( text = name, style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold
                ))
            }

            ElevatedButton(onClick = { expanded = !expanded }) {
                Text(if (expanded) "Show less" else "Show more")
            }
        }
    }
}

@Composable
private fun Greetings(
    modifier: Modifier = Modifier,
//    names: List<String> = listOf("World", "Compose")
    names: List<String> = List(1000) { "$it" }
) {
    // 여러개의 리스트를 처리하기 위해 LazyColumn을 사용. recyclerView 역할이지만
    // 자식을 재활용하지 않는다는 점이 차이점이다.
    // 스크롤 할때 새로운 컴포저블을 생성하는데 뷰를 인스턴스화 하는 것보다 비용이 적게든다.
    // items()에 처리할 아이템의 리스트 넣어줌
    LazyColumn(modifier = modifier.padding(vertical = 4.dp)) {
//        for (name in names) {
//            Greeting(name = name)
//        }

        items(items = names) { name ->
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
//    var shouldShowOnBoarding by remember { mutableStateOf(true) }

    // 화면이 돌아가는 상황과 같은 여러 configuration에서 값이 초기화되는데
    // 이를 방지하고 계속 저장된 값을 유지하려면 rememberSaveable 키워드를 사용해야한다.
    var shouldShowOnboarding by rememberSaveable { mutableStateOf(true) }

    Surface(modifier) {
        if (shouldShowOnboarding) {
            OnboardingScreenByHoisting(onContinueClicked = { shouldShowOnboarding = false })
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

// dark mode 추가
// uiMode = UI_MODE_NIGHT_YES,
@Preview(
    showBackground = true,
    widthDp = 320,
    uiMode = UI_MODE_NIGHT_YES,
    name = "GreetingPreviewDark"
)
@Preview(showBackground = true, widthDp = 320)
@Composable
fun GreetingPreviewDarkMode() {
    ComposeBasicTheme {
        Greetings()
    }
}