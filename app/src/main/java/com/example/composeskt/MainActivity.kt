package com.example.composeskt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.example.composeskt.data.RetrofitInstance
import com.example.composeskt.repository.CurrencyRepositoryImpl
import com.example.composeskt.viewmodel.CurrencyViewModel
import com.example.composeskt.viewmodel.CurrencyViewModelFactory

class MainActivity : ComponentActivity() {
    private val apiCall by lazy { RetrofitInstance().api }
    private val repository by lazy { CurrencyRepositoryImpl(apiCall) }
    private lateinit var viewModel: CurrencyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, CurrencyViewModelFactory(repository))[CurrencyViewModel::class.java]
        enableEdgeToEdge()
        setContent {
            CurrencyConverterApp(viewModel)
        }
    }

    @Composable
    fun CurrencyConverterApp(viewModel: CurrencyViewModel) {
        // Observing the API result
        val currencyData by viewModel.apiResult.observeAsState()
        var amount by remember { mutableStateOf("0") }
        var convertedAmount by remember { mutableStateOf("0") }
        var fromCurrency by remember { mutableStateOf("inr") }
        var toCurrency by remember { mutableStateOf("usd") }
        val currencyOptionsList = currencyData?.inr?.keys?.toList() ?: emptyList()

        // Fetch currency data when `fromCurrency` changes
        LaunchedEffect(fromCurrency) {
            viewModel.getCurrencyData(fromCurrency)
        }

        fun swapCurrencies() {
            val temp = fromCurrency
            fromCurrency = toCurrency
            toCurrency = temp
        }

        // Convert only if currency data is available
        fun convert() {
            val amountValue = amount.toDoubleOrNull() ?: 0.0
            val rate = currencyData?.inr?.get(toCurrency)
            if (rate != null) {
                convertedAmount = (amountValue * rate).toString()
            } else {
                convertedAmount = "Conversion Error"
            }
        }

        // Trigger the API call and reset conversion after the currency is changed
        LaunchedEffect(toCurrency) {
            convert()
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(0.9f),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CurrencyInput(
                        label = "From",
                        amount = amount,
                        onAmountChange = { amount = it },
                        currencyOptions = currencyOptionsList,
                        selectedCurrency = fromCurrency,
                        onCurrencyChange = { fromCurrency = it }
                    )

                    Button(
                        onClick = {
                            swapCurrencies()
                            // Re-trigger conversion after swapping currencies
                            convert()
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text("Swap")
                    }

                    CurrencyInput(
                        label = "To",
                        amount = convertedAmount,
                        onAmountChange = {},
                        currencyOptions = currencyOptionsList,
                        selectedCurrency = toCurrency,
                        onCurrencyChange = { toCurrency = it },
                        amountDisable = true
                    )

                    Button(
                        onClick = { convert() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
                    ) {
                        Text(
                            text = "Convert ${fromCurrency.uppercase()} to ${toCurrency.uppercase()}",
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun CurrencyInput(
        label: String,
        amount: String,
        onAmountChange: (String) -> Unit,
        currencyOptions: List<String>,
        selectedCurrency: String,
        onCurrencyChange: (String) -> Unit,
        amountDisable: Boolean = false
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                OutlinedTextField(
                    value = amount,
                    onValueChange = onAmountChange,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    enabled = !amountDisable
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "Currency Type",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                DropdownMenuExample(
                    selectedCurrency = selectedCurrency,
                    currencyOptions = currencyOptions,
                    onCurrencyChange = onCurrencyChange
                )
            }
        }
    }

    @Composable
    fun DropdownMenuExample(
        selectedCurrency: String,
        currencyOptions: List<String>,
        onCurrencyChange: (String) -> Unit
    ) {
        var expanded by remember { mutableStateOf(false) }

        Box {
            OutlinedButton(onClick = { expanded = true }) {
                Text(text = selectedCurrency.uppercase())
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                currencyOptions.forEach { currency ->
                    DropdownMenuItem(
                        text = { Text(currency.uppercase()) },
                        onClick = {
                            onCurrencyChange(currency)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
