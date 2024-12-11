package com.cocot3ro.mipastillero.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.cocot3ro.mipastillero.R
import com.cocot3ro.mipastillero.domain.model.UsuarioItem
import com.cocot3ro.mipastillero.ui.common.CreateUserDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    modifier: Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
    onManageUsers: () -> Unit,
    onUserSelected: () -> Unit
) {
    var showCreateUserDialog by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.users))
                },
                actions = {
                    IconButton(onClick = onManageUsers) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.manage_accounts_32dp),
                            contentDescription = null // TODO: Content description
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showCreateUserDialog = true }) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.person_add_32dp),
                    contentDescription = null // TODO: Content description
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            val users by viewModel.usersFlow.collectAsState(emptyList())
            val defaultUser by viewModel.defaultUserFlow.collectAsState(null)

            users.firstOrNull()?.let {
                viewModel.selectUser(it)
                onUserSelected.invoke()
            }

            if (users.isEmpty()) {
                Text(
                    modifier = Modifier
                        .align(Alignment.Center),
                    text = stringResource(R.string.no_users),
                    textAlign = TextAlign.Center
                )
            } else {
                LazyVerticalGrid(
                    modifier = Modifier
                        .fillMaxSize(),
                    columns = GridCells.FixedSize(125.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    items(users) { user ->
                        UsuarioItem(
                            modifier = Modifier
                                .height(150.dp)
                                .fillMaxWidth()
                                .padding(horizontal = 4.dp, vertical = 8.dp),
                            user = user,
                            defaultUser = defaultUser
                        ) {
                            viewModel.selectUser(user)
                            onUserSelected.invoke()
                        }
                    }
                }
            }
        }

        if (showCreateUserDialog) {
            CreateUserDialog(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(WindowInsets.safeDrawing.asPaddingValues()),
                onDismissRequest = { showCreateUserDialog = false },
                onConfirm = { newUser, isDefault ->
                    viewModel.createUser(newUser, isDefault)
                    showCreateUserDialog = false
                }
            )
        }
    }
}

@Composable
fun UsuarioItem(
    modifier: Modifier,
    user: UsuarioItem,
    defaultUser: Long?,
    onClick: () -> Unit
) {
    Box(modifier = modifier) {
        Card(modifier = Modifier
            .fillMaxSize()
            .clickable { onClick.invoke() }) {
            ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                val (image, text, star) = createRefs()

                if (user.pkUsuario == defaultUser) {
                    Box(
                        modifier = Modifier.constrainAs(star) {
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.star_on_32dp),
                            contentDescription = null, // TODO: Content description
                            tint = Color.Yellow
                        )

                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.star_off_32dp),
                            contentDescription = null // TODO: Content description
                        )
                    }
                }

                Image(
                    modifier = Modifier.constrainAs(image) {
                        top.linkTo(parent.top, margin = 12.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                    imageVector = ImageVector.vectorResource(R.drawable.account_circle_72dp),
                    contentDescription = null // TODO: Content description
                )

                Text(
                    modifier = Modifier.constrainAs(text) {
                        top.linkTo(image.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    },
                    textAlign = TextAlign.Center,
                    text = user.nombre
                )
            }
        }
    }
}
