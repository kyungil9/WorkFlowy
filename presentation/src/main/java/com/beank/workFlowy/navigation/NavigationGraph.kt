package com.beank.workFlowy.navigation

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
import com.beank.data.mapper.localDateToInt
import com.beank.domain.model.Schedule
import com.beank.workFlowy.screen.WorkFlowyState
import com.beank.workFlowy.screen.analysis.AnalysisScreen
import com.beank.workFlowy.screen.home.HomeScreen
import com.beank.workFlowy.screen.login.LoginScreen
import com.beank.workFlowy.screen.schedule.ScheduleScreen
import com.beank.workFlowy.screen.sign_up.SignUpScreen
import com.beank.workFlowy.screen.tag.TagScreen
import com.beank.workFlowy.ui.theme.black
import java.time.LocalDate

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
                openPopUpScreen = {route,popup ->  appState.navigatePopUp(route,popup)},
                openScreen = {route -> appState.navigate(route)})
        }

        composable(
            route = NavigationItem.SIGNUP.route,
            enterTransition = appState.slideUpIn(700),
            exitTransition = appState.slideDownOut(700)
        ){
            SignUpScreen(onBack = {appState.popUp()})
        }

        composable(route = NavigationItem.HOME.route){
            HomeScreen(
                openScreen = {route -> appState.navigate(route)},
                openSchedule = {route,date -> appState.navigate(route,date)},
                openEditSchedule = {route,schedule -> appState.navigate(route,schedule)})
        }

        composable(route = NavigationItem.MENU.route){

        }

        composable(
            route = NavigationItem.ANALYSIS.route,
            enterTransition = appState.slideUpIn(700),
            exitTransition = appState.slideDownOut(700)
        ){
            AnalysisScreen()
        }

        composable(
            route = NavigationItem.TAG.route,
            enterTransition = appState.slideUpIn(700,300),
            exitTransition = appState.slideDownOut(700)
        ){
            TagScreen(
                resource = appState.resource,
                onBackHome = { appState.popUp() }
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
            enterTransition = appState.slideUpIn(700),
            exitTransition = appState.slideDownOut(700)
        ){
            ScheduleScreen(
                resource = appState.resource,
                onBackHome = { appState.popUp() }
            )
        }

        composable(
            route = NavigationItem.MISSON.route,
            enterTransition = appState.slideUpIn(700),
            exitTransition = appState.slideDownOut(700)
        ){
            Box(modifier = Modifier
                .background(black)
                .fillMaxSize()) {
                Text(text = "test")
            }
        }

    }
}