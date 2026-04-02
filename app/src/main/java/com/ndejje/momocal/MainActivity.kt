package com.ndejje.momocal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MoMoCalculatorScreen(modifier = Modifier.padding(innerPadding))
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

    // Formatter for UGX currency
    val formatter = NumberFormat.getCurrencyInstance(Locale("en", "UG")).apply {
        maximumFractionDigits = 0
    }

    Column(modifier = modifier.padding(16.dp)) {
        Text(
            text = stringResource(id = R.string.app_title),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        HoistedAmountInput(
            amount = amountInput,
            onAmountChange = { amountInput = it },
            isError = isError
        )

        Spacer(modifier = Modifier.height(32.dp))

        if (!isError && amount > 0) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(id = R.string.fee_label, formatter.format(withdrawalFee)),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = stringResource(id = R.string.total_label, formatter.format(totalAmount)),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Text(
                    text = "(Amount + Withdrawal Fee)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}

@Composable
fun HoistedAmountInput(
    amount: String,
    onAmountChange: (String) -> Unit,
    isError: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        TextField(
            value = amount,
            onValueChange = { 
                // Only allow digits to keep it simple
                if (it.all { char -> char.isDigit() }) {
                    onAmountChange(it)
                }
            },
            label = { Text(stringResource(id = R.string.enter_amount)) },
            isError = isError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            supportingText = {
                if (isError) {
                    Text(
                        text = stringResource(id = R.string.error_numbers_only),
                        color = MaterialTheme.colorScheme.error,
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
        MoMoCalculatorScreen()
    }
}
