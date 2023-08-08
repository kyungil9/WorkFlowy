package com.example.workFlowy.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.workFlowy.R
import com.example.workFlowy.screen.Home.WeekViewModel
import com.example.workFlowy.component.WeekAppBar
import com.example.workFlowy.component.WeekLayout
import com.example.workFlowy.screen.Home.HomeScreen
import com.example.workFlowy.ui.theme.black

@Composable
fun NavigationGraph(
    navController: NavHostController,
    weekViewModel: WeekViewModel
){
    NavHost(
        navController = navController,
        startDestination = NavigationItem.HOME.route
    ){

        composable(route = NavigationItem.HOME.route){
            HomeScreen(
                weekViewModel = weekViewModel,
                onTailIconClick = {navController.navigate(NavigationItem.ANALYSIS.route)},
                onAddTag = { navController.navigate(NavigationItem.TAG.route)}
            )
        }

        composable(route = NavigationItem.MENU.route){

        }

        composable(
            route = NavigationItem.ANALYSIS.route,
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = tween(700)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Down,
                    animationSpec = tween(700)
                )
            }
        ){
            Box(modifier = Modifier
                .background(black)
                .fillMaxSize()) {
                Text(text = "test")
            }
        }

        composable(
            route = NavigationItem.TAG.route,
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = tween(700)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Down,
                    animationSpec = tween(700)
                )
            }
        ){

        }

        composable(
            route = NavigationItem.SCHEDULE.route,
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = tween(700)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Down,
                    animationSpec = tween(700)
                )
            }
        ){

        }

    }
}