package com.ooooonly.lma.utils

import androidx.compose.runtime.*

@Composable
fun <T> rememberMutableStateOf(
    value: T,
    policy: SnapshotMutationPolicy<T> = structuralEqualityPolicy()
) = remember {
    mutableStateOf(value)
}