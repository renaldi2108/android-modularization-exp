package com.example.feature.users.presentation.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.core.shared.designsystem.theme.AppTheme
import com.example.feature.users.domain.AppUser

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailScreen(
    state: UserDetailUiState,
    onAction: (UserDetailAction) -> Unit,
    snackbarHostState: SnackbarHostState,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(state.user?.fullName ?: "Detail Pengguna", maxLines = 1) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Kembali")
                    }
                },
            )
        },
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding), Alignment.Center) {
            when {
                state.isLoading -> CircularProgressIndicator()
                state.user != null -> UserDetailContent(state.user)
                state.errorMessage != null -> Text(state.errorMessage, color = AppTheme.colors.error)
            }
        }
    }
}

@Composable
private fun UserDetailContent(user: AppUser) {
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(AppTheme.dimens.screenPadding),
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.spaceSm),
    ) {
        Text(user.fullName, style = AppTheme.typography.headlineSmall)
        Text(user.email, style = AppTheme.typography.bodyMedium, color = AppTheme.colors.onSurfaceVariant)
        Text("Telepon: ${user.phone}", style = AppTheme.typography.bodyMedium)
        Text("Umur: ${user.age}", style = AppTheme.typography.bodyMedium)
        Text("Universitas: ${user.university}", style = AppTheme.typography.bodyMedium)
    }
}
