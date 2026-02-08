package com.example.alcometer

import android.R.attr.text
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.alcometer.ui.theme.AlcometerTheme
import androidx.compose.ui.graphics.Color as color


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AlcometerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                   Alcometer(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Alcometer(modifier: Modifier = Modifier) {

    // state variables for inputs and result
    var weight by remember { mutableStateOf("")}
    //var gender by remember { mutableStateOf("Female")}
    val genderOptions = listOf("Female", "Male")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(genderOptions[0]) }
    var portionQuantity by remember { mutableStateOf("")}
    var hoursConsumed by remember { mutableStateOf("")}

    /*val litres = portionQuantity.toDouble().times(0.33)
    val grams = litres?.times(8)?.times(4.5)
    val burning =  (weight.toDouble()) / 10
    val gramsLeftToBurn = grams?.minus((burning * (hoursConsumed.toDouble())))
    var result by remember { mutableDoubleStateOf(0.0) }*/

    /*if(gender == "Female") {
        gramsLeftToBurn / (weight.toDouble() * 0.6)
        } else {
            gramsLeftToBurn / (weight.toDouble() * 0.7)
        }
    )*/
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        // horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)

    )

    {
        // heading
        Text(
            text = "Alcometer",
            modifier = Modifier
                .padding(top = 32.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        // input field for weight
        OutlinedTextField(
            value = weight,
            onValueChange = { weight = it },
            label = { Text("Your weight") },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor =MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                cursorColor = MaterialTheme.colorScheme.primary,
                focusedLabelColor = MaterialTheme.colorScheme.primary
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        // error handling for weight input
        val weightValue = weight.toIntOrNull()
        if (weightValue != null && weightValue < 1 ) {
            Text(
                text = "Please enter a valid weight",
                color = MaterialTheme.colorScheme.error
            )
        }
        // radio button for gender selection, both selections are on own row
        // loop over gender options to render each button with correct label

           genderOptions.forEach {
               option -> Row(
                   modifier = Modifier.padding(4.dp),
                   verticalAlignment = Alignment.CenterVertically,
               ) {
                   RadioButton(
                       selected = (option == selectedOption), // set selected state based on option
                       onClick = { onOptionSelected(option)}  // whichever option from gender list is chosen
                   )
               Text (
                   text = option // label according to selection
               )
           }


       }





    }


}

@Preview(showBackground = true)
@Composable
fun AlcometerPreview() {
    AlcometerTheme {
        Alcometer()
    }
}