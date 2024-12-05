package com.ilyastoletov.iqtest.presentation.vacancies.screen.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilyastoletov.iqtest.R
import com.ilyastoletov.iqtest.presentation.theme.IQGroupTestTheme

@Composable
fun SearchField(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onClickFilters: () -> Unit,
    onClickSorting: () -> Unit
) {
    OutlinedTextField(
        modifier = modifier,
        value = query,
        onValueChange = onQueryChange,
        shape = RoundedCornerShape(percent = 100),
        placeholder = {
            Text(
                text = stringResource(R.string.search_field_placeholder),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )
        },
        leadingIcon = {
            IconButton(onClick = onSearch) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_search),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    contentDescription = null
                )
            }
        },
        trailingIcon = {
            Row(verticalAlignment = Alignment.CenterVertically) {

                IconButton(
                    onClick = onClickFilters
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_filter),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        contentDescription = null
                    )
                }

                IconButton(
                    onClick = onClickSorting
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_sort),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        contentDescription = null
                    )
                }

            }
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = { onSearch.invoke() }
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            disabledBorderColor = Color.Transparent,
            errorBorderColor = Color.Transparent,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            disabledContainerColor = Color.White,
            errorContainerColor = Color.White
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun SearchFieldPreview() {
    IQGroupTestTheme {
        SearchField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            query = "",
            onQueryChange = {},
            onSearch = {},
            onClickSorting = {},
            onClickFilters = {}
        )
    }
}