package com.uzuu.admin.feature.login

data class LoginUiState(
    val isLoading: Boolean = false,
    val username: String   = ""
)

sealed class LoginUiEvent {
    data class Toast(val message: String) : LoginUiEvent()
    object NavigateToScan : LoginUiEvent()
}