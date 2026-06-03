package com.example.kimstaste.navigation

// This defines paths to access our composable screens
sealed class Screen(val route: String) {
    object Splash : Screen(route = "splash")
    object Login : Screen(route = "login")
    object Signup : Screen(route = "signup")
    object ForgotPassword : Screen(route = "forgot_password")
    object Home : Screen(route = "home_screen")
    object Booking : Screen(route = "booking_screen")
    object MyStay : Screen(route = "mystay_screen")
    object Profile : Screen(route = "profile_screen")
    object UploadMedia : Screen(route = "upload_media")
    object EditMedia : Screen(route = "edit_media")
    object MemberCard : Screen(route = "member_card")

    // Sub-Details Flow
    object MemberScreen : Screen(route = "member_screen/{itemId}") {
        fun passId(id: String) = "member_screen/$id"
    }
}
