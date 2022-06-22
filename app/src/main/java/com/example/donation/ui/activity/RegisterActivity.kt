package com.example.donation.ui.activity

import android.app.AlertDialog
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.donation.entity.User
import com.example.donation.extetion.MaskVisualTransformation
import com.example.donation.extetion.toLocalDate
import com.example.donation.controller.UserController
import com.example.donation.ui.theme.AppTheme

@RequiresApi(Build.VERSION_CODES.O)
class RegisterActivity : ComponentActivity() {
    private val userService = UserController

    private var nameValue by mutableStateOf(TextFieldValue())
    private var emailValue by mutableStateOf(TextFieldValue())
    private var passwordValue by mutableStateOf(TextFieldValue())
    private var phoneValue by mutableStateOf(TextFieldValue())
    private var bloodTypeValue by mutableStateOf(TextFieldValue())
    private var lastDonationValue by mutableStateOf(TextFieldValue())

    private var nameError by mutableStateOf(false)
    private var emailError by mutableStateOf(false)
    private var passwordError by mutableStateOf(false)
    private var phoneError by mutableStateOf(false)
    private var bloodTypeError by mutableStateOf(false)
    private var lastDonationError by mutableStateOf(false)

    private var registerButtonEnabled by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { view() }
    }

    private fun loginClick() = finish()

    private fun registerClick() =
        try {
            val user = User(
                name = nameValue.text,
                email = emailValue.text,
                password = passwordValue.text,
                phone = phoneValue.text,
                bloodType = bloodTypeValue.text,
                lastDonation = lastDonationValue.text.toLocalDate()
            )
            userService.register(user)
            finish()
        } catch (e: Exception) {
            AlertDialog.Builder(this)
                .setMessage(e.message)
                .setNeutralButton("Ok") { dialog, _ -> dialog.dismiss() }
                .show()
        }

    private fun isNameValid(tfValue: TextFieldValue) = tfValue.text.isNotBlank()

    private fun isEmailValid(tfValue: TextFieldValue) = EMAIL_REGEX.matches(tfValue.text)

    private fun isPasswordValid(tfValue: TextFieldValue) = tfValue.text.isNotBlank()

    private fun isPhoneValid(tfValue: TextFieldValue) = tfValue.text.isNotBlank()

    private fun isBloodTypeValid(tfValue: TextFieldValue) = tfValue.text.isNotBlank()

    private fun isLastDonationValid(tfValue: TextFieldValue) = tfValue.text.isNotBlank()

    private fun isRegisterButtonEnabled(): Boolean =
        isNameValid(nameValue)
                && isEmailValid(emailValue)
                && isPasswordValid(passwordValue)
                && isPhoneValid(phoneValue)
                && isBloodTypeValid(bloodTypeValue)
                && isLastDonationValid(lastDonationValue)

    private fun onNameChange(tfValue: TextFieldValue) {
        nameValue = tfValue
        nameError = !isNameValid(tfValue)
        defRegisterButtonState()
    }

    private fun onEmailChange(tfValue: TextFieldValue) {
        emailValue = tfValue
        emailError = !isEmailValid(tfValue)
        defRegisterButtonState()
    }

    private fun onPasswordChange(tfValue: TextFieldValue) {
        passwordValue = tfValue
        passwordError = !isPasswordValid(tfValue)
        defRegisterButtonState()
    }

    private fun onPhoneChange(tfValue: TextFieldValue) {
        phoneValue = tfValue
        phoneError = !isPhoneValid(tfValue)
        defRegisterButtonState()
    }

    private fun onBloodTypeChange(tfValue: TextFieldValue) {
        bloodTypeValue = tfValue
        bloodTypeError = !isBloodTypeValid(tfValue)
        defRegisterButtonState()
    }

    private fun onLastDonationChange(tfValue: TextFieldValue) {
        lastDonationValue = tfValue
        lastDonationError = !isLastDonationValid(tfValue)
        defRegisterButtonState()
    }

    private fun defRegisterButtonState() {
        registerButtonEnabled = isRegisterButtonEnabled()
    }

    @Composable
    private fun nameField() = OutlinedTextField(
        value = nameValue,
        onValueChange = ::onNameChange,
        modifier = FIELD_MODIFIER,
        label = { Text(FIELD_LABEL_NAME) },
        singleLine = true,
        isError = nameError,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
    )

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
        visualTransformation = PasswordVisualTransformation(),
    )

    @Composable
    private fun phoneField() = OutlinedTextField(
        value = phoneValue,
        onValueChange = ::onPhoneChange,
        modifier = FIELD_MODIFIER,
        label = { Text(FIELD_LABEL_PHONE) },
        singleLine = true,
        isError = phoneError,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        visualTransformation = MaskVisualTransformation(
            if (phoneValue.text.length <= 10)
                "(##) ####-####"
            else
                "(##) #####-####"
        )
    )

    @Composable
    private fun bloodTypeField() = OutlinedTextField(
        value = bloodTypeValue,
        onValueChange = ::onBloodTypeChange,
        modifier = FIELD_MODIFIER,
        label = { Text(FIELD_LABEL_BLOOD_TYPE) },
        singleLine = true,
        isError = bloodTypeError,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
    )

    @Composable
    private fun lastDonationField() = OutlinedTextField(
        value = lastDonationValue,
        onValueChange = ::onLastDonationChange,
        modifier = FIELD_MODIFIER,
        label = { Text(FIELD_LABEL_LAST_DONATION) },
        singleLine = true,
        isError = lastDonationError,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        visualTransformation = MaskVisualTransformation("##/##/####")
    )

    @Composable
    private fun registerButton() = Button(
        modifier = Modifier.fillMaxWidth().padding(vertical = 30.dp),
        onClick = ::registerClick,
        enabled = registerButtonEnabled,
        content = {
            Text(
                modifier = Modifier.padding(vertical = 10.dp),
                text = BUTTON_TEXT_REGISTER
            )
        }
    )

    @Composable
    private fun loginButton() = TextButton(
        onClick = ::loginClick,
        content = { Text(BUTTON_TEXT_LOGIN) },
    )

    @Composable
    private fun body() = run {
        nameField()
        emailField()
        passwordField()
        phoneField()
        bloodTypeField()
        lastDonationField()
        registerButton()
        loginButton()
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
        private const val FIELD_LABEL_NAME = "Nome"
        private const val FIELD_LABEL_EMAIL = "Email"
        private const val FIELD_LABEL_PASSWORD = "Senha"
        private const val FIELD_LABEL_PHONE = "Telefone"
        private const val FIELD_LABEL_BLOOD_TYPE = "Tipo Sanguineo"
        private const val FIELD_LABEL_LAST_DONATION = "Ultima Doação"

        private const val BUTTON_TEXT_REGISTER = "CADASTRAR-SE"
        private const val BUTTON_TEXT_LOGIN = "Já é membro? Faça login agora."
    }
}
