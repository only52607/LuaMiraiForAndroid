package com.ooooonly.lma.ui.components.selection

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource

sealed class SelectionItem(
    @StringRes val labelResId: Int,
    @StringRes val contentDescriptionResId: Int,
    val onClick: () -> Unit
) {
    class ResourceIcon(
        @StringRes labelResId: Int,
        @StringRes contentDescriptionResId: Int,
        @DrawableRes val iconResId: Int,
        onClick: () -> Unit
    ) : SelectionItem(labelResId, contentDescriptionResId, onClick)

    class ImageVectorIcon(
        @StringRes labelResId: Int,
        @StringRes contentDescriptionResId: Int,
        val iconImageVector: ImageVector,
        onClick: () -> Unit
    ) : SelectionItem(labelResId, contentDescriptionResId, onClick)
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
    )
}