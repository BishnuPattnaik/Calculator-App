package eu.tutorials.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.style.TextOverflow
import eu.tutorials.calculator.ui.theme.CalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculatorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CalculatorApp()
                }
            }
        }
    }
}

@Composable
fun CalculatorApp() {
    var displayText by remember { mutableStateOf("") }
    var firstOperand by remember { mutableStateOf("") }
    var operator by remember { mutableStateOf("") }
    var secondOperand by remember { mutableStateOf("") }
    val displayBackground = remember {
        Brush.verticalGradient(
            listOf(Color(0xFFB3E5FC), Color(0xFFE1F5FE))
        )
    }

    fun onButtonClick(value: String) {
        when {
            value in "0123456789" -> {
                if (operator.isEmpty()) {
                    firstOperand += value
                } else {
                    secondOperand += value
                }
            }
            value in "+-*/" -> {
                if (firstOperand.isNotEmpty() && secondOperand.isEmpty()) {
                    operator = value
                }
            }
            value == "=" -> {
                if (firstOperand.isNotEmpty() && secondOperand.isNotEmpty()) {
                    val result = calculateResult(
                        firstOperand.toDouble(),
                        operator,
                        secondOperand.toDouble()
                    )
                    displayText = result.toString()
                    firstOperand = result.toString()
                    operator = ""
                    secondOperand = ""
                }
            }
            value == "C" -> {
                firstOperand = ""
                operator = ""
                secondOperand = ""
                displayText = ""
            }
        }
        displayText = "$firstOperand $operator $secondOperand"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Display
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(displayBackground, shape = RoundedCornerShape(8.dp))
                .padding(16.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                text = displayText.ifEmpty { "0" },
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                fontStyle = FontStyle.Italic,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
                textAlign = TextAlign.End,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Buttons
        val buttons = listOf(
            listOf("7", "8", "9", "/"),
            listOf("4", "5", "6", "*"),
            listOf("1", "2", "3", "-"),
            listOf("C", "0", "=", "+")
        )

        buttons.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                row.forEach { buttonText ->
                    CalculatorButton(buttonText) { onButtonClick(it) }
                }
            }
        }
    }
}

@Composable
fun CalculatorButton(value: String, onClick: (String) -> Unit) {
    var isPressed by remember { mutableStateOf(false) }

    val backgroundColor by animateColorAsState(
        if (isPressed) Color(0xFF90CAF9) else Color(0xFFBBDEFB), label = ""
    )

    val elevation by animateFloatAsState(if (isPressed) 4f else 12f, label = "")

    Box(
        modifier = Modifier
            .size(80.dp)
            .padding(8.dp)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = {
                isPressed = true
                onClick(value)  // Correct passing of value
                isPressed = false
            })
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = value,
            fontSize = 24.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
    }
}

fun calculateResult(operand1: Double, operator: String, operand2: Double): Double {
    return when (operator) {
        "+" -> operand1 + operand2
        "-" -> operand1 - operand2
        "*" -> operand1 * operand2
        "/" -> if (operand2 != 0.0) operand1 / operand2 else Double.NaN
        else -> 0.0
    }
}
