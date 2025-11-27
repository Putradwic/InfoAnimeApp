package com.putradwicahyono.ime.ui.components


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.putradwicahyono.ime.ui.theme.LinkStyle
import com.putradwicahyono.ime.ui.theme.SectionHeaderStyle

/**
 * Section Header Component
 * Header untuk section dengan judul dan "Lihat Semua" button
 *
 * File: ui/components/SectionHeader.kt
 */
@Composable
fun SectionHeader(
    title: String,
    onSeeAllClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Section Title
        Text(
            text = title,
            style = SectionHeaderStyle,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold
        )

        // "Lihat Semua" Link
        if (onSeeAllClick != null) {
            Text(
                text = "Lihat Semua",
                style = LinkStyle,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { onSeeAllClick() }
            )
        }
    }
}