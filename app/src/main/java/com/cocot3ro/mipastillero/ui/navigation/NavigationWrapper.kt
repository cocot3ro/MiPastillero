package com.cocot3ro.mipastillero.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.cocot3ro.mipastillero.ui.screens.splash.SplashScreen

@Composable
fun NavigationWrapper() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Splash(null)) {

        composable<Splash>(
            deepLinks = listOf(
                navDeepLink<Splash>(basePath = "https://cocot3ro.github.io/MiPastillero/")
            )
        ) { backStackEntry ->
            val args: Splash = backStackEntry.toRoute()
            SplashScreen(
                modifier = Modifier.fillMaxSize(),
                incomingUser = args.user,
                onFirstTime = {
                    navController.popBackStack()
                    navController.navigate(FirstTime)
                },
                onLoginRequired = {
                    navController.popBackStack()
                    navController.navigate(Login)
                },
                onLoginSuccess = {
                    navController.popBackStack()
                    navController.navigate(Login)
                    navController.navigate(Home)
                }
            )
        }

        composable<Login> {
//            LoginScreen(
//                modifier = Modifier.fillMaxSize(),
//                onNavigateToManageUsers = { navController.navigate(ManageUsers) },
//                onUserSelected = { navController.navigate(Home) }
//            )
        }

        composable<Home> {
//            HomeScreen(
//                modifier = Modifier.fillMaxSize(),
//                onLogout = { navController.popBackStack(Login, false) },
//                onNavigateToSettings = { navController.navigate(Settings) },
//                onNavigateToDiary = { navController.navigate(Diary) },
//                onNavigateToHistory = { navController.navigate(History) },
//                onNavigateToMedInfo = { medId -> navController.navigate(MedInfo(medId = medId)) }
//            )
        }

        composable<ManageUsers> {
//            ManageUsersScreen(
//                modifier = Modifier.fillMaxSize(),
//                onNavigateBack = { navController.popBackStack() },
//                onCurrentUserDeleted = { navController.popBackStack(Login, false) }
//            )
        }

        composable<History> {
//            HistoryScreen(modifier = Modifier.fillMaxSize())
        }

        composable<Settings> {
//            SettingsScreen(
//                modifier = Modifier.fillMaxSize(),
//                onNavigateBack = { navController.popBackStack() },
//                onNavigateToManageUsers = { navController.navigate(ManageUsers) },
//                onDataDeleted = { navController.popBackStack(Login, false) }
//            )
        }

        composable<Diary> {
//            DiaryScreen(modifier = Modifier.fillMaxSize())
        }

        composable<MedInfo> { backStackEntry ->
            val args: MedInfo = backStackEntry.toRoute()

//            MedInfoScreen(
//                modifier = Modifier.fillMaxSize(),
//                medId = medInfo.medId
//            )
        }

        composable<FirstTime> {

            // If the device is running Android 13 or higher,
            // request the POST_NOTIFICATIONS permission if it's the first time.
            // Won't ask again if the user denies it.
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//                val notificationPermissionState = rememberPermissionState(
//                    permission = android.Manifest.permission.POST_NOTIFICATIONS
//                )
//
//                if (!notificationPermissionState.status.isGranted
//                    && !notificationPermissionState.status.shouldShowRationale
//                ) {
//                    SideEffect {
//                        notificationPermissionState.launchPermissionRequest()
//                    }
//                }
//            }

//            FirstTimeScreen(
//                modifier = Modifier.fillMaxSize(),
//                onSetUpCompleted = {
//                    navController.popBackStack()
//                    navController.navigate(Login)
//                    navController.navigate(Home)
//                }
//            )
        }

    }
}
