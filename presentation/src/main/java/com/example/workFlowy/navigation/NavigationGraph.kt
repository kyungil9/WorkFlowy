package com.example.workFlowy.navigation

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
import com.example.workFlowy.screen.home.WeekViewModel
import com.example.workFlowy.screen.home.HomeScreen
import com.example.workFlowy.screen.schedule.ScheduleScreen
import com.example.workFlowy.screen.schedule.ScheduleViewModel
import com.example.workFlowy.screen.tag.TagScreen
import com.example.workFlowy.screen.tag.TagViewModel
import com.example.workFlowy.ui.theme.black

@Composable
fun NavigationGraph(
    navController: NavHostController,
    weekViewModel: WeekViewModel,
    tagViewModel: TagViewModel,
    scheduleViewModel: ScheduleViewModel
){
    NavHost(
        navController = navController,
        startDestination = NavigationItem.HOME.route
    ){

        composable(route = NavigationItem.HOME.route){
            HomeScreen(
                weekViewModel = weekViewModel,
                onTailIconClick = {navController.navigate(NavigationItem.ANALYSIS.route)},
                onMoveTag = { navController.navigate(NavigationItem.TAG.route)},
                onMoveMisson = { navController.navigate(NavigationItem.MISSON.route)},
                onMoveSchedule = { navController.navigate(NavigationItem.SCHEDULE.route) }
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
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = tween(700, delayMillis = 300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Down,
                    animationSpec = tween(700)
                )
            }
        ){
            TagScreen(
                tagViewModel = tagViewModel,
                onBackHome = {
                    navController.popBackStack()//현재화면 닫기
                }
            )
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
            ScheduleScreen(
                scheduleViewModel = scheduleViewModel,
                onBackHome = {navController.popBackStack()//현재화면 닫기
                }
            )
        }

        composable(
            route = NavigationItem.MISSON.route,
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

    }
}