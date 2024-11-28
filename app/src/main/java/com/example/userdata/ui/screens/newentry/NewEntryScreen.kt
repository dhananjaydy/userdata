package com.example.userdata.ui.screens.newentry

import android.icu.util.Calendar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.userdata.InputFormState
import com.example.userdata.R

@Composable
fun NewEntryScreen(
    formState: InputFormState,
    onNameChange: (String) -> Unit,
    onAgeChange: (String) -> Unit,
    onDobChange: (String) -> Unit,
    onAddressChange: (String) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {

    /* data being sent down the tree and changes made in the state would be transmitted up the tree */

    var showDatePicker by rememberSaveable { mutableStateOf(false) }

    if (showDatePicker) {
        DatePickerModal(
            onDateSelected = { selectedDateMillis ->
                selectedDateMillis?.let { dateMillis ->
                    val dateFormatted = convertMillisToDate(dateMillis)
                    onDobChange(dateFormatted)
                }
            },
            onDismiss = { showDatePicker = false }
        )
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        /* each TextField has their own error text, will be triggered whenever invalid entries are entered */

        OutlinedTextField(
            value = formState.name,
            onValueChange = onNameChange,
            label = { Text(stringResource(R.string.name)) },
            isError = formState.nameError != null,
            modifier = Modifier.fillMaxWidth()
        )
        formState.nameError?.let { Text(
            it,
            color = Color.Red
        ) }

        OutlinedTextField(
            value = formState.age,
            onValueChange = onAgeChange,
            label = { Text(stringResource(R.string.age)) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            isError = formState.ageError != null,
            modifier = Modifier.fillMaxWidth()
        )
        formState.ageError?.let { Text(
            it,
            color = Color.Red
        ) }

        OutlinedTextField(
            value = formState.dob,
            onValueChange = {},
            label = { Text("DOB") },
            trailingIcon = {
                IconButton(onClick = { showDatePicker = true }) {
                    Icon(Icons.AutoMirrored.Default.KeyboardArrowRight, contentDescription = "Pick Date")
                }
            },
            isError = formState.dobError != null,
            modifier = Modifier.fillMaxWidth()
        )
        formState.dobError?.let { Text(
            it,
            color = Color.Red
        ) }

        OutlinedTextField(
            value = formState.address,
            onValueChange = onAddressChange,
            label = { Text(stringResource(R.string.address)) },
            isError = formState.addressError != null,
            modifier = Modifier.fillMaxWidth()
        )
        formState.addressError?.let { Text(
            it,
            color = Color.Red
        ) }


        /* each component has equal priority and will occupy half the width of the screen */
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = onCancel,
                modifier = Modifier.weight(1f)
            ) {
                Text(stringResource(R.string.cancel))
            }

            Button(
                onClick = onSave,
                modifier = Modifier.weight(1f)
            ) {
                Text(stringResource(R.string.save))
            }
        }
    }
}

fun convertMillisToDate(millis: Long): String {
    val calendar = Calendar.getInstance().apply {
        timeInMillis = millis
    }
    return "${calendar.get(Calendar.DAY_OF_MONTH)}/${calendar.get(Calendar.MONTH) + 1}/${calendar.get(Calendar.YEAR)}"
}


/* Potential improvements -
*   - communicating to the user what happened when they performed an action
*   - navigating them to the Records screen when a successful action is performed
*   - have this screen as a bottomsheet for more integrated experience
* */
