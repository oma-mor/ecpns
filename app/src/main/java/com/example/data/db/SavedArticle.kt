package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_articles")
data class SavedArticle(
    @PrimaryKey
    val articleId: String,
    val title: String,
    val category: String,
    val summary: String,
    val savedAt: Long = System.currentTimeMillis()
)
