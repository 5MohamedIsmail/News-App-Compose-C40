package com.example.news_compose_c40.screens.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.news_compose_c40.R
import com.example.news_compose_c40.screens.news.getErrorMessage
import com.example.news_compose_c40.ui.theme.green
import com.example.news_compose_c40.widgets.ErrorDialog
import com.example.news_compose_c40.widgets.NewsList
import kotlinx.serialization.Serializable

@Serializable
object SearchRoute

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    onNewsClick: (String, String) -> Unit
) {

    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(key1 = true) {
        focusRequester.requestFocus()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .paint(painterResource(R.drawable.bg_pattern), contentScale = ContentScale.Crop)
    )
    {
        Box(
            modifier = Modifier
                .fillMaxHeight(.1f)
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomEnd = 50.dp, bottomStart = 50.dp))
                .background(green)
                .padding(horizontal = 30.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            TextField(
                value = viewModel.searchQuery,
                singleLine = true,
                onValueChange = { viewModel.setSearchQuery(it) },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = green,
                    unfocusedTextColor = green,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = green
                ), modifier = Modifier
                    .padding(15.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(30.dp))
                    .focusRequester(focusRequester)
                    .onFocusChanged { focusState ->
                        viewModel.setIsFocused(focusState.isFocused)
                    },
                placeholder = {
                    Text(text = stringResource(R.string.search_articles))
                }, keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search
                ), keyboardActions = KeyboardActions(onSearch = {
                    viewModel.getNews()
                    focusManager.clearFocus()
                }), leadingIcon = {
                    Image(
                        painterResource(R.drawable.search_icon),
                        stringResource(R.string.search), modifier = Modifier.clickable {
                            viewModel.getNews()
                            focusManager.clearFocus()
                        }
                    )
                }, trailingIcon = {
                    if (viewModel.isFocused) {
                        Image(
                            painter = painterResource(R.drawable.close_icon),
                            contentDescription = stringResource(
                                R.string.close
                            ), modifier = Modifier.clickable {
                                if (viewModel.searchQuery.isNotEmpty())
                                    viewModel.setSearchQuery("")
                                else
                                    focusManager.clearFocus()
                            }
                        )
                    }
                }
            )
        }

        if (viewModel.isErrorDialogVisible) {
            val errorMessage = getErrorMessage(
                errorMessage = viewModel.uiMessage.errorMessage,
                errorMessageId = viewModel.uiMessage.errorMessageId
            )
            ErrorDialog(
                errorMessage = errorMessage,
                onRetry = { viewModel.uiMessage.retryAction }) {
                viewModel.hideErrorDialog()
            }
        }

        NewsList(
            newsList = viewModel.articlesList,
            shouldDisplayNoArticlesFound = viewModel.uiMessage.shouldDisplayNoArticlesFound,
            loadingState = viewModel.uiMessage.isLoading
        ) { title, sourceName ->
            onNewsClick(title, sourceName)
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun SearchScreenPreview() {
    SearchScreen { _, _ ->
    }
}
