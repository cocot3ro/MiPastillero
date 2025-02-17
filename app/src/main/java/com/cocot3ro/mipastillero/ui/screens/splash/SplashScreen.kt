package com.cocot3ro.mipastillero.ui.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.cocot3ro.mipastillero.R
import com.cocot3ro.mipastillero.domain.model.UserItem
import org.koin.androidx.compose.koinViewModel

@Composable
fun SplashScreen(
    modifier: Modifier,
    viewModel: SplashViewModel = koinViewModel(),
    incomingUser: Long?,
    onFirstTime: () -> Unit,
    onLoginRequired: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    viewModel.fetch()

    Scaffold(modifier = modifier) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = null // TODO: Content description
            )
        }
    }

    val firstTime = viewModel.firstTime.collectAsState().value
    val defaultUser = viewModel.defaultUser.collectAsState().value
    val users = viewModel.usersFlow.collectAsState().value

    if (firstTime !is SplashUiState.Success<*> || defaultUser !is SplashUiState.Success<*> || users !is SplashUiState.Success<*>) return

    LaunchedEffect(Unit) {
        when {
            firstTime.data as Boolean -> {
                onFirstTime()
            }

            incomingUser != null -> {
                // Autologin with the user.
                // True when coming from the deep link from the notification
                if (viewModel.login(incomingUser)) onLoginSuccess()
                else onLoginRequired()
            }

            // If there is a default user, login with it.
            defaultUser.data as? Long != null -> {
                val userId: Long = defaultUser.data

                if (viewModel.login(userId)) onLoginSuccess()
                else onLoginRequired()
            }

            // If there is only one user, login with it.
            (users.data as List<*>).size == 1 -> {
                val userId: Long = (users.data).map { (it as UserItem).userId }.first()

                if (viewModel.login(userId)) onLoginSuccess()
                else onLoginRequired()
            }

            else -> {
                onLoginRequired()
            }
        }
    }
}
