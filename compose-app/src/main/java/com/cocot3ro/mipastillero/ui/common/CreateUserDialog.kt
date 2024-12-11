package com.cocot3ro.mipastillero.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.cocot3ro.mipastillero.R
import com.cocot3ro.mipastillero.domain.model.UsuarioItem

private object CreateUserDialogKeys {
    const val USER_NAME = "USER_NAME"
    const val IS_DEFAULT = "IS_DEFAULT"
}

@Composable
fun CreateUserDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onConfirm: (newUser: UsuarioItem, isDefault: Boolean) -> Unit
) {

    var userName by rememberSaveable(key = CreateUserDialogKeys.USER_NAME) { mutableStateOf("") }
    var isDefault by rememberSaveable(key = CreateUserDialogKeys.IS_DEFAULT) { mutableStateOf(false) }

    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        title = { Text(text = stringResource(R.string.create_user)) },
        text = {
            Box(
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 8.dp)
            ) {
                Card(modifier = Modifier.fillMaxSize()) {
                    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                        val (image, text, star) = createRefs()

                        IconButton(
                            modifier = Modifier.constrainAs(star) {
                                top.linkTo(parent.top)
                                end.linkTo(parent.end)
                            },
                            onClick = {
                                isDefault = !isDefault
                            }
                        ) {
                            if (isDefault) {
                                Image(
                                    painter = painterResource(id = R.drawable.star_on_32dp),
                                    contentDescription = null, // TODO: Content description
                                )
                            }

                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.star_off_32dp),
                                contentDescription = null // TODO: Content description
                            )
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

                        TextField(
                            modifier = Modifier
                                .padding(horizontal = 32.dp)
                                .constrainAs(text) {
                                    top.linkTo(image.bottom)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                    bottom.linkTo(parent.bottom)
                                },
                            value = userName,
                            onValueChange = {
                                userName = it.substring(0, it.length.coerceAtMost(30))
                            },
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onConfirm(
                    UsuarioItem(nombre = userName),
                    isDefault
                )
            }) {
                Text(text = stringResource(R.string.accept))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = stringResource(R.string.cancel))
            }
        }
    )
}
