package com.beank.workFlowy.screen

import android.content.res.Resources
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.material.ScaffoldState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Stable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.beank.domain.model.Schedule
import com.beank.workFlowy.component.snackbar.SnackbarManager
import com.beank.workFlowy.component.snackbar.SnackbarMessage.Companion.toMessage
import com.beank.workFlowy.utils.toInt
import com.beank.workFlowy.utils.toScheduleJson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import java.time.LocalDate

@Stable
class WorkFlowyState(
    val snackbarHostState: SnackbarHostState,
    val navController: NavHostController,
    val resource : Resources,
    private val snackbarManager : SnackbarManager,
    coroutineScope: CoroutineScope
) {
    init {
        coroutineScope.launch {
            snackbarManager.snackbarMessages.filterNotNull().collect() {snackbarMessage ->
                val text = snackbarMessage.toMessage(resource)
                snackbarHostState.showSnackbar(text)
            }
        }
    }

    fun popUp() {
        navController.popBackStack()
    }

    fun navigate(route : String) {
        navController.navigate(route){ launchSingleTop = true }
    }

    fun navigate(route: String, date: LocalDate){
        navController.navigate("${route}?today=${date.toInt()}"){
            launchSingleTop = true
        }
    }

    fun navigate(route: String, schedule: Schedule){
        navController.navigate("${route}?schedule=${schedule.toScheduleJson()}"){
            launchSingleTop = true
        }
    }

    fun navigatePopUp(route: String,popUp : String){
        navController.navigate(route){
            launchSingleTop = true
            popUpTo(popUp) { inclusive = true}
        }
    }

    fun slideUpIn(spec : Int) : (@JvmSuppressWildcards
    AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?) = {
        slideIntoContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Up,
            animationSpec = tween(spec)
        )
    }

    fun slideUpIn(spec : Int,delay : Int) : (@JvmSuppressWildcards
    AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?) = {
        slideIntoContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Up,
            animationSpec = tween(spec, delayMillis = delay)
        )
    }

    fun slideDownOut(spec : Int) : (@JvmSuppressWildcards
    AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?) ={
        slideOutOfContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Down,
            animationSpec = tween(spec)
        )
    }
}