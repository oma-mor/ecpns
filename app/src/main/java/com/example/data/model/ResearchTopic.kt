package com.example.data.model

data class ResearchTopic(
    val id: String,
    val title: String,
    val category: String, // التأسيس، الأهداف، الانتخابات، الجلسات، التمكين، أزهر سيناء
    val summary: String,
    val fullContent: String,
    val keyFacts: List<String>,
    val iconName: String,
    val dateOrEra: String
)
