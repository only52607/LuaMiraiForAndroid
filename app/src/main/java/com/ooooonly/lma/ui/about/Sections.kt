package com.ooooonly.lma.ui.about

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Update
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.microsoft.appcenter.distribute.Distribute
import com.ooooonly.lma.R
import com.ooooonly.lma.ui.components.selection.SelectionIcon
import com.ooooonly.lma.ui.components.selection.SelectionItem
import com.ooooonly.lma.utils.ripperClickable

@OptIn(ExperimentalMaterialApi::class)
fun LazyListScope.aboutItems(
    context: Context
) {
    val githubItem = SelectionItem.ResourceIcon(
        labelResId = R.string.about_github,
        contentDescriptionResId = R.string.about_github,
        iconResId = R.drawable.ic_github_light,
        onClick = {
            context.startActivity(Intent().apply {
                action = "android.intent.action.VIEW"
                data = Uri.parse("https://github.com/only52607/lua-mirai")
            })
        }
    )
    val updateCheckItem = SelectionItem.ImageVectorIcon(
        labelResId = R.string.about_check_update,
        contentDescriptionResId = R.string.about_check_update,
        iconImageVector = Icons.Filled.Update,
        onClick = Distribute::checkForUpdate
    )
    val listItems = listOf(
        githubItem,
        updateCheckItem
    )
    items(listItems) {
        ListItem(
            icon = { SelectionIcon(it) },
            text = { Text(stringResource(it.labelResId)) },
            modifier = Modifier.ripperClickable { it.onClick() }
        )
    }
}