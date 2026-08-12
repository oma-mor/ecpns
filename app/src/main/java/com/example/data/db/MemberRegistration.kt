package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "member_registrations")
data class MemberRegistration(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val fullName: String,
    val age: Int,
    val city: String, // العريش، الشيخ زويد، رفح، بئر العبد، الحسنة، نخل
    val schoolOrAzhar: String,
    val phone: String,
    val preferredCommittee: String,
    val motivationStatement: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isSubmittedToGoogleForm: Boolean = false
)
