package com.pokemon.ui

sealed class MainUiState

object LoadingState : MainUiState()
object LoadingNextPageState : MainUiState()
object SecondaryLoadingState : MainUiState()

object ContentState : MainUiState()
object ContentNextPageState : MainUiState()
object EmptyState : MainUiState()
class ErrorState(val message: String) : MainUiState()
class ErrorNextPageState(val message: String) : MainUiState()
class ErrorSecondaryState(val message: String) : MainUiState()
