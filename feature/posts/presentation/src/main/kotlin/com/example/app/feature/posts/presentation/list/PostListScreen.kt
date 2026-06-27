package com.example.app.feature.posts.presentation.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import com.example.app.core.shared.designsystem.component.AppButton
import com.example.app.core.shared.designsystem.component.AppTextField
import com.example.app.core.shared.designsystem.theme.AppTheme
import com.example.app.feature.posts.domain.Post

@Composable
fun PostListScreen(
    state: PostListUiState,
    onAction: (PostListAction) -> Unit,
    snackbarHostState: SnackbarHostState,
    onPostClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(modifier = modifier, snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            AppTextField(
                value = state.query,
                onValueChange = { onAction(PostListAction.QueryChanged(it)) },
                label = "Cari post",
                leadingIcon = Icons.Outlined.Search,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { onAction(PostListAction.Submit) }),
                modifier = Modifier.padding(horizontal = AppTheme.dimens.space, vertical = AppTheme.dimens.spaceSm),
            )
            when {
                state.isLoading && state.posts.isEmpty() ->
                    Box(Modifier.fillMaxSize(), Alignment.Center) { CircularProgressIndicator() }

                state.errorMessage != null && state.posts.isEmpty() ->
                    Box(Modifier.fillMaxSize(), Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(state.errorMessage, color = AppTheme.colors.error)
                            AppButton("Coba lagi", { onAction(PostListAction.Retry) }, Modifier.padding(top = AppTheme.dimens.space))
                        }
                    }

                else -> LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(AppTheme.dimens.space),
                    verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.spaceSm),
                ) {
                    items(state.posts, key = { it.id }) { post ->
                        PostRow(post) { onPostClick(post.id) }
                    }
                }
            }
        }
    }
}

@Composable
private fun PostRow(post: Post, onClick: () -> Unit) {
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(AppTheme.dimens.space),
            verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.spaceXs),
        ) {
            Text(post.title, style = AppTheme.typography.titleMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(post.body, style = AppTheme.typography.bodySmall, color = AppTheme.colors.onSurfaceVariant, maxLines = 2, overflow = TextOverflow.Ellipsis)
            Text("👁 ${post.views}   👍 ${post.likes}", style = AppTheme.typography.labelMedium, color = AppTheme.colors.primary)
        }
    }
}
