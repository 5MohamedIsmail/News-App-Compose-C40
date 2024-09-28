package com.example.news_compose_c40.screens.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.LocaleListCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.news_compose_c40.R
import com.example.news_compose_c40.activity.HomeActivity
import com.example.news_compose_c40.ui.theme.Poppins
import com.example.news_compose_c40.ui.theme.gray
import com.example.news_compose_c40.ui.theme.green
import com.example.news_compose_c40.widgets.NewsTopAppBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.Serializable

@Serializable
object SettingsRoute

@Composable
fun SettingsScreen(
    viewModel: SettingsScreenViewModel = hiltViewModel(),
    scope: CoroutineScope,
    drawerState: DrawerState
) {
    Scaffold(
        topBar = {
            NewsTopAppBar(
                shouldDisplaySearchIcon = false,
                shouldDisplayMenuIcon = true,
                titleResourceId = R.string.settings,
                scope = scope,
                drawerState = drawerState
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
                .paint(painterResource(R.drawable.bg_pattern), contentScale = ContentScale.Crop)

        ) {
            LanguageSelection(viewModel)
        }
    }
}

@Composable
fun LanguageSelection(viewModel: SettingsScreenViewModel) {
    var expanded by remember { mutableStateOf(false) }
    val languages = mapOf("en" to "English", "ar" to "العربية")
    val activity = (LocalContext.current) as HomeActivity
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.language_title),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = Poppins,
            color = gray,
            modifier = Modifier.padding(top = 16.dp, bottom = 32.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(color = Color.White)
                .border(width = 2.dp, color = green)
                .clickable { expanded = !expanded }
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                GetLanguageIcon(viewModel.selectedLanguageCode)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = viewModel.selectedLanguage,
                    fontSize = 16.sp,
                    color = green,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = "Dropdown",
                    tint = green
                )
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(360.dp)
                .background(Color.White)
        ) {
            languages.forEach { language ->
                DropdownMenuItem(
                    onClick = {
                        viewModel.updateLanguage(language.value, language.key)
                        expanded = false
                        AppCompatDelegate.setApplicationLocales(
                            LocaleListCompat.forLanguageTags(
                                language.key
                            )
                        )
                        activity.recreate()
                    },
                    text = {
                        Row {
                            GetLanguageIcon(languageCode = language.key)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = language.value)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun GetLanguageIcon(languageCode: String) {
    val painter = when (languageCode) {
        "en" -> painterResource(id = R.drawable.flag_united_kingdom)
        "ar" -> painterResource(id = R.drawable.flag_saudi_arabia)
        else -> painterResource(id = R.drawable.ic_launcher_foreground) // Default icon
    }
    Image( // Directly use Image composable here
        painter = painter,
        contentDescription = null,
        modifier = Modifier.size(24.dp)
    )
}