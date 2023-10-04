package com.beank.workFlowy.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.beank.workFlowy.screen.WorkFlowyState
import com.beank.workFlowy.screen.analysis.AnalysisScreen
import com.beank.workFlowy.screen.home.HomeScreen
import com.beank.workFlowy.screen.login.LoginScreen
import com.beank.workFlowy.screen.schedule.ScheduleScreen
import com.beank.workFlowy.screen.setting.SettingScreen
import com.beank.workFlowy.screen.sign_up.SignUpScreen
import com.beank.workFlowy.screen.tag.TagScreen
import com.beank.workFlowy.ui.theme.black

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationGraph(
    appState : WorkFlowyState
){
    NavHost(
        navController = appState.navController,
        startDestination = NavigationItem.LOGIN.route
    ){
        composable(route = NavigationItem.LOGIN.route){
            LoginScreen(
                openPopUpScreen = appState::navigatePopUp,
                openScreen = appState::navigate,
                snackbarHostState = appState.snackbarHostState
            )
        }

        composable(
            route = NavigationItem.SIGNUP.route,
            enterTransition = appState.slideUpIn(500),
            exitTransition = appState.slideDownOut(500)
        ){
            SignUpScreen(
                onBack = appState::popUp,
                snackbarHostState = appState.snackbarHostState
            )
        }

        composable(route = NavigationItem.HOME.route){
            HomeScreen(
                snackbarHostState = appState.snackbarHostState,
                openScreen = {route -> appState.navigate(route)},
                openSchedule = {route,date -> appState.navigate(route,date)},
                openEditSchedule = {route,schedule -> appState.navigate(route,schedule)})
        }

        composable(route = NavigationItem.MENU.route){

        }

        composable(
            route = NavigationItem.ANALYSIS.route,
            enterTransition = appState.slideUpIn(500),
            exitTransition = appState.slideDownOut(500)
        ){
            AnalysisScreen(
                snackbarHostState = appState.snackbarHostState,
                onBack = appState::popUp)
        }

        composable(
            route = NavigationItem.TAG.route,
            enterTransition = appState.slideUpIn(500,300),
            exitTransition = appState.slideDownOut(500)
        ){
            TagScreen(
                snackbarHostState = appState.snackbarHostState,
                onBackHome = appState::popUp
            )
        }

        composable(
            route = "${NavigationItem.SCHEDULE.route}?today={today}&schedule={schedule}"
            ,arguments = listOf(
                navArgument("today"){
                    type = NavType.IntType
                    defaultValue = 0
                },
                navArgument("schedule"){
                    type = NavType.StringType
                    defaultValue = ""
                }
            ),
            enterTransition = appState.slideUpIn(500),
            exitTransition = appState.slideDownOut(500)
        ){
            ScheduleScreen(
                snackbarHostState = appState.snackbarHostState,
                onBackHome = appState::popUp
            )
        }

        composable(
            route = NavigationItem.MISSON.route,
            enterTransition = appState.slideUpIn(500),
            exitTransition = appState.slideDownOut(500)
        ){
            Box(modifier = Modifier
                .background(black)
                .fillMaxSize()) {
                Text(text = "test")
            }
        }

        composable(
            route = NavigationItem.SETTING.route,
            enterTransition = appState.slideRightIn(500),
            exitTransition = appState.slideLeftOut(500)
        ){
            SettingScreen(
                onBack = appState::navigateAllclear,
                snackbarHostState = appState.snackbarHostState
            )
        }

    }
}

