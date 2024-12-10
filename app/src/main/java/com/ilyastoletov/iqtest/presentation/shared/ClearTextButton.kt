package com.ilyastoletov.iqtest.presentation.shared

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.ilyastoletov.iqtest.R

@Composable
fun ClearTextButton(onClear: () -> Unit) {
    TextButton(
        onClick = onClear
    ) {
        Text(
            text = stringResource(R.string.clear_filters),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(0.5f)
        )
    }
}