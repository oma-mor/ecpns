package com.example.ui.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.data.ParliamentData
import com.example.data.db.MemberRegistration
import com.example.data.db.QuizResult
import com.example.data.db.SavedArticle
import com.example.data.model.ResearchTopic
import com.example.data.repository.ParliamentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppScreen(val titleAr: String) {
    HOME("الرئيسية"),
    RESEARCH("البحث الشامل"),
    COMMITTEES("اللجان البرلمانية"),
    GALLERY("معرض الفعاليات"),
    JOIN("استمارة الانضمام"),
    QUIZ("اختبار الثقافة")
}

class ParliamentViewModel(val repository: ParliamentRepository) : ViewModel() {

    private val _currentScreen = MutableStateFlow(AppScreen.HOME)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("الكل")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _selectedArticle = MutableStateFlow<ResearchTopic?>(null)
    val selectedArticle: StateFlow<ResearchTopic?> = _selectedArticle.asStateFlow()

    val savedArticles: StateFlow<List<SavedArticle>> = repository.savedArticles
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val memberRegistrations: StateFlow<List<MemberRegistration>> = repository.allRegistrations
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val quizResults: StateFlow<List<QuizResult>> = repository.quizResults
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredResearchTopics: StateFlow<List<ResearchTopic>> = combine(
        _searchQuery,
        _selectedCategory
    ) { query, category ->
        var list = repository.getResearchTopics()
        if (category != "الكل") {
            list = list.filter { it.category == category }
        }
        if (query.isNotBlank()) {
            list = list.filter {
                it.title.contains(query, ignoreCase = true) ||
                        it.summary.contains(query, ignoreCase = true) ||
                        it.fullContent.contains(query, ignoreCase = true)
            }
        }
        list
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), repository.getResearchTopics())

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedCategory(category: String) {
        _selectedCategory.value = category
    }

    fun selectArticle(topic: ResearchTopic?) {
        _selectedArticle.value = topic
    }

    fun toggleSaveArticle(topic: ResearchTopic) {
        viewModelScope.launch {
            val isCurrentlySaved = savedArticles.value.any { it.articleId == topic.id }
            if (isCurrentlySaved) {
                repository.deleteSavedArticle(topic.id)
            } else {
                repository.saveArticle(
                    SavedArticle(
                        articleId = topic.id,
                        title = topic.title,
                        category = topic.category,
                        summary = topic.summary
                    )
                )
            }
        }
    }

    fun submitRegistration(
        fullName: String,
        ageStr: String,
        city: String,
        school: String,
        phone: String,
        committee: String,
        motivation: String,
        context: Context,
        onSuccess: () -> Unit
    ) {
        val age = ageStr.toIntOrNull()
        if (fullName.isBlank() || age == null || age !in 9..19 || city.isBlank() || phone.isBlank()) {
            Toast.makeText(context, "يرجى تعبئة جميع الحقول بشكل صحيح (العمر بين 10 و 18 سنة)", Toast.LENGTH_LONG).show()
            return
        }

        viewModelScope.launch {
            val reg = MemberRegistration(
                fullName = fullName,
                age = age,
                city = city,
                schoolOrAzhar = school,
                phone = phone,
                preferredCommittee = committee,
                motivationStatement = motivation,
                isSubmittedToGoogleForm = true
            )
            repository.saveRegistration(reg)
            Toast.makeText(context, "تم حفظ بيانات الانضمام بنجاح! يتم الآن فتح رابط النماذج الرسمي...", Toast.LENGTH_SHORT).show()
            openOfficialGoogleForm(context)
            onSuccess()
        }
    }

    fun openOfficialGoogleForm(context: Context) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(ParliamentData.GOOGLE_FORM_URL))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "عذراً، تعذر فتح الرابط المباشر. يمكنك نسخه: ${ParliamentData.GOOGLE_FORM_URL}", Toast.LENGTH_LONG).show()
        }
    }

    fun recordQuizScore(score: Int, total: Int) {
        viewModelScope.launch {
            val percentage = (score.toFloat() / total.toFloat()) * 100f
            repository.recordQuizResult(
                QuizResult(
                    score = score,
                    totalQuestions = total,
                    percentage = percentage
                )
            )
        }
    }
}

class ParliamentViewModelFactory(private val repository: ParliamentRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ParliamentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ParliamentViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
