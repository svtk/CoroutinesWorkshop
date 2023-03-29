package com.kotlinconf.workshop.articles.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kotlinconf.workshop.articles.ui.LoadingMode

@Composable
fun LoadingControls(
    modifier: Modifier = Modifier,
    loadingMode: LoadingMode,
    updateLoadingMode: (LoadingMode) -> Unit,
    startLoading: () -> Unit,
    newLoadingEnabled: Boolean,
    cancelLoading: () -> Unit,
    cancellationEnabled: Boolean,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        Card(
            modifier = modifier.padding(top = 20.dp, start = 20.dp, end = 20.dp),
        ) {
            Column(
                modifier = modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                LabeledElement("Loading Mode") {
                    LoadingModesDropdown(loadingMode, updateLoadingMode)
                }
            }
        }
        Row {
            Box(
                modifier = Modifier.fillMaxWidth(0.5f),
                contentAlignment = Alignment.Center,
            ) {
                Button(
                    onClick = startLoading,
                    enabled = newLoadingEnabled,
                    modifier = Modifier.fillMaxWidth().padding(top = 10.dp, start = 20.dp, end = 10.dp)
                ) {
                    Text("Load comments")
                }
            }
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                Button(
                    onClick = cancelLoading,
                    enabled = cancellationEnabled,
                    modifier = Modifier.fillMaxWidth().padding(top = 10.dp, start = 10.dp, end = 20.dp)
                ) {
                    Text("Cancel")
                }
            }
        }
    }
}

@Composable
fun LoadingModesDropdown(
    loadingMode: LoadingMode,
    updateLoadingMode: (LoadingMode) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        OutlinedTextField(
            value = loadingMode.name,
            onValueChange = {},
            readOnly = true,
        )
        IconButton(
            modifier = Modifier.align(Alignment.CenterEnd),
            onClick = { expanded = true }
        ) {
            Icon(
                imageVector = Icons.Filled.MoreVert,
                contentDescription = "Options"
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            LoadingMode.values().forEach { chosenMode ->
                DropdownMenuItem(onClick = {
                    updateLoadingMode(chosenMode)
                    expanded = false
                }) {
                    Text(text = chosenMode.name)
                }
            }
        }
    }
}

@Composable
fun LabeledElement(label: String, content: @Composable () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.4f)
                .padding(top = 15.dp),
            contentAlignment = Alignment.CenterStart,
        ) {
            Text(
                style = MaterialTheme.typography.subtitle1,
                text = label
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd,
        ) {
            content()
        }
    }
}
