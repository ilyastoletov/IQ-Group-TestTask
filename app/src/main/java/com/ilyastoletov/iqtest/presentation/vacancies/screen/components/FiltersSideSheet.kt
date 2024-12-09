package com.ilyastoletov.iqtest.presentation.vacancies.screen.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
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
import com.ilyastoletov.domain.model.filter.AppliedFilters
import com.ilyastoletov.domain.model.filter.Filter
import com.ilyastoletov.domain.model.filter.FilterKey
import com.ilyastoletov.domain.model.filter.FilterValue
import com.ilyastoletov.domain.util.Mock
import com.ilyastoletov.iqtest.R
import com.ilyastoletov.iqtest.presentation.theme.IQGroupTestTheme
import com.ilyastoletov.domain.model.filter.FilterMap
import com.ilyastoletov.iqtest.presentation.vacancies.viewmodel.model.FiltersLoadingState
import kotlin.math.roundToInt


@Composable
fun FiltersSideSheet(
    filters: Filter,
    appliedFilters: AppliedFilters,
    loadingState: FiltersLoadingState,
    onClose: () -> Unit,
    onClear: () -> Unit,
    onApplyFilters: (AppliedFilters) -> Unit
) {

    val localFiltersMap = remember(appliedFilters) {
        val filtersPairList = appliedFilters.map.map { it.toPair() }.toTypedArray()
        mutableStateMapOf(*filtersPairList)
    }

    var salaryFilter by remember(appliedFilters) {
        mutableStateOf(appliedFilters.salary)
    }

    BackHandler(
        enabled = true,
        onBack = onClose
    )

    Scaffold(
        topBar = {
            TopBar(
                onClose = onClose,
                onClear = {
                    onClear()
                    localFiltersMap.clear()
                }
            )
        },
        bottomBar = {
            Footer {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        onApplyFilters.invoke(
                            AppliedFilters(
                                map = localFiltersMap,
                                salary = salaryFilter
                            )
                        )
                        onClose()
                    },
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
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { scaffoldPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding),
            contentAlignment = Alignment.Center
        ) {
            when(loadingState) {

                FiltersLoadingState.LOADING -> {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.secondary
                    )
                }

                FiltersLoadingState.LOADED -> {
                    FiltersList(
                        modifier = Modifier.fillMaxSize(),
                        filters = filters,
                        appliedFiltersMap = appliedFilters.map,
                        salaryFilter = salaryFilter,
                        onToggle = { key, value ->
                            val mutableFilters = localFiltersMap[key]?.toMutableList() ?: mutableListOf()
                            mutableFilters.apply { if (value in this) remove(value) else add(value) }
                            localFiltersMap[key] = mutableFilters
                        },
                        onSalaryFilterChange = { salaryFilter = it }
                    )
                }

                FiltersLoadingState.ERROR -> {
                    Text(
                        text = stringResource(R.string.filters_loading_error_message),
                        style = MaterialTheme.typography.titleMedium
                    )
                }

            }
        }

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
    filters: Filter,
    appliedFiltersMap: FilterMap,
    salaryFilter: Int?,
    onToggle: (FilterKey, FilterValue) -> Unit,
    onSalaryFilterChange: (Int) -> Unit
) {
    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        FilterItem(
            title = stringResource(R.string.experience_filter),
            items = filters.experience,
            selected = appliedFiltersMap[FilterKey.EXPERIENCE].orEmpty(),
            onToggleItem = { onToggle.invoke(FilterKey.EXPERIENCE, it) }
        )
        FilterItem(
            title = stringResource(R.string.employment_filter),
            items = filters.employment,
            selected = appliedFiltersMap[FilterKey.EMPLOYMENT].orEmpty(),
            onToggleItem = { onToggle.invoke(FilterKey.EMPLOYMENT, it) }
        )
        FilterItem(
            title = stringResource(R.string.schedule_filter),
            items = filters.schedule,
            selected = appliedFiltersMap[FilterKey.SCHEDULE].orEmpty(),
            onToggleItem = { onToggle.invoke(FilterKey.SCHEDULE, it) }
        )
        SalarySlider(
            value = salaryFilter,
            onValueChange = onSalaryFilterChange
        )
        FilterItem(
            title = stringResource(R.string.area_filter),
            items = filters.area,
            selected = appliedFiltersMap[FilterKey.AREA].orEmpty(),
            onToggleItem = { onToggle.invoke(FilterKey.AREA, it) }
        )
    }
}

@Composable
private fun FilterItem(
    title: String,
    items: List<FilterValue>,
    selected: List<FilterValue>,
    onToggleItem: (FilterValue) -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(
            modifier = Modifier.height(12.dp)
        )
        repeat(items.size) { index ->
            val filterValue = remember { items[index] }

            FilterOption(
                text = filterValue.name,
                selected = (filterValue in selected),
                onToggle = { onToggleItem.invoke(filterValue) }
            )
        }
    }
}

@Composable
private fun FilterOption(
    text: String,
    selected: Boolean,
    onToggle: () -> Unit
) {
    var selectedLocal by remember(selected) { mutableStateOf(selected) }

    Row(
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {
                    selectedLocal = !selectedLocal
                    onToggle()
                }
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

@Composable
private fun SalarySlider(
    value: Int?,
    onValueChange: (Int) -> Unit
) {

    var currentValue by remember(value) { mutableStateOf(value?.toFloat()) }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.salary_filter),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = if (currentValue == null) "Не указана" else "${currentValue?.roundToInt()} ₽",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Spacer(
            modifier = Modifier.height(12.dp)
        )
        Slider(
            value = currentValue ?: 0F,
            valueRange = (0F..500_000F),
            steps = 500,
            onValueChangeFinished = { onValueChange(currentValue!!.roundToInt()) },
            onValueChange = { number ->
                currentValue = number.roundToNearestThousand()
            },
            modifier = Modifier.fillMaxWidth(),
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.secondary,
                activeTrackColor = MaterialTheme.colorScheme.secondary,
                inactiveTrackColor = MaterialTheme.colorScheme.onSurfaceVariant,
                activeTickColor = Color.Transparent,
                inactiveTickColor = Color.Transparent
            )
        )
    }
}

private fun Float.roundToNearestThousand(): Float {
    val remainder = this % 1000F
    return if (remainder >= 500F) {
        this + (1000F - remainder)
    } else {
        this - remainder
    }
}

@Preview(showBackground = true)
@Composable
private fun FiltersSideSheetPreview() {
    IQGroupTestTheme {
        FiltersSideSheet(
            filters = Mock.testFilter,
            loadingState = FiltersLoadingState.LOADING,
            appliedFilters = AppliedFilters(),
            onClose = {},
            onApplyFilters = {},
            onClear = {},
        )
    }
}