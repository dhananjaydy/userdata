package com.example.userdata.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.userdata.MainViewModel
import com.example.userdata.R
import com.example.userdata.ui.screens.newentry.NewEntryScreen
import com.example.userdata.ui.screens.records.UserDataScreen

@Composable
fun UserDataApp(
    mainViewModel: MainViewModel = hiltViewModel(),
    navController: NavHostController = rememberNavController()
) {

    /* making use of navigation APIs to handle user actions */

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = UserDataScreen.valueOf(
        backStackEntry?.destination?.route ?: UserDataScreen.Records.name
    )

    Scaffold(
        topBar = {
            UserDataAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        }
    ) { innerPadding ->

        /* collectAsStateWithLifecycle helps in efficient collection as the data is only collected when it is
        *  active and not when in background, thus only doing the required and not wasting resources when not required
        * */

        val formState by mainViewModel.formState.collectAsStateWithLifecycle()
        val allUsersData by mainViewModel.allUsersData.collectAsStateWithLifecycle()

       /*   UDF pattern (state goes down, events flow up) is followed to minimise sharing state down the composables,
            helping achieve more stateless composables and have a central system over the actions being performed. */

        NavHost(
            navController = navController,
            startDestination = UserDataScreen.Records.name,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
        ) {

            /* required data passed down the tree */

            composable(route = UserDataScreen.Records.name) {
                UserDataScreen(
                    allUsersData = allUsersData,
                    onClick = { navController.navigate(UserDataScreen.NewEntryScreen.name) }
                )
            }

            /* callbacks being executed in the parent composable making other composables reusable */

            composable(route = UserDataScreen.NewEntryScreen.name) {
                NewEntryScreen(formState = formState,
                    onNameChange = { mainViewModel.updateName(it) },
                    onAgeChange = { mainViewModel.updateAge(it) },
                    onDobChange = { mainViewModel.updateDob(it) },
                    onAddressChange = { mainViewModel.updateAddress(it) },
                    onCancel = { navController.navigateUp() },
                    onSave = { mainViewModel.saveUserData() }
                )
            }
        }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDataAppBar(
    currentScreen: UserDataScreen,
   canNavigateBack: Boolean,
   navigateUp: () -> Unit,
   modifier: Modifier = Modifier
) {

    TopAppBar(
        title = { Text(stringResource(currentScreen.title)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {

            /* icon only shown when there is a valid state in the backstack */

            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}

enum class UserDataScreen(@StringRes val title: Int) {
    NewEntryScreen(R.string.new_entry),
    Records(R.string.records)
}

