package com.cocot3ro.mipastillero.ui.screens.home

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.navigation.compose.rememberNavController
import com.cocot3ro.mipastillero.R
import com.cocot3ro.mipastillero.ui.navigation.ActiveMeds
import com.cocot3ro.mipastillero.ui.navigation.Calendar
import com.cocot3ro.mipastillero.ui.navigation.FavoriteMeds
import com.cocot3ro.mipastillero.ui.navigation.HomeNavGraph
import kotlinx.coroutines.launch

private object HomeScreenKeys {
    const val SELECTED = "selected"
}

private data class NavItem(
    val route: Any,
    val icon: ImageVector,
    val label: String,
    val contentDescription: String?
)

@Composable
fun HomeScreen(
    modifier: Modifier,
    onDiary: () -> Unit,
    onHistory: () -> Unit
) {
    val navController = rememberNavController()

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(text = "Perfíl")
                HorizontalDivider()
                val context = LocalContext.current
                NavigationDrawerItem(
                    icon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.outpatient_med_32dp),
                            contentDescription = null
                        )
                    },
                    label = { Text(text = stringResource(R.string.add_med)) },
                    onClick = {
                        // TODO: show the add med screen/dialog
                        scope.launch {
                            Toast.makeText(context, "Not implemented yet", Toast.LENGTH_LONG)
                                .show()
                            drawerState.close()
                        }
                    },
                    selected = false
                )
                NavigationDrawerItem(
                    icon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.menu_book_32dp),
                            contentDescription = null
                        )
                    },
                    label = { Text(text = stringResource(R.string.diary)) },
                    onClick = {
                        onDiary.invoke()
                        scope.launch {
                            drawerState.close()
                        }
                    },
                    selected = false
                )
                NavigationDrawerItem(
                    icon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.history_32dp),
                            contentDescription = null
                        )
                    },
                    label = { Text(text = stringResource(R.string.history)) },
                    onClick = {
                        onHistory.invoke()
                        scope.launch {
                            drawerState.close()
                        }
                    },
                    selected = false
                )
            }
        }
    ) {

        Scaffold(
            modifier = modifier,
            bottomBar = {
                NavigationBar {
                    var selected by rememberSaveable(key = HomeScreenKeys.SELECTED) {
                        mutableIntStateOf(0)
                    }

                    val items = listOf(
                        NavItem(
                            route = Calendar,
                            icon = ImageVector.vectorResource(id = R.drawable.calendar_month_32dp),
                            label = stringResource(R.string.calendar),
                            contentDescription = null
                        ),

                        NavItem(
                            route = ActiveMeds,
                            icon = ImageVector.vectorResource(id = R.drawable.pill_32dp),
                            label = stringResource(R.string.active_meds),
                            contentDescription = null
                        ),

                        NavItem(
                            route = FavoriteMeds,
                            icon = ImageVector.vectorResource(id = R.drawable.star_off_32dp),
                            label = stringResource(R.string.favorites),
                            contentDescription = null
                        )
                    )

                    items.forEachIndexed { idx, item ->
                        NavBarItem(
                            selected = selected == idx,
                            onclick = {
                                selected = idx
                                navController.navigate(item.route)
                            },
                            icon = item.icon,
                            label = item.label,
                            contentDescription = item.contentDescription
                        )
                    }
                }
            }
        ) { innerPadding ->

            HomeNavGraph(
                modifier = Modifier
                    .consumeWindowInsets(innerPadding)
                    .padding(innerPadding)
                ,
                navController = navController,
                onOpenDrawer = {
                    scope.launch {
                        if (drawerState.isClosed)
                            drawerState.open()
                    }
                }
            )
        }
    }
}

@Composable
private fun RowScope.NavBarItem(
    selected: Boolean,
    onclick: () -> Unit,
    icon: ImageVector,
    label: String,
    contentDescription: String? = null
) {
    NavigationBarItem(
        selected = selected,
        onClick = onclick,
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription
            )
        },
        label = {
            Text(text = label)
        }
    )
}
