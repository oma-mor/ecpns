package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.SectionHeader
import com.example.ui.theme.AccentDeepNavy
import com.example.ui.theme.AccentIceBlue
import com.example.ui.theme.AccentNavyContainer
import com.example.ui.theme.DarkBorderColor
import com.example.ui.theme.DarkCanvasBackground
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.OnBackgroundLight
import com.example.ui.theme.OnSurfaceSecondary
import com.example.ui.viewmodel.ParliamentViewModel

@Composable
fun QuizScreen(
    viewModel: ParliamentViewModel
) {
    val questions = remember { viewModel.repository.getQuizQuestions() }
    val pastResults by viewModel.quizResults.collectAsState()

    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var selectedOptionIndex by remember { mutableStateOf<Int?>(null) }
    var score by remember { mutableIntStateOf(0) }
    var quizCompleted by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkCanvasBackground)
            .padding(16.dp)
            .testTag("quiz_screen")
    ) {
        SectionHeader(
            title = "اختبار الثقافة البرلمانية وحقوق الطفل",
            subtitle = "اختبر معلوماتك حول برلمان الطفل والتجربة الديمقراطية بشمال سيناء"
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (!quizCompleted && currentQuestionIndex < questions.size) {
            val q = questions[currentQuestionIndex]

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .border(width = 1.dp, color = DarkBorderColor, shape = RoundedCornerShape(20.dp)),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(AccentNavyContainer)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "السؤال ${currentQuestionIndex + 1} من ${questions.size}",
                                fontWeight = FontWeight.Bold,
                                color = AccentIceBlue,
                                fontSize = 12.sp
                            )
                        }

                        Text(
                            text = "النقاط الحالية: $score",
                            fontWeight = FontWeight.Bold,
                            color = AccentIceBlue,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = q.question,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = OnBackgroundLight,
                            lineHeight = 22.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    q.options.forEachIndexed { idx, optionText ->
                        val isSelected = selectedOptionIndex == idx
                        val isCorrect = idx == q.correctAnswerIndex
                        val backgroundColor = when {
                            selectedOptionIndex != null && isCorrect -> Color(0xFF1B5E20).copy(alpha = 0.4f)
                            selectedOptionIndex != null && isSelected && !isCorrect -> Color(0xFFB71C1C).copy(alpha = 0.4f)
                            isSelected -> AccentNavyContainer
                            else -> DarkCanvasBackground
                        }
                        val borderColor = when {
                            selectedOptionIndex != null && isCorrect -> Color(0xFF81C784)
                            selectedOptionIndex != null && isSelected && !isCorrect -> Color(0xFFE57373)
                            isSelected -> AccentIceBlue
                            else -> DarkBorderColor
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(backgroundColor)
                                .border(1.dp, borderColor, RoundedCornerShape(12.dp))
                                .clickable(enabled = selectedOptionIndex == null) {
                                    selectedOptionIndex = idx
                                    if (idx == q.correctAnswerIndex) {
                                        score++
                                    }
                                }
                                .padding(12.dp)
                        ) {
                            Text(
                                text = optionText,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = OnBackgroundLight
                                )
                            )
                        }
                    }

                    AnimatedVisibility(visible = selectedOptionIndex != null) {
                        Column(modifier = Modifier.padding(top = 12.dp)) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(AccentNavyContainer)
                                    .border(1.dp, DarkBorderColor, RoundedCornerShape(12.dp))
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = "التوضيح العلمي والبرلماني: ${q.explanation}",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = OnBackgroundLight,
                                        lineHeight = 18.sp
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Button(
                                onClick = {
                                    if (currentQuestionIndex + 1 < questions.size) {
                                        currentQuestionIndex++
                                        selectedOptionIndex = null
                                    } else {
                                        quizCompleted = true
                                        viewModel.recordQuizScore(score, questions.size)
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = AccentIceBlue,
                                    contentColor = AccentDeepNavy
                                ),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("next_quiz_btn")
                            ) {
                                Text(
                                    text = if (currentQuestionIndex + 1 < questions.size) "السؤال التالي" else "إنهاء الاختبار وعرض النتيجة",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        } else {
            // Quiz Completed Result Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .border(width = 1.dp, color = DarkBorderColor, shape = RoundedCornerShape(20.dp)),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = null,
                        tint = AccentIceBlue,
                        modifier = Modifier.size(64.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "تهانينا! أكملت اختبار برلمان الطفل",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = OnBackgroundLight
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "حصلت على $score من أصل ${questions.size} درجة",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = AccentIceBlue
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    val message = when {
                        score >= 4 -> "ممتاز! لديك وعي برلماني رفيع وتفهم عميق لتجربة طفل سيناء."
                        score >= 2 -> "جيد جداً! نوصيك بقراءة قسم البحث التوثيقي لزيادة حصيلتك."
                        else -> "حاول مجدداً لتتعلم أكثر عن برلمان طفل شمال سيناء."
                    }

                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            textAlign = TextAlign.Center,
                            color = OnSurfaceSecondary
                        )
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            currentQuestionIndex = 0
                            selectedOptionIndex = null
                            score = 0
                            quizCompleted = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AccentIceBlue,
                            contentColor = AccentDeepNavy
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("restart_quiz_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "إعادة الاختبار مجدداً", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        if (pastResults.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))

            SectionHeader(title = "سجل نتائجك السابقة")

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(pastResults) { res ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .border(width = 1.dp, color = DarkBorderColor, shape = RoundedCornerShape(12.dp)),
                        colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(12.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "النتيجة: ${res.score} / ${res.totalQuestions}",
                                fontWeight = FontWeight.Bold,
                                color = OnBackgroundLight
                            )
                            Text(
                                text = "النسبة: ${res.percentage.toInt()}%",
                                color = AccentIceBlue,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
