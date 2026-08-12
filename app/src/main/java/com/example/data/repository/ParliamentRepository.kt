package com.example.data.repository

import com.example.data.data.ParliamentData
import com.example.data.db.MemberRegistration
import com.example.data.db.ParliamentDao
import com.example.data.db.QuizResult
import com.example.data.db.SavedArticle
import com.example.data.model.Committee
import com.example.data.model.EventItem
import com.example.data.model.QuizQuestion
import com.example.data.model.ResearchTopic
import kotlinx.coroutines.flow.Flow

class ParliamentRepository(private val dao: ParliamentDao) {

    val allRegistrations: Flow<List<MemberRegistration>> = dao.getAllRegistrations()
    val savedArticles: Flow<List<SavedArticle>> = dao.getSavedArticles()
    val quizResults: Flow<List<QuizResult>> = dao.getQuizResults()

    suspend fun saveRegistration(registration: MemberRegistration): Long {
        return dao.insertRegistration(registration)
    }

    suspend fun deleteRegistration(id: Long) {
        dao.deleteRegistration(id)
    }

    suspend fun saveArticle(article: SavedArticle) {
        dao.saveArticle(article)
    }

    suspend fun deleteSavedArticle(articleId: String) {
        dao.deleteSavedArticle(articleId)
    }

    fun isArticleSaved(articleId: String): Flow<Boolean> {
        return dao.isArticleSaved(articleId)
    }

    suspend fun recordQuizResult(result: QuizResult) {
        dao.insertQuizResult(result)
    }

    fun getResearchTopics(): List<ResearchTopic> = ParliamentData.researchTopics

    fun getCommittees(): List<Committee> = ParliamentData.committees

    fun getEvents(): List<EventItem> = ParliamentData.events

    fun getQuizQuestions(): List<QuizQuestion> = ParliamentData.quizQuestions
}
