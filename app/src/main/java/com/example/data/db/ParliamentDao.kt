package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ParliamentDao {
    @Query("SELECT * FROM member_registrations ORDER BY timestamp DESC")
    fun getAllRegistrations(): Flow<List<MemberRegistration>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRegistration(registration: MemberRegistration): Long

    @Query("DELETE FROM member_registrations WHERE id = :id")
    suspend fun deleteRegistration(id: Long)

    @Query("SELECT * FROM saved_articles ORDER BY savedAt DESC")
    fun getSavedArticles(): Flow<List<SavedArticle>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveArticle(article: SavedArticle)

    @Query("DELETE FROM saved_articles WHERE articleId = :articleId")
    suspend fun deleteSavedArticle(articleId: String)

    @Query("SELECT EXISTS(SELECT 1 FROM saved_articles WHERE articleId = :articleId)")
    fun isArticleSaved(articleId: String): Flow<Boolean>

    @Query("SELECT * FROM quiz_results ORDER BY timestamp DESC")
    fun getQuizResults(): Flow<List<QuizResult>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizResult(quizResult: QuizResult)
}
