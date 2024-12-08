package com.cocot3ro.mipastillero.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.cocot3ro.mipastillero.ui.screens.activemeds.ActiveMedsScreen
import com.cocot3ro.mipastillero.ui.screens.calendar.CalendarScreen
import com.cocot3ro.mipastillero.ui.screens.diary.DiaryScreen
import com.cocot3ro.mipastillero.ui.screens.favmeds.FavoriteMedsScreen
import com.cocot3ro.mipastillero.ui.screens.history.HistoryScreen
import com.cocot3ro.mipastillero.ui.screens.login.LoginScreen
import com.cocot3ro.mipastillero.ui.screens.manageusers.ManageUsersScreen
import com.cocot3ro.mipastillero.ui.screens.settings.SettingsScreen
import com.cocot3ro.mipastillero.ui.screens.splash.SplashScreen

@Composable
fun NavigationWrapper() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Splash) {

        composable<Splash> {
            SplashScreen(
                modifier = Modifier.fillMaxSize(),
                onLoginRequired = {
                    navController.popBackStack()
                    navController.navigate(Login)
                },
                onLoginSuccess = {
                    navController.popBackStack()
                    navController.navigate(Main)
                }
            )
        }

        composable<Login> {
            LoginScreen(modifier = Modifier.fillMaxSize())
        }

        navigation<Main>(startDestination = Calendar) {
            composable<Calendar> {
                CalendarScreen(modifier = Modifier.fillMaxSize())
            }

            composable<ActiveMeds> {
                ActiveMedsScreen(modifier = Modifier.fillMaxSize())
            }

            composable<FavoriteMeds> {
                FavoriteMedsScreen(modifier = Modifier.fillMaxSize())
            }
        }

        composable<ManageUsers> {
            ManageUsersScreen(modifier = Modifier.fillMaxSize())
        }

        composable<History> {
            HistoryScreen(modifier = Modifier.fillMaxSize())
        }

        composable<Settings> {
            SettingsScreen(modifier = Modifier.fillMaxSize())
        }

        composable<Diary> {
            DiaryScreen(modifier = Modifier.fillMaxSize())
        }

    }
}
