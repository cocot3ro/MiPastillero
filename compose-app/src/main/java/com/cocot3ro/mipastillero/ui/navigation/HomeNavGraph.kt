package com.cocot3ro.mipastillero.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.cocot3ro.mipastillero.ui.screens.activemeds.ActiveMedsScreen
import com.cocot3ro.mipastillero.ui.screens.calendar.CalendarScreen
import com.cocot3ro.mipastillero.ui.screens.favmeds.FavoriteMedsScreen

@Composable
fun HomeNavGraph(
    navController: NavHostController,
    onOpenDrawer: () -> Unit
) {

    NavHost(
        navController = navController,
        startDestination = Calendar
    ) {
        composable<Calendar> {
            CalendarScreen(modifier = Modifier.fillMaxSize(), onOpenDrawer = onOpenDrawer)
        }

        composable<ActiveMeds> {
            ActiveMedsScreen(modifier = Modifier.fillMaxSize(), onOpenDrawer = onOpenDrawer)
        }

        composable<FavoriteMeds> {
            FavoriteMedsScreen(modifier = Modifier.fillMaxSize(), onOpenDrawer = onOpenDrawer)
        }
    }
}
