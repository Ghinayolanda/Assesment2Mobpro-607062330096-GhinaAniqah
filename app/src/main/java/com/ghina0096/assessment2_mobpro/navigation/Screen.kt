package com.ghina0096.assessment2_mobpro.navigation

import com.ghina0096.assessment2_mobpro.ui.theme.screen.KEY_ID_RESEP

sealed class Screen(val route: String) {
    data object Home : Screen("mainScreen")
    data object FormBaru: Screen("detailScreen")
    data object FormUbah: Screen("detailScreen/{$KEY_ID_RESEP}") {
        fun withId(id: Long) = "detailScreen/$id"
    }
}
