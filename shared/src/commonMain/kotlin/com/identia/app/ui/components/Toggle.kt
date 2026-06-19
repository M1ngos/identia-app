package com.identia.app.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.identia.app.core.theme.Border
import com.identia.app.core.theme.Card
import com.identia.app.core.theme.IdentiaTheme

/** Custom pill toggle matching the design (purple = on, with glow). */
@Composable
fun IdentiaToggle(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val trackShape = RoundedCornerShape(99.dp)
    val trackColor by animateColorAsState(
        if (checked) IdentiaTheme.colors.primary else Card,
    )
    val knobOffset by animateDpAsState(if (checked) 20.dp else 0.dp)

    Box(
        modifier = modifier
            .size(width = 46.dp, height = 26.dp)
            .then(if (checked) Modifier.glow(IdentiaTheme.colors.primary.copy(alpha = 0.5f), 14.dp, trackShape) else Modifier)
            .clip(trackShape)
            .background(trackColor)
            .then(if (checked) Modifier else Modifier.border(1.dp, Border, trackShape))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ) { onCheckedChange(!checked) }
            .padding(3.dp),
        contentAlignment = Alignment.CenterStart,
    ) {
        Box(
            Modifier
                .padding(start = knobOffset)
                .size(20.dp)
                .clip(CircleShape)
                .background(Color.White),
        )
    }
}
