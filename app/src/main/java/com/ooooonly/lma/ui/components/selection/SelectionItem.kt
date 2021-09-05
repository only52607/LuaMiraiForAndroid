package com.ooooonly.lma.ui.components.selection

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource

sealed class SelectionItem(
    @StringRes val labelResId: Int,
    @StringRes val contentDescriptionResId: Int,
    val color: Color? = null,
    val onClick: () -> Unit,
) {
    class ResourceIcon(
        @StringRes labelResId: Int,
        @StringRes contentDescriptionResId: Int,
        @DrawableRes val iconResId: Int,
        color: Color? = null,
        onClick: () -> Unit
    ) : SelectionItem(labelResId, contentDescriptionResId, color, onClick)

    class ImageVectorIcon(
        @StringRes labelResId: Int,
        @StringRes contentDescriptionResId: Int,
        val iconImageVector: ImageVector,
        color: Color? = null,
        onClick: () -> Unit
    ) : SelectionItem(labelResId, contentDescriptionResId, color, onClick)
}

@Composable
fun SelectionIcon(item: SelectionItem) {
    val painter = when (item) {
        is SelectionItem.ResourceIcon -> painterResource(item.iconResId)
        is SelectionItem.ImageVectorIcon -> rememberVectorPainter(item.iconImageVector)
    }
    Icon(
        painter = painter,
        contentDescription = stringResource(item.contentDescriptionResId),
        tint = item.color ?: LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
    )
}