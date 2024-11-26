package com.kodiiiofc.urbanuniversity.jetpackcompose.unidirectionaldataflow

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.format.Formatter
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableDoubleState
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kodiiiofc.urbanuniversity.jetpackcompose.unidirectionaldataflow.ui.theme.UnidirectionalDataFlowTheme
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UnidirectionalDataFlowTheme {

                val height = remember { mutableIntStateOf(170) }
                val bodyMass = remember { mutableIntStateOf(60) }
                val ibmIndex =
                    remember {
                        derivedStateOf {
                            if (bodyMass.intValue == 0 || height.intValue == 0)
                                0.0
                            else bodyMass.intValue / (height.intValue / 100.0 * height.intValue / 100.0) } }

                Column(
                    Modifier
                        .padding(16.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.LightGray)
                        .fillMaxWidth()
                ) {

                    Text(
                        "Калькулятор ИМТ", textAlign = TextAlign.Center, fontSize = 24.sp,
                        modifier = Modifier
                            .background(Color.DarkGray)
                            .fillMaxWidth()
                            .padding(12.dp),
                        color = Color.White
                    )
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Input("Рост, см", height)
                        Input("Вес, кг", bodyMass)
                        OutputBMI(ibmIndex)
                        ResetButton(height, bodyMass)
                    }


                }


            }
        }
    }
}

@Composable
fun Input(text: String, valueState: MutableIntState) {
    Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {

        Text(text = "$text:", fontSize = 20.sp)

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(4.dp)) {

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.Blue)
                    .size(24.dp)
                    .clickable { valueState.intValue -= 5 },
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(16.dp, 3.dp)
                        .background(Color.White)
                )
            }

            Text(
                text = "${valueState.intValue}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(20.dp, 4.dp),
            )

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.Red)
                    .size(24.dp)
                    .clickable { valueState.intValue += 5 },
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(16.dp, 3.dp)
                        .background(Color.White)
                )
                Box(
                    modifier = Modifier
                        .size(3.dp, 16.dp)
                        .background(Color.White)
                )
            }


        }

    }
}

@Preview(showBackground = true)
@Composable
fun InputPreview() {
    val height = remember { mutableIntStateOf(40) }
    Input("Рост", height)
}

@Composable
fun OutputBMI(valueState: State<Double>) {

    val outputValue = if (valueState.value < 0.0) 0.0 else valueState.value

    val ibmClass = when (outputValue) {
        0.0 -> "Недостаточно данных или они некорректные"
        in 5.0..<16.0 -> "Выраженный дефицит массы тела"
        in 16.0..<18.5 -> "Недостаточная масса тела"
        in 18.5..25.0 -> "Нормальная масса тела"
        in 25.0..<30.0 -> "Избыточная масса тела (предожирение)"
        in 30.0..<35.0 -> "Ожирение 1-ой степени"
        in 35.0..40.0 -> "Ожирение 2-ой степени"
        else -> "Ожирение 3-ой степени"
    }

    val textValue = String.format(Locale.ROOT, "%.2f", outputValue)

    Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Коэффициент ИМТ:", fontSize = 20.sp)
        Text(
            textValue,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(20.dp, 4.dp)
        )
        Text(
            ibmClass,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(20.dp, 8.dp)
                .shadow(2.dp, CircleShape)
                .clip(CircleShape)
                .background(Color.White)
                .padding(20.dp, 4.dp)
        )
    }

}

@Composable
fun ResetButton(vararg mutableIntStates: MutableIntState) {
    Text(
        "Сбросить",
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Gray,
        modifier = Modifier
            .padding(20.dp, 8.dp)
            .shadow(2.dp, CircleShape)
            .clip(CircleShape)
            .background(Color.White)
            .padding(20.dp, 4.dp)
            .clickable {
                mutableIntStates.forEach {
                    it.intValue = 0
                }
            }
    )
}