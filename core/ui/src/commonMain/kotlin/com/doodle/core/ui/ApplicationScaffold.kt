package com.doodle.core.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplicationScaffold(
    modifier: Modifier = Modifier,
    title: String,
    navigationIcon: @Composable () -> Unit,
    vararg actions: @Composable () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = title) },
                navigationIcon = navigationIcon,
                actions = { actions.forEach { it() } },
                windowInsets = WindowInsets.statusBars
            )
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        content(innerPadding)
    }
}
