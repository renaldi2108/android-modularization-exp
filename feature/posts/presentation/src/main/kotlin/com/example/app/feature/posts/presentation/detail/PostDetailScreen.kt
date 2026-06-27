package com.example.app.feature.posts.presentation.detail

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
import com.example.app.core.shared.designsystem.theme.AppTheme
import com.example.app.feature.posts.domain.Post

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailScreen(
    state: PostDetailUiState,
    onAction: (PostDetailAction) -> Unit,
    snackbarHostState: SnackbarHostState,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(state.post?.title ?: "Detail Post", maxLines = 1) },
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
                state.post != null -> PostDetailContent(state.post)
                state.errorMessage != null -> Text(state.errorMessage, color = AppTheme.colors.error)
            }
        }
    }
}

@Composable
private fun PostDetailContent(post: Post) {
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(AppTheme.dimens.screenPadding),
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.spaceSm),
    ) {
        Text(post.title, style = AppTheme.typography.headlineSmall)
        Text(
            "👁 ${post.views}    👍 ${post.likes}    👎 ${post.dislikes}",
            style = AppTheme.typography.labelLarge,
            color = AppTheme.colors.primary,
        )
        if (post.tags.isNotEmpty()) {
            Text("#${post.tags.joinToString("  #")}", style = AppTheme.typography.bodySmall, color = AppTheme.colors.onSurfaceVariant)
        }
        Text(post.body, style = AppTheme.typography.bodyMedium)
    }
}
