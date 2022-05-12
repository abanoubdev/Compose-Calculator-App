package net.pubnative.lite.udemycoursecalculatorapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Card
import androidx.compose.material.Slider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.pubnative.lite.udemycoursecalculatorapp.components.InputField
import net.pubnative.lite.udemycoursecalculatorapp.ui.theme.BLACK
import net.pubnative.lite.udemycoursecalculatorapp.ui.theme.CardBackground
import net.pubnative.lite.udemycoursecalculatorapp.ui.theme.UdemyCourseCalculatorAppTheme
import net.pubnative.lite.udemycoursecalculatorapp.utils.calculateTipAmount
import net.pubnative.lite.udemycoursecalculatorapp.utils.calculateTotalPerPerson
import net.pubnative.lite.udemycoursecalculatorapp.widgets.RoundedIconButton

class MainActivity : ComponentActivity() {

    var mContext: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        @OptIn(ExperimentalComposeUiApi::class)
        super.onCreate(savedInstanceState)
        mContext = applicationContext
        setContent {
            UdemyCourseCalculatorAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    MyApp()
                }
            }
        }
    }
}

@Composable
fun MyApp() {
    MainContent()
}

@Composable
fun TopHeader(totalPerPerson: MutableState<Double>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(10.dp, 0.dp, 10.dp, 0.dp)
            .clip(CircleShape.copy(all = CornerSize(20.dp))),
        backgroundColor = CardBackground,
        border = BorderStroke(6.dp, CardBackground)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Total Per Person",
                style = TextStyle(
                    color = BLACK,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center
                )
            )

            val total = "%.2f".format(totalPerPerson.value)

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "$$total",
                style = TextStyle(
                    color = BLACK,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center
                )
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MainContent() {
    val totalBillState = remember {
        mutableStateOf("")
    }

    val validState = remember(totalBillState.value) {
        totalBillState.value.trim().isNotEmpty()
    }

    val splitValue = remember {
        mutableStateOf("1")
    }

    val sliderValueState = remember {
        mutableStateOf(0F)
    }

    val tipRowState = remember {
        mutableStateOf(0.0)
    }

    val totalPerPersonState = remember {
        mutableStateOf(0.0)
    }

    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier.padding(all = 10.dp),
    ) {

        TopHeader(totalPerPersonState)

        Spacer(modifier = Modifier.height(10.dp))

        Surface(
            modifier =
            Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(CornerSize(8.dp)),
            border = BorderStroke(1.dp, Gray)
        ) {
            Column {
                InputField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 10.dp, top = 5.dp),
                    valueState = totalBillState,
                    onValueChange = {
                        tipRowState.value =
                            calculateTipAmount(totalBillState.value, sliderValueState.value)
                        totalPerPersonState.value = calculateTotalPerPerson(
                            totalBillState.value,
                            splitValue.value.toInt(),
                            sliderValueState.value
                        )
                    },
                    labelId = "Enter Bill",
                    enabled = true,
                    isSingleLine = true,
                    onAction = KeyboardActions {
                        if (!validState) {
                            return@KeyboardActions
                        }
                        keyboardController?.hide()
                    })

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "Split",
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .align(Alignment.CenterVertically),
                    )

                    Spacer(modifier = Modifier.width(160.dp))

                    RoundedIconButton(imageVector = Icons.Default.Remove,
                        onClick = {
                            if (splitValue.value.toInt() > 1) {
                                var originalValue = splitValue.value.toInt()
                                originalValue -= 1
                                splitValue.value = originalValue.toString()

                                tipRowState.value =
                                    calculateTipAmount(totalBillState.value, sliderValueState.value)

                                totalPerPersonState.value = calculateTotalPerPerson(
                                    totalBillState.value,
                                    splitValue.value.toInt(),
                                    sliderValueState.value)
                            }
                        })

                    Spacer(modifier = Modifier.width(5.dp))

                    Text(
                        text = splitValue.value,
                        modifier = Modifier
                            .padding(start = 3.dp)
                            .align(Alignment.CenterVertically),
                    )

                    Spacer(modifier = Modifier.width(5.dp))

                    RoundedIconButton(imageVector = Icons.Default.Add,
                        onClick = {
                            var originalValue = splitValue.value.toInt()
                            originalValue += 1
                            splitValue.value = originalValue.toString()

                            tipRowState.value =
                                calculateTipAmount(totalBillState.value, sliderValueState.value)

                            totalPerPersonState.value = calculateTotalPerPerson(
                                totalBillState.value,
                                splitValue.value.toInt(),
                                sliderValueState.value)
                        })
                }
                //Tip Row

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 25.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "Tip",
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .align(Alignment.CenterVertically),
                    )

                    Spacer(modifier = Modifier.width(220.dp))

                    Text(
                        text = "$${tipRowState.value}",
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .align(Alignment.CenterVertically),
                    )
                }

                //Tip Percentage
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 25.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "${sliderValueState.value.toInt()} %",
                        style = TextStyle(
                            color = BLACK,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Center
                        )
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                    Slider(
                        value = sliderValueState.value, onValueChange = {
                            Log.d("TipPercentage", "$it")
                            sliderValueState.value = it
                            tipRowState.value =
                                calculateTipAmount(totalBillState.value, sliderValueState.value)

                            totalPerPersonState.value = calculateTotalPerPerson(
                                totalBillState.value,
                                splitValue.value.toInt(),
                                sliderValueState.value
                            )
                        },
                        valueRange = 0f..100f,
                        modifier = Modifier.padding(
                            start = 20.dp, end = 20.dp
                        ),
                        steps = 5
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview() {
    UdemyCourseCalculatorAppTheme {
        MyApp()
    }
}