package com.example.userdata

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.userdata.data.local.UserDataEntity
import com.example.userdata.data.local.UserDataItem
import com.example.userdata.data.local.toUserDataItem
import com.example.userdata.data.repository.UserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

/*
    Dispatchers and repo injected here to make swapping and testing easier,
    having these available in hardcoded or manual manner decreases the ease of swapping and testing
*/

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: UserDataRepository,
    @Named("IoDispatcher") private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {

    /*
    * https://skydoves.medium.com/loading-initial-data-in-launchedeffect-vs-viewmodel-f1747c20ce62
    * practise inspired by this article, ensures emissions are only made only when the field is subscribed
    *
    * StateFlow fits the bill in these ways -
    *   needs an initial value, can be used to handle loading before data is loaded
    *   can store last available data, valid case for configuration changes
    * */

    val allUsersData: StateFlow<UiState<List<UserDataItem>>> = repository.getAllNotifications()
        .map { entities ->
            if (entities.isEmpty()) {
                UiState.Empty
            } else {
                UiState.Success(
                    entities
                        .map { it.toUserDataItem() }
                        .sortedByDescending { it.id }
                )
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            UiState.Loading
        )


    private val _formState = MutableStateFlow(InputFormState())
    val formState = _formState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
        initialValue = InputFormState()
    )

    /* valid checks being made and data is being saved after all the checks are passed, else respective errors are highlighted */

    fun saveUserData() {
        viewModelScope.launch {
            val state = _formState.value
            val nameError = validateName(state.name)
            val ageError = validateAge(state.age)
            val dobError = validateDob(state.dob)
            val addressError = validateAddress(state.address)

            if (nameError == null && ageError == null && dobError == null && addressError == null) {

                val userDataEntity = UserDataEntity(
                    name = state.name,
                    age = state.age.toInt(),
                    dob = state.dob,
                    address = state.address
                )
                withContext(ioDispatcher) { repository.addUserData(userDataEntity) }
            } else {

                _formState.update {
                    it.copy(
                        nameError = nameError,
                        ageError = ageError,
                        dobError = dobError,
                        addressError = addressError
                    )
                }
            }
        }
    }

    /* various methods to validate the entered data and save if deemed under restrictions */

    fun updateName(name: String) {
        _formState.update { it.copy(name = name, nameError = validateName(name)) }
    }

    fun updateAge(age: String) {
        _formState.update { it.copy(age = age, ageError = validateAge(age)) }
    }

    fun updateDob(dob: String) {
        _formState.update { it.copy(dob = dob, dobError = validateDob(dob)) }
    }

    fun updateAddress(address: String) {
        _formState.update { it.copy(address = address, addressError = validateAddress(address)) }
    }

    private fun validateName(name: String): String? =
        if (name.isBlank()) "Name cannot be empty" else null

    private fun validateAge(age: String): String? {
        val ageInt = age.toIntOrNull()
        return when {
            ageInt == null -> "Age must be a number"
            ageInt < 0 -> "Age cannot be negative"
            else -> null
        }
    }

    private fun validateDob(dob: String): String? =
        if (dob.isBlank()) "DOB cannot be empty" else null

    private fun validateAddress(address: String): String? =
        if (address.isBlank()) "Address cannot be empty" else null


}

/* input form field and its error being monitored */

@Stable
data class InputFormState(
    val name: String = "",
    val nameError: String? = null,
    val age: String = "",
    val ageError: String? = null,
    val dob: String = "",
    val dobError: String? = null,
    val address: String = "",
    val addressError: String? = null
)


/* handling custom state for more brevity to the person who reads the code */


sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    data class Success<out T>(val data: T) : UiState<T>
    data object Empty : UiState<Nothing>
}

/*
* Potential improvements -
*
*   - more efficient state handling for DB operations being made
*   - sending valid messages to the UI in the form of callback indicating to the user what happened when a certain action was performed by the user
*   - strings shall not be hardcoded
*   - state and classes be in their adequate / designated places
*
* */