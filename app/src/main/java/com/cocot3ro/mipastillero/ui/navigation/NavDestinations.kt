package com.cocot3ro.mipastillero.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
data class Splash(val user: Long? = null)

@Serializable
data object Login

@Serializable
data object Home

@Serializable
data object Calendar

@Serializable
data object ActiveMeds

@Serializable
data object FavoriteMeds

@Serializable
data object ManageUsers

@Serializable
data object History

@Serializable
data object Settings

@Serializable
data object Diary

@Serializable
data class MedInfo(val medId: Long)

@Serializable
data object FirstTime
