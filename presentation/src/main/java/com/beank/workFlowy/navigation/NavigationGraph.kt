package com.beank.workFlowy.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.beank.workFlowy.screen.WorkFlowyState
import com.beank.workFlowy.screen.home.WeekViewModel
import com.beank.workFlowy.screen.home.HomeScreen
import com.beank.workFlowy.screen.schedule.ScheduleScreen
import com.beank.workFlowy.screen.tag.TagScreen
import com.beank.workFlowy.ui.theme.black

@Composable
fun NavigationGraph(
    appState : WorkFlowyState
){
    NavHost(
        navController = appState.navController,
        startDestination = NavigationItem.HOME.route
    ){
        composable(route = NavigationItem.HOME.route){
            HomeScreen(
                onTailIconClick = {appState.navigate(NavigationItem.ANALYSIS.route)},
                onMoveTag = { appState.navigate(NavigationItem.TAG.route)},
                onMoveMisson = { appState.navigate(NavigationItem.MISSON.route)},
                onMoveSchedule = { appState.navigate(NavigationItem.SCHEDULE.route) }
            )
        }

        composable(route = NavigationItem.MENU.route){

        }

        composable(
            route = NavigationItem.ANALYSIS.route,
            enterTransition = appState.slideUpIn(700),
            exitTransition = appState.slideDownOut(700)
        ){
            Box(modifier = Modifier
                .background(black)
                .fillMaxSize()) {
                Text(text = "test")
            }
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
            route = NavigationItem.SCHEDULE.route,
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