package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.ResearchTopic
import com.example.ui.components.RegistrationCallToActionCard
import com.example.ui.components.SectionHeader
import com.example.ui.theme.AccentDeepNavy
import com.example.ui.theme.AccentIceBlue
import com.example.ui.theme.AccentNavyContainer
import com.example.ui.theme.DarkBorderColor
import com.example.ui.theme.DarkCanvasBackground
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.OnBackgroundLight
import com.example.ui.theme.OnSurfaceSecondary
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.ParliamentViewModel

@Composable
fun HomeScreen(
    viewModel: ParliamentViewModel,
    onNavigate: (AppScreen) -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val researchTopics by viewModel.filteredResearchTopics.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkCanvasBackground)
            .verticalScroll(scrollState)
            .padding(16.dp)
            .testTag("home_screen")
    ) {
        // Hero Card Banner
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .border(width = 1.dp, color = DarkBorderColor, shape = RoundedCornerShape(24.dp)),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_hero_banner_1786567009465),
                    contentDescription = "برلمان الطفل بشمال سيناء",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, DarkCanvasBackground.copy(alpha = 0.92f))
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(AccentNavyContainer)
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "وثائقي • شمال سيناء",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = AccentIceBlue
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "برلمان الطفل المصري بشمال سيناء",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = OnBackgroundLight,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    )
                    Text(
                        text = "منصة الديمقراطية والقيادة للنشء بأرض الفيروز",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = OnSurfaceSecondary
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Registration Call to Action Card
        RegistrationCallToActionCard(
            onOpenFormClick = { viewModel.openOfficialGoogleForm(context) }
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Quick Stats Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            StatCard(
                number = "15+",
                label = "نواب برلمانيون صغار",
                icon = Icons.Default.Groups,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            StatCard(
                number = "6",
                label = "إدارات شبابية بفرع سيناء",
                icon = Icons.Default.LocationOn,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            StatCard(
                number = "12",
                label = "لجان نوعية متخصصة",
                icon = Icons.Default.AccountBalance,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Research Highlights Section
        SectionHeader(
            title = "البحث التوثيقي الشامل",
            subtitle = "أبرز محطات وتاريخ البرلمان من التأسيس للآن",
            actionText = "عرض الكل",
            onActionClick = { onNavigate(AppScreen.RESEARCH) }
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(researchTopics.take(4)) { topic ->
                ResearchQuickCard(
                    topic = topic,
                    onClick = {
                        viewModel.selectArticle(topic)
                        onNavigate(AppScreen.RESEARCH)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Governor Meetings & Executive Support Card
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .border(width = 1.dp, color = DarkBorderColor, shape = RoundedCornerShape(16.dp))
                .clickable { onNavigate(AppScreen.GALLERY) }
                .testTag("governor_card"),
            colors = CardDefaults.elevatedCardColors(
                containerColor = DarkSurfaceElevated
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_governor_meeting_1786567037771),
                    contentDescription = "لقاء المحافظ",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(12.dp))
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(AccentNavyContainer)
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "دعم الجهاز التنفيذي",
                            color = AccentIceBlue,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "استقبال محافظ شمال سيناء الدائم لنواب البرلمان",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = OnBackgroundLight
                        ),
                        maxLines = 2
                    )
                    Text(
                        text = "جلسات استماع دورية بقاعة ديوان المحافظة بالعريش لاستجابة مطالب أطفال سيناء.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = OnSurfaceSecondary
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Quick Navigation Tiles
        SectionHeader(
            title = "أقسام ودليل التطبيق",
            subtitle = "تصفح أركان برلمان طفل سيناء بسهولة"
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            QuickNavTile(
                title = "اللجان النوعية",
                subtitle = "6 لجان متخصصة",
                icon = Icons.Default.AccountBalance,
                backgroundColor = DarkSurfaceCard,
                modifier = Modifier.weight(1f),
                onClick = { onNavigate(AppScreen.COMMITTEES) }
            )
            QuickNavTile(
                title = "اختبار الثقافة",
                subtitle = "اختبر معلوماتك البرلمانية",
                icon = Icons.Default.School,
                backgroundColor = DarkSurfaceCard,
                modifier = Modifier.weight(1f),
                onClick = { onNavigate(AppScreen.QUIZ) }
            )
        }
    }
}

@Composable
fun StatCard(
    number: String,
    label: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .border(width = 1.dp, color = DarkBorderColor, shape = RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(
            containerColor = DarkSurfaceCard
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = AccentIceBlue,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = number,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = AccentIceBlue
                )
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 10.sp,
                    color = OnSurfaceSecondary
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun ResearchQuickCard(
    topic: ResearchTopic,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(220.dp)
            .clickable { onClick() }
            .clip(RoundedCornerShape(16.dp))
            .border(width = 1.dp, color = DarkBorderColor, shape = RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(AccentNavyContainer)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = topic.category,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = AccentIceBlue
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = topic.title,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = OnBackgroundLight
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = topic.summary,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = OnSurfaceSecondary,
                    fontSize = 11.sp
                ),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun QuickNavTile(
    title: String,
    subtitle: String,
    icon: ImageVector,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .border(width = 1.dp, color = DarkBorderColor, shape = RoundedCornerShape(16.dp))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(AccentIceBlue.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = AccentIceBlue,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = OnBackgroundLight,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = OnSurfaceSecondary,
                        fontSize = 10.sp
                    )
                )
            }
        }
    }
}
