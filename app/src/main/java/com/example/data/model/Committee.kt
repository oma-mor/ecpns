package com.example.data.model

data class Committee(
    val id: String,
    val name: String,
    val roleDescription: String,
    val primaryFocus: List<String>,
    val targetAgeGroup: String = "10 - 18 سنة",
    val memberCountEstimate: String = "15 - 20 عضواً لكل إدارة شبابية"
)
