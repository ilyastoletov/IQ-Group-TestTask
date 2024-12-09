package com.ilyastoletov.iqtest.presentation.vacancies.screen.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilyastoletov.domain.model.Sorting
import com.ilyastoletov.iqtest.R
import com.ilyastoletov.iqtest.presentation.theme.IQGroupTestTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortingBottomSheet(
    selected: Sorting,
    onSelect: (Sorting) -> Unit,
    onDismiss: () -> Unit
) {

    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismiss
    ) {
        val hide: () -> Unit = {
            scope.launch {
                sheetState.hide()
                onDismiss()
            }
        }

        SortingModal(
            modifier = Modifier
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp,
                ),
            selected = selected,
            onApply = { sort ->
                onSelect(sort)
                hide()
            },
            onClose = hide
        )

        Spacer(
            modifier = Modifier.navigationBarsPadding()
        )
    }
}

@Composable
private fun SortingModal(
    modifier: Modifier = Modifier,
    selected: Sorting,
    onApply: (Sorting) -> Unit,
    onClose: () -> Unit
) {

    var localSelectedSorting by remember(selected) { mutableStateOf(selected) }

    Column {
        Column(modifier = modifier) {
            Text(
                text = stringResource(R.string.sorting_title),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(
                modifier = Modifier.height(20.dp)
            )
            SortingItem(
                text = stringResource(R.string.relevance_sorting),
                selected = (localSelectedSorting == Sorting.RELEVANCE),
                onSelect = { localSelectedSorting = Sorting.RELEVANCE }
            )
            Spacer(
                modifier = Modifier.height(8.dp)
            )
            SortingItem(
                text = stringResource(R.string.date_sorting),
                selected = (localSelectedSorting == Sorting.DATE),
                onSelect = { localSelectedSorting = Sorting.DATE }
            )
            Spacer(
                modifier = Modifier.height(20.dp)
            )
        }
        Actions(
            modifier = Modifier,
            onApply = { onApply.invoke(localSelectedSorting) },
            onClose = onClose,
        )
    }

}

@Composable
private fun SortingItem(
    text: String,
    selected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color.White,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onSelect
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.weight(0.9f)
        )
        RadioButton(
            selected = selected,
            modifier = Modifier.weight(0.1f),
            onClick = null,
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.secondary,
                unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}

@Composable
private fun Actions(
    modifier: Modifier,
    onApply: () -> Unit,
    onClose: () -> Unit
) {
    Footer(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Button(
                modifier = Modifier.weight(0.5f),
                onClick = onApply,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = stringResource(R.string.apply_sorting_button),
                    style = MaterialTheme.typography.labelLarge
                )
            }
            Spacer(
                modifier = Modifier.width(8.dp)
            )
            OutlinedButton(
                modifier = Modifier.weight(0.5f),
                onClick = onClose,
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color(0xFF211A14)
                )
            ) {
                Text(
                    text = stringResource(R.string.close_sorting_button),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SortingModalPreview() {
    IQGroupTestTheme {
        SortingModal(
            modifier = Modifier.padding(16.dp),
            selected = Sorting.RELEVANCE,
            onClose = {},
            onApply = {}
        )
    }
}