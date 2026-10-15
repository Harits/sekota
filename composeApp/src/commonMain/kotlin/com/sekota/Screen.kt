package com.sekota

sealed interface Screen {
    data object Landing : Screen
    data object Catalog : Screen
    data object Details : Screen
    data object ProductDetails : Screen
    data object Merchandise : Screen
    data object Login : Screen
    data object Signup : Screen
    data object Profile : Screen
    data object Admin : Screen
    data class BookReader(val bookId: String, val chapterIndex: Int = 0) : Screen
}
