package com.example.donation.ui.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissState
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.donation.config.SessionManager
import com.example.donation.entity.Collect
import com.example.donation.controller.CollectController
import com.example.donation.ui.Mode
import com.example.donation.ui.theme.AppTheme
import com.example.donation.ui.theme.Red900

@RequiresApi(Build.VERSION_CODES.O)
class MainActivity : ComponentActivity() {
    private val collectController = CollectController
    private val sessionManager = SessionManager

    private var collects by mutableStateOf(collectController.findCompatibleWithUser())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getCollects()
        setContent { view() }
    }

    override fun onRestart() {
        super.onRestart()
        refresh()
    }

    private fun refresh() {
        getCollects()
        setContent { view() }
    }

    private fun getCollects() = run { collects = collectController.findCompatibleWithUser() }

    private fun logoff() {
        sessionManager.logoff()
        openLoginActivity()
    }

    private fun addUserToDonor(collect: Collect) =
        try {
            collectController.addDonor(collect.id, sessionManager.authenticatedUser)
            refresh()
        } catch (e: Exception) {
            AlertDialog.Builder(this)
                .setMessage(e.message)
                .setNeutralButton("Ok") { _, _ -> refresh() }
                .show()
        }

    private fun openLoginActivity() =
        Intent(this, LoginActivity::class.java)
            .apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK }
            .run(::startActivity)

    private fun openCollectActivityToCreate() =
        Intent(this, CollectActivity::class.java).run(::startActivity)

    private fun openCollectActivityToView(collect: Collect) =
        Intent(this, CollectActivity::class.java)
            .putExtra(CollectActivity.MODE_KEY, Mode.VIEW)
            .putExtra(CollectActivity.COLLECT_KEY, collect)
            .run(::startActivity)

    private fun toEndSwipeItem(collect: Collect) = openCollectActivityToView(collect)

    private fun toStartSwipeItem(collect: Collect) =
        AlertDialog.Builder(this)
            .setMessage("Tem certeza que deseja cadastrar-se como doador dessa coleta?")
            .setPositiveButton("Sim") { _, _ -> addUserToDonor(collect) }
            .setNegativeButton("Não") { _, _ -> refresh() }
            .show()

    @Composable
    private fun logoffButton() = IconButton(
        onClick = ::logoff,
        content = {
            Icon(
                imageVector = Icons.Filled.ExitToApp,
                contentDescription = BUTTON_TEXT_LOGOFF
            )
        }
    )

    @Composable
    private fun topBar() = TopAppBar(
        title = { Text(APP_BAR_TITLE) },
        actions = { logoffButton() }
    )

    @Composable
    private fun floatingActionButton() = FloatingActionButton(
        onClick = ::openCollectActivityToCreate,
        content = {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = BUTTON_TEXT_CREATE_COLLECT
            )
        }
    )

    @Composable
    private fun body() {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            items(
                items = collects,
                itemContent = {
                    collectItem(
                        collect = it,
                        onSwipeToEnd = { toEndSwipeItem(it) },
                        onSwipeToStart = { toStartSwipeItem(it) },
                    )
                }
            )
        }
    }

    @Composable
    private fun container() {
        Scaffold(
            topBar = { topBar() },
            content = { body() },
            floatingActionButton = { floatingActionButton() }
        )
    }

    @Preview(showBackground = true)
    @Composable
    private fun view() = AppTheme(content = { container() })

    companion object {
        private const val APP_BAR_TITLE = "Nome do app aqui"
        private const val BUTTON_TEXT_CREATE_COLLECT = "Criar Coleta"
        private const val BUTTON_TEXT_LOGOFF = "Logoff"
    }
}

@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun collectItem(collect: Collect, onSwipeToEnd: () -> Unit, onSwipeToStart: () -> Unit) {
    @Composable
    fun paddedColumn(content: @Composable ColumnScope.() -> Unit) = Column(
        modifier = Modifier.padding(5.dp),
        content = content
    )

    @Composable
    fun bloodTypeColumn() = paddedColumn {
        Text(
            fontSize = 30.sp,
            text = collect.bloodType
        )
    }

    @Composable
    fun infosColumn() = paddedColumn {
        Text(
            fontSize = 20.sp,
            text = DATE_TEXT.format(collect.date)
        )
        Text(
            fontSize = 20.sp,
            text = DONOR_EXPECTATION_TEXT.format(collect.donorExpectation)
        )
    }

    @Composable
    fun body() = run {
        bloodTypeColumn()
        infosColumn()
    }

    @Composable
    fun backGroundToEnd() =
        Box(
            modifier = Modifier.fillMaxSize().background(Red900).padding(8.dp),
            content = {
                Column(modifier = Modifier.align(Alignment.CenterStart)) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        )

    @Composable
    fun backGroundToStart() =
        Box(
            modifier = Modifier.fillMaxSize().background(Red900).padding(8.dp),
            content = {
                Column(modifier = Modifier.align(Alignment.CenterEnd)) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        )


    @Composable
    fun container() {
        val state = DismissState(
            initialValue = DismissValue.Default,
            confirmStateChange = {
                when (it) {
                    DismissValue.Default -> {}
                    DismissValue.DismissedToEnd -> onSwipeToEnd()
                    DismissValue.DismissedToStart -> onSwipeToStart()
                }
                true
            }
        )
        SwipeToDismiss(
            state = state,
            dismissContent = {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = CONTAINER_MODIFIER,
                    content = { body() },
                )
            },
            background = {
                val direction = state.dismissDirection
                if (direction == DismissDirection.StartToEnd) backGroundToEnd() else backGroundToStart()
            }
        )
    }

    @Composable
    fun view() {
        container()
    }

    view()
}

private const val DATE_TEXT = "Data da Coleta:                      %s"
private const val DONOR_EXPECTATION_TEXT = "Expectativa de doadores:   %s"
private val CONTAINER_MODIFIER = Modifier.fillMaxWidth()
    .height(90.dp)
    .border(width = 0.5.dp, color = Color.LightGray)
    .background(Color.White)
