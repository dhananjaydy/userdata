package com.example.userdata.ui.screens.records

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.userdata.UiState
import com.example.userdata.data.local.UserDataItem

@Composable
fun UserDataScreen(
    allUsersData: UiState<List<UserDataItem>>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        UserDataList(
            state = allUsersData,
            modifier = Modifier.weight(1f)
        )

        FloatingActionButton(
            onClick = onClick,
            modifier = Modifier
                .align(Alignment.End)
                .padding(16.dp),
            elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Floating action button."
            )
        }
    }
}

/* Potential improvement -
*   - better styling of components
*   - search bar to enable user to search the records
*   - FAB to only take required space and not the current one
* */



