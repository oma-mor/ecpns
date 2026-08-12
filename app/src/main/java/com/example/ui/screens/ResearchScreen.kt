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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.automirrored.filled.Launch
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ResearchTopic
import com.example.ui.theme.AccentDeepNavy
import com.example.ui.theme.AccentIceBlue
import com.example.ui.theme.AccentNavyContainer
import com.example.ui.theme.DarkBorderColor
import com.example.ui.theme.DarkCanvasBackground
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.OnBackgroundLight
import com.example.ui.theme.OnSurfaceSecondary
import com.example.ui.viewmodel.ParliamentViewModel

@Composable
fun ResearchScreen(
    viewModel: ParliamentViewModel
) {
    val context = LocalContext.current
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val researchTopics by viewModel.filteredResearchTopics.collectAsState()
    val savedArticles by viewModel.savedArticles.collectAsState()

    val categories = listOf(
        "الكل",
        "التأسيس والنشأة",
        "الأهداف والمبادئ",
        "تمكين الشباب والقيادة",
        "الانتخابات والتنظيم",
        "اللقاءات الرسمية",
        "المبادرات والإنجازات",
        "التحديات والحلول",
        "الإنجازات الوطنية",
        "الدعوة والمشاركة"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkCanvasBackground)
            .padding(16.dp)
            .testTag("research_screen")
    ) {
        // Search Input
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.setSearchQuery(it) },
            placeholder = { Text("بحث في تاريخ وأنشطة برلمان طفل سيناء...", color = OnSurfaceSecondary) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "بحث",
                    tint = AccentIceBlue
                )
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("search_input"),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AccentIceBlue,
                unfocusedBorderColor = DarkBorderColor,
                focusedContainerColor = DarkSurfaceCard,
                unfocusedContainerColor = DarkSurfaceCard,
                focusedTextColor = OnBackgroundLight,
                unfocusedTextColor = OnBackgroundLight
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Category Chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(categories) { cat ->
                val isSelected = cat == selectedCategory
                FilterChip(
                    selected = isSelected,
                    onClick = { viewModel.setSelectedCategory(cat) },
                    label = {
                        Text(
                            text = cat,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = AccentNavyContainer,
                        selectedLabelColor = AccentIceBlue,
                        containerColor = DarkSurfaceCard,
                        labelColor = OnSurfaceSecondary
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = isSelected,
                        borderColor = DarkBorderColor,
                        selectedBorderColor = AccentIceBlue
                    ),
                    modifier = Modifier.testTag("chip_$cat")
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Results Count
        Text(
            text = "نتائج البحث الأكاديمي والتوثيقي (${researchTopics.size} مادة توثيقية)",
            style = MaterialTheme.typography.labelMedium.copy(
                color = OnSurfaceSecondary
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Article List
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(researchTopics) { topic ->
                val isSaved = savedArticles.any { it.articleId == topic.id }
                ResearchArticleCard(
                    topic = topic,
                    isSaved = isSaved,
                    onBookmarkToggle = { viewModel.toggleSaveArticle(topic) },
                    onOpenForm = { viewModel.openOfficialGoogleForm(context) }
                )
            }
        }
    }
}

@Composable
fun ResearchArticleCard(
    topic: ResearchTopic,
    isSaved: Boolean,
    onBookmarkToggle: () -> Unit,
    onOpenForm: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = 1.dp,
                color = if (expanded) AccentIceBlue else DarkBorderColor,
                shape = RoundedCornerShape(16.dp)
            )
            .testTag("article_card_${topic.id}"),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(AccentNavyContainer)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = topic.category,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = AccentIceBlue
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = topic.dateOrEra,
                        fontSize = 10.sp,
                        color = OnSurfaceSecondary
                    )
                    IconButton(onClick = onBookmarkToggle) {
                        Icon(
                            imageVector = if (isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "حفظ",
                            tint = if (isSaved) AccentIceBlue else OnSurfaceSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = topic.title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = OnBackgroundLight
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = topic.summary,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = OnSurfaceSecondary,
                    lineHeight = 20.sp
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Expandable Content
            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = 8.dp)) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(DarkBorderColor)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = topic.fullContent,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            lineHeight = 22.sp,
                            color = OnBackgroundLight
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "حقائق ومعطيات أساسية:",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = AccentIceBlue
                        )
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    topic.keyFacts.forEach { fact ->
                        Row(
                            verticalAlignment = Alignment.Top,
                            modifier = Modifier.padding(vertical = 3.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = AccentIceBlue,
                                modifier = Modifier
                                    .size(16.dp)
                                    .padding(top = 2.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = fact,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = OnBackgroundLight
                                )
                            )
                        }
                    }

                    if (topic.id == "participation_call") {
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = onOpenForm,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AccentIceBlue,
                                contentColor = AccentDeepNavy
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("open_form_from_research_btn")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Launch,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "فتح استمارة التسجيل الرسمية الآن",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (expanded) "طي البحث والتفاصيل" else "قراءة البحث والتفاصيل بالكامل",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = AccentIceBlue,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = AccentIceBlue
                )
            }
        }
    }
}
