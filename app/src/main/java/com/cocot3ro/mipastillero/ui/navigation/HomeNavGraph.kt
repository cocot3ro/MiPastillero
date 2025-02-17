package com.cocot3ro.mipastillero.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun HomeNavGraph(
    modifier: Modifier,
    navController: NavHostController,
    onOpenDrawer: () -> Unit,
    onNavigateToMedInfo: (Long) -> Unit
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Calendar
    ) {
        composable<Calendar> {
//            CalendarScreen(
//                modifier = Modifier.fillMaxSize(),
//                onOpenDrawer = onOpenDrawer
//            )
        }

        composable<ActiveMeds> {
//            ActiveMedsScreen(
//                modifier = Modifier.fillMaxSize(),
//                onOpenDrawer = onOpenDrawer,
//                onNavigateToMedInfo = onNavigateToMedInfo
//            )
        }

        composable<FavoriteMeds> {
//            FavoriteMedsScreen(
//                modifier = Modifier.fillMaxSize(),
//                onOpenDrawer = onOpenDrawer,
//                onNavigateToMedInfo = onNavigateToMedInfo
//            )
        }
    }
}
