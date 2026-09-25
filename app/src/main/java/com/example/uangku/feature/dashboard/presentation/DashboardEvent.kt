package com.example.uangku.feature.dashboard.presentation

sealed interface DashboardEvent {

    data object onScreenOpen: DashboardEvent

}