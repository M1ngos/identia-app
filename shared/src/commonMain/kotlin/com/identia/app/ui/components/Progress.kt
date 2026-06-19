package com.identia.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.identia.app.core.theme.IdentiaTheme

private val SegmentInactive = Color(0xFF2A2A33)

/** Top segmented progress bar (e.g. 4 steps through the verification flow). */
@Composable
fun SegmentedProgress(
    total: Int,
    active: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        repeat(total) { index ->
            Segment(filled = index < active)
        }
    }
}

@Composable
private fun RowScope.Segment(filled: Boolean) {
    Box(
        Modifier
            .weight(1f)
            .height(3.dp)
            .clip(RoundedCornerShape(2.dp))
            .background(if (filled) IdentiaTheme.colors.primary else SegmentInactive),
    )
}
