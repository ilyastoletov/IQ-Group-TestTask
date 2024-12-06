package com.ilyastoletov.iqtest.presentation.vacancies.screen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilyastoletov.domain.model.filter.FilterKey
import com.ilyastoletov.domain.model.filter.FilterValue
import com.ilyastoletov.domain.util.Mock
import com.ilyastoletov.iqtest.R
import com.ilyastoletov.iqtest.presentation.theme.IQGroupTestTheme
import com.ilyastoletov.iqtest.presentation.vacancies.viewmodel.model.FilterMap

@Composable
fun FiltersSideSheet(
    filters: FilterMap,
    onClose: () -> Unit,
    onClear: () -> Unit,
    onApplyFilters: (FilterMap) -> Unit,
) {

    val localFilterMap = remember {
        val filtersPairList = filters.map { it.toPair() }.toTypedArray()
        mutableStateMapOf(*filtersPairList)
    }

    Scaffold(
        topBar = {
            TopBar(
                onClose = onClose,
                onClear = onClear
            )
        },
        bottomBar = {
            Footer {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onApplyFilters.invoke(localFilterMap) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = stringResource(R.string.apply_filters_button),
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.White
                    )
                }
            }
        }
    ) { scaffoldPadding ->
        FiltersList(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding),
            filters = localFilterMap,
            onToggle = { key, value ->
                val mutableFilters = localFilterMap[key]?.toMutableList() ?: return@FiltersList
                val itemIndex = mutableFilters.indexOf(value)
                val item = mutableFilters[itemIndex]
                item.selected = !item.selected
                mutableFilters.set(itemIndex, item)
                localFilterMap[key] = mutableFilters
            }
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    onClose: () -> Unit,
    onClear: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(R.string.filters_side_sheet_title),
                style = MaterialTheme.typography.titleMedium
            )
        },
        navigationIcon = {
            IconButton(
                onClick = onClose
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_cross),
                    tint = Color.Unspecified,
                    contentDescription = null
                )
            }
        },
        actions = {
            TextButton(
                onClick = onClear
            ) {
                Text(
                    text = stringResource(R.string.clear_filters),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(0.5f)
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}

@Composable
private fun FiltersList(
    modifier: Modifier = Modifier,
    filters: FilterMap,
    onToggle: (FilterKey, FilterValue) -> Unit
) {
    Surface(modifier = modifier) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            repeat(filters.keys.size) { keyIndex ->
                val key = remember { filters.keys.elementAt(keyIndex) }

                filters[key]?.let { itemsList ->
                    Column {
                        Text(
                            text = key.displayName,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(
                            modifier = Modifier.height(12.dp)
                        )
                        repeat(itemsList.size) { index ->
                            val filterValue = remember { itemsList[index] }

                            FilterItem(
                                text = filterValue.name,
                                selected = filterValue.selected,
                                onToggle = { onToggle.invoke(key, filterValue) }
                            )
                        }
                        Spacer(
                            modifier = Modifier.height(16.dp)
                        )
                    }
                }

            }
        }
    }
}

@Composable
private fun FilterItem(
    text: String,
    selected: Boolean,
    onToggle: () -> Unit
) {
    var selectedLocal by remember { mutableStateOf(selected) }

    Row(
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onToggle
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = selectedLocal,
            onCheckedChange = {
                selectedLocal = it
                onToggle()
            },
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.secondary,
                uncheckedColor = MaterialTheme.colorScheme.onSurfaceVariant,
                checkmarkColor = Color.White
            )
        )
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FiltersSideSheetPreview() {
    IQGroupTestTheme {
        FiltersSideSheet(
            filters = Mock.testFilters,
            onClose = {},
            onApplyFilters = {},
            onClear = {}
        )
    }
}