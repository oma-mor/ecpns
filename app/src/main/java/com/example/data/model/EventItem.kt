package com.example.data.model

data class EventItem(
    val id: String,
    val title: String,
    val description: String,
    val location: String, // العريش، ديوان عام المحافظة، مركز شباب العريش، إلخ
    val dateText: String,
    val drawableResId: Int? = null,
    val badge: String
)
