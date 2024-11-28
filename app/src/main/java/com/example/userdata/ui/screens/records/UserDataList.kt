package com.example.userdata.ui.screens.records

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.userdata.R
import com.example.userdata.UiState
import com.example.userdata.data.local.UserDataItem

@Composable
fun UserDataList(
    state: UiState<List<UserDataItem>>,
    modifier: Modifier = Modifier
) {

    /* conditional rendering basis the state that is received by the composable */

    when (state) {
        UiState.Empty -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = stringResource(R.string.no_user_records_available_yet),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }
        }
        UiState.Loading -> {
            FullScreenLoader()
        }
        is UiState.Success -> {
            LazyColumn(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {

                /* key to make the items recognizable in case reconfiguration needs to be done and state would be maintained in that manner */

                items(items = state.data, key = { it.id }) { userDataItem ->
                    UserDataRecord(
                        userDataItem = userDataItem,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun FullScreenLoader(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(80.dp),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    }
}
