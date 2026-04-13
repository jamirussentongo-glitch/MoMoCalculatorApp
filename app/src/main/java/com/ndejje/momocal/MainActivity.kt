package com.ndejje.momocal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ndejje.momocal.ui.theme.MoMoCalculatorAppTheme
import java.text.NumberFormat
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MoMoCalculatorAppTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        MoMoCalculatorScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun MoMoCalculatorScreen(modifier: Modifier = Modifier) {
    var amountInput by remember { mutableStateOf("") }

    val amount = amountInput.toIntOrNull() ?: 0
    val isError = amountInput.isNotEmpty() && amountInput.toIntOrNull() == null

    val withdrawalFee = calculateWithdrawalFee(amount)
    val totalAmount = amount + withdrawalFee

    val formatter = NumberFormat.getCurrencyInstance(Locale.Builder().setLanguage("en").setRegion("UG").build()).apply {
        maximumFractionDigits = 0
    }

    // Bold Curved Frame/Surface for the calculator
    Surface(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 24.dp, // High elevation for "bold" surface pop
        tonalElevation = 12.dp,
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)) // Bold border
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            // Header with Logo and Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.mine2),
                    contentDescription = "App Logo",
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Text(
                    text = stringResource(id = R.string.app_title),
                    style = MaterialTheme.typography.headlineLarge, // Larger title
                    fontWeight = FontWeight.ExtraBold, // Bolder title
                    color = MaterialTheme.colorScheme.primary
                )
            }

            HoistedAmountInput(
                amount = amountInput,
                onAmountChange = { amountInput = it },
                isError = isError
            )

            Spacer(modifier = Modifier.height(32.dp))

            if (!isError && amount > 0) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = stringResource(id = R.string.fee_label, formatter.format(withdrawalFee)),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider(
                            thickness = 2.dp,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = stringResource(id = R.string.total_label, formatter.format(totalAmount)),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Black, // Extremely bold total
                            color = MaterialTheme.colorScheme.primary
                        )

                        Text(
                            text = "(Amount + Withdrawal Fee)",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun HoistedAmountInput(
    amount: String,
    onAmountChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false
) {
    Column(modifier = modifier) {
        TextField(
            value = amount,
            onValueChange = {
                if (it.all { char -> char.isDigit() }) {
                    onAmountChange(it)
                }
            },
            label = { 
                Text(
                    text = stringResource(id = R.string.enter_amount),
                    fontWeight = FontWeight.Bold 
                ) 
            },
            isError = isError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            shape = RoundedCornerShape(16.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent
            ),
            supportingText = {
                if (isError) {
                    Text(
                        text = stringResource(id = R.string.error_numbers_only),
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        )
    }
}

/**
 * Calculates MoMo withdrawal fee based on typical UGX tiers.
 */
fun calculateWithdrawalFee(amount: Int): Int {
    return when (amount) {
        in 0..500 -> 0
        in 501..2500 -> 330
        in 2501..5000 -> 440
        in 5001..15000 -> 700
        in 15001..30000 -> 880
        in 30001..45000 -> 1210
        in 45001..60000 -> 1500
        in 60001..125000 -> 1925
        in 125001..250000 -> 3575
        in 250001..500000 -> 7000
        in 500001..1000000 -> 12500
        in 1000001..2000000 -> 15000
        in 2000001..4000000 -> 18000
        in 4000001..7000000 -> 20000
        else -> if (amount > 7000000) 20000 else 0
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCalculator() {
    MoMoCalculatorAppTheme {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            MoMoCalculatorScreen()
        }
    }
}
