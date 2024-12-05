package com.ilyastoletov.iqtest.presentation.vacancies.screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ilyastoletov.iqtest.R
import com.ilyastoletov.iqtest.presentation.theme.IQGroupTestTheme

@Composable
fun VacancyItem(
    modifier: Modifier = Modifier,
    title: String,
    company: String,
    salary: String,
    location: String
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Color.White,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(all = 16.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
        )
        Spacer(
            modifier = Modifier.height(12.dp)
        )
        if (salary.isNotEmpty()) {
            Text(
                text = salary,
                style = MaterialTheme.typography.titleMedium,
                fontSize = 18.sp
            )
            Spacer(
                modifier = Modifier.height(12.dp)
            )
        }
        TextItem(
            name = stringResource(R.string.vacancy_item_company),
            value = company
        )
        Spacer(
            modifier = Modifier.height(4.dp)
        )
        TextItem(
            name = stringResource(R.string.vacancy_item_location),
            value = location
        )
    }
}

@Composable
private fun TextItem(
    name: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(0.45f),
            modifier = Modifier.weight(0.3f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.weight(0.7f)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun VacancyItemPreview() {
    IQGroupTestTheme {
        VacancyItem(
            modifier = Modifier.padding(12.dp),
            title = "Электрогазосварщик",
            company = "ООО Росэнергострой",
            salary = "от 85 000 р",
            location = "Россия, Красноярск"
        )
    }
}