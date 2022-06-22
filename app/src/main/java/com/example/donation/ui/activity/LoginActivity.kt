package com.example.donation.ui.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.donation.config.SessionManager
import com.example.donation.ui.theme.AppTheme

@RequiresApi(Build.VERSION_CODES.O)
class LoginActivity : ComponentActivity() {
    private val sessionManager = SessionManager

    private var emailValue by mutableStateOf(TextFieldValue())
    private var passwordValue by mutableStateOf(TextFieldValue())

    private var emailError by mutableStateOf(false)
    private var passwordError by mutableStateOf(false)

    private var loginButtonEnabled by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { view() }
    }

    private fun loginClick() {
        try {
            sessionManager.login(emailValue.text, passwordValue.text)
            openMainActivity()
        } catch (e: Exception) {
            AlertDialog.Builder(this)
                .setMessage(e.message)
                .setNeutralButton("Ok") { dialog, _ -> dialog.dismiss() }
                .show()
        }
    }

    private fun openRegisterActivity() = Intent(this, RegisterActivity::class.java).run(::startActivity)

    private fun openMainActivity() = Intent(this, MainActivity::class.java)
        .apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK }
        .run(::startActivity)

    private fun isEmailValid(tfValue: TextFieldValue) = EMAIL_REGEX.matches(tfValue.text)

    private fun isPasswordValid(tfValue: TextFieldValue) = tfValue.text.isNotBlank()

    private fun isLoginButtonEnabled(): Boolean = isEmailValid(emailValue) && isPasswordValid(passwordValue)

    private fun onEmailChange(tfValue: TextFieldValue) {
        emailValue = tfValue
        emailError = !isEmailValid(tfValue)
        defLoginButtonState()
    }

    private fun onPasswordChange(tfValue: TextFieldValue) {
        passwordValue = tfValue
        passwordError = !isPasswordValid(tfValue)
        defLoginButtonState()
    }

    private fun defLoginButtonState() {
        loginButtonEnabled = isLoginButtonEnabled()
    }

    @Composable
    private fun emailField() = OutlinedTextField(
        value = emailValue,
        onValueChange = ::onEmailChange,
        modifier = FIELD_MODIFIER,
        label = { Text(FIELD_LABEL_EMAIL) },
        singleLine = true,
        isError = emailError,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
    )

    @Composable
    private fun passwordField() = OutlinedTextField(
        value = passwordValue,
        onValueChange = ::onPasswordChange,
        modifier = FIELD_MODIFIER,
        label = { Text(FIELD_LABEL_PASSWORD) },
        singleLine = true,
        isError = passwordError,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
    )

    @Composable
    private fun loginButton() = Button(
        modifier = Modifier.fillMaxWidth().padding(vertical = 30.dp),
        onClick = ::loginClick,
        enabled = loginButtonEnabled,
        content = {
            Text(
                modifier = Modifier.padding(vertical = 10.dp),
                text = BUTTON_TEXT_LOGIN
            )
        }
    )

    @Composable
    private fun registerButton() = TextButton(
        onClick = ::openRegisterActivity,
        content = { Text(BUTTON_TEXT_REGISTER) },
    )

    @Composable
    private fun body() = run {
        emailField()
        passwordField()
        loginButton()
        registerButton()
    }

    @Composable
    private fun container() = Column(
        modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(all = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        content = { body() }
    )

    @Preview(showBackground = true)
    @Composable
    private fun view() = AppTheme(content = { container() })

    companion object {
        private val EMAIL_REGEX = Regex(".*@.*\\..*")
        private val FIELD_MODIFIER = Modifier.fillMaxWidth().padding(vertical = 10.dp)
        private const val FIELD_LABEL_EMAIL = "Email"
        private const val FIELD_LABEL_PASSWORD = "Senha"
        private const val BUTTON_TEXT_LOGIN = "Login"
        private const val BUTTON_TEXT_REGISTER = "Ainda não é membro? Cadastre-se agora."
    }
}
