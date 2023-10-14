package com.beank.workFlowy.navigation

import androidx.annotation.DrawableRes
import com.beank.presentation.R

sealed class NavigationItem(
    val title:String,
    val route:String,
    @DrawableRes val icon:Int? = null
){
    data object HOME: NavigationItem(
        title = "홈",
        route = "Home",
        icon = R.drawable.baseline_home_24
    )

    data object MENU: NavigationItem(
        title = "메뉴",
        route = "Menu",
        icon = R.drawable.baseline_dehaze_24
    )

    data object ANALYSIS: NavigationItem(
        title = "통계",
        route = "Analysis",
        icon = R.drawable.baseline_insert_chart_outlined_24
    )

    data object TAG: NavigationItem(
        title = "태그추가",
        route = "Tag",
        icon = R.drawable.baseline_add_circle_24
    )

    data object SCHEDULE: NavigationItem(
        title = "스캐줄 추가",
        route = "Schedule",
        icon = R.drawable.baseline_post_add_24
    )

    data object MISSON: NavigationItem(
        title = "미션",
        route = "Misson",
        icon = R.drawable.baseline_cookie_24
    )

    data object LOGIN: NavigationItem(
        title = "로그인화면",
        route = "Login",
        icon = null
    )

    data object SIGNUP: NavigationItem(
        title = "계정생성",
        route = "Signup",
        icon = R.drawable.ic_create_account
    )

    data object SETTING: NavigationItem(
        title = "설정화면",
        route = "Setting",
        icon = null
    )

    data object TRIGGER: NavigationItem(
        title = "트리거화면",
        route = "Trigger",
        icon = null
    )

    data object ADDTRIGGER: NavigationItem(
        title = "트리거추가화면",
        route = "AddTrigger",
        icon = null
    )

}
