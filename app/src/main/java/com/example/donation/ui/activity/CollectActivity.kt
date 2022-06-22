package com.example.donation.ui.activity

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
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
import com.example.donation.entity.Collect
import com.example.donation.extetion.MaskVisualTransformation
import com.example.donation.extetion.toLocalDate
import com.example.donation.extetion.toSingleString
import com.example.donation.controller.CollectController
import com.example.donation.ui.Mode
import com.example.donation.ui.theme.AppTheme
import java.util.UUID

@RequiresApi(Build.VERSION_CODES.O)
class CollectActivity : ComponentActivity() {
    private val collectController = CollectController
    private val sessionManager = SessionManager

    private var id by mutableStateOf<UUID?>(null)

    private var mode by mutableStateOf(Mode.CREATE)
    private var enable by mutableStateOf(true)

    private var bloodTypeValue by mutableStateOf(TextFieldValue())
    private var dateValue by mutableStateOf(TextFieldValue())
    private var donorExpectationValue by mutableStateOf(TextFieldValue())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { view() }

        intent.extras?.get(MODE_KEY)?.let { this.mode = it as Mode }

        if (mode == Mode.VIEW) {
            intent.extras?.get(COLLECT_KEY)?.let {
                it as Collect
                id = it.id
                bloodTypeValue = TextFieldValue(it.bloodType)
                dateValue = TextFieldValue(it.date.toSingleString())
                donorExpectationValue = TextFieldValue(it.donorExpectation.toString())

                if (it.requester?.id != sessionManager.authenticatedUser.id) {
                    enable = false
                }
            }
        }
    }

    private fun confirmClick() {
        val collect = Collect(
            bloodType = bloodTypeValue.text,
            date = dateValue.text.toLocalDate(),
            donorExpectation = donorExpectationValue.text.toInt(),
        )

        when (mode) {
            Mode.CREATE -> collectController.create(collect)
            Mode.VIEW -> collectController.update(id!!, collect)
        }
        finish()
    }

    private fun deleteClick() {
        CollectController.delete(id!!)
        finish()
    }

    @Composable
    private fun confirmButton() = IconButton(
        onClick = ::confirmClick,
        enabled = enable,
        content = {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = BUTTON_TEXT_CONFIRM
            )
        }
    )

    @Composable
    private fun deleteButton() = IconButton(
        onClick = ::deleteClick,
        enabled = enable,
        content = {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = BUTTON_TEXT_DELETE
            )
        }
    )

    @Composable
    private fun topBar() = TopAppBar(
        title = { Text(APP_BAR_TITLE) },
        actions = {
            id?.let { deleteButton() }
            confirmButton()
        }
    )

    @Composable
    private fun bloodTypeField() = OutlinedTextField(
        value = bloodTypeValue,
        onValueChange = { bloodTypeValue = it },
        modifier = FIELD_MODIFIER,
        label = { Text(FIELD_LABEL_BLOOD_TYPE) },
        singleLine = true,
        enabled = enable,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
    )

    @Composable
    private fun dateField() = OutlinedTextField(
        value = dateValue,
        onValueChange = { dateValue = it },
        modifier = FIELD_MODIFIER,
        label = { Text(FIELD_LABEL_DATE) },
        singleLine = true,
        enabled = enable,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        visualTransformation = MaskVisualTransformation("##/##/####")
    )

    @Composable
    private fun donorExpectationField() = OutlinedTextField(
        value = donorExpectationValue,
        onValueChange = { donorExpectationValue = it },
        modifier = FIELD_MODIFIER,
        label = { Text(FIELD_LABEL_DONOR_EXPECTATION) },
        singleLine = true,
        enabled = enable,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
    )

    @Composable
    private fun body() = run {
        bloodTypeField()
        dateField()
        donorExpectationField()
    }

    @Composable
    private fun container() = Scaffold(
        topBar = { topBar() },
        content = {
            Column(
                modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(all = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                content = { body() }
            )
        }
    )

    @Preview(showBackground = true)
    @Composable
    private fun view() = AppTheme(content = { container() })

    companion object {
        private val FIELD_MODIFIER = Modifier.fillMaxWidth().padding(vertical = 10.dp)
        private const val APP_BAR_TITLE = "Cadastrar Coleta"
        private const val BUTTON_TEXT_CONFIRM = "Confirmar"
        private const val BUTTON_TEXT_DELETE = "Deletar"
        private const val FIELD_LABEL_BLOOD_TYPE = "Tipo Sanguineo"
        private const val FIELD_LABEL_DATE = "Data"
        private const val FIELD_LABEL_DONOR_EXPECTATION = "Expectativa de Doadores"

        const val MODE_KEY = "mode"
        const val COLLECT_KEY = "collect"
    }
}
