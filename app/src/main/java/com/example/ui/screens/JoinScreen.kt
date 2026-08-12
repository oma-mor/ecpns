package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.automirrored.filled.Launch
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.HowToReg
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.data.ParliamentData
import com.example.data.db.MemberRegistration
import com.example.ui.components.RegistrationCallToActionCard
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JoinScreen(
    viewModel: ParliamentViewModel
) {
    val context = LocalContext.current
    val savedRegistrations by viewModel.memberRegistrations.collectAsState()

    var fullName by remember { mutableStateOf("") }
    var ageStr by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("العريش") }
    var school by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var committee by remember { mutableStateOf("لجنة التعليم والبحث العلمي") }
    var motivation by remember { mutableStateOf("") }

    val cities = listOf("العريش", "بئر العبد", "الشيخ زويد", "رفح", "الحسنة", "نخل")
    val committeesList = listOf(
        "لجنة التعليم والبحث العلمي",
        "لجنة حقوق الطفل والحماية المجتمعية",
        "لجنة الدفاع والأمن القومي للشباب",
        "لجنة الثقافة والإعلام والشباب",
        "لجنة الصحة والبيئة والسكان",
        "لجنة الرياضة والترويح"
    )

    var cityDropdownExpanded by remember { mutableStateOf(false) }
    var committeeDropdownExpanded by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkCanvasBackground)
            .padding(16.dp)
            .testTag("join_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            RegistrationCallToActionCard(
                onOpenFormClick = { viewModel.openOfficialGoogleForm(context) }
            )
        }

        item {
            // Direct Link Box
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(width = 1.dp, color = DarkBorderColor, shape = RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Link,
                                contentDescription = null,
                                tint = AccentIceBlue
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "الرابط المباشر لاستمارة التسجيل",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = OnBackgroundLight
                                )
                            )
                        }

                        OutlinedButton(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("Parliament Form URL", ParliamentData.GOOGLE_FORM_URL)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "تم نسخ رابط التسجيل بنجاح!", Toast.LENGTH_SHORT).show()
                            },
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = AccentIceBlue
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "نسخ الرابط", fontSize = 11.sp, color = AccentIceBlue)
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = ParliamentData.GOOGLE_FORM_URL,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = AccentIceBlue,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }
        }

        item {
            SectionHeader(
                title = "استمارة التسجيل والتسجيل المحلي",
                subtitle = "أدخل بياناتك أولاً لحفظها بالهاتف وتفعيل إرسال الاستمارة"
            )
        }

        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .border(width = 1.dp, color = DarkBorderColor, shape = RoundedCornerShape(20.dp)),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val tfColors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AccentIceBlue,
                        unfocusedBorderColor = DarkBorderColor,
                        focusedTextColor = OnBackgroundLight,
                        unfocusedTextColor = OnBackgroundLight,
                        focusedLabelColor = AccentIceBlue,
                        unfocusedLabelColor = OnSurfaceSecondary,
                        focusedLeadingIconColor = AccentIceBlue,
                        unfocusedLeadingIconColor = OnSurfaceSecondary
                    )

                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = { Text("الاسم الثلاثي أو الرباعي") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_full_name"),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = tfColors
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = ageStr,
                            onValueChange = { ageStr = it },
                            label = { Text("العمر (10-18 سنة)") },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("input_age"),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            colors = tfColors
                        )

                        // City Dropdown
                        ExposedDropdownMenuBox(
                            expanded = cityDropdownExpanded,
                            onExpandedChange = { cityDropdownExpanded = !cityDropdownExpanded },
                            modifier = Modifier.weight(1f)
                        ) {
                            OutlinedTextField(
                                value = city,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("المدينة / المركز") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = cityDropdownExpanded) },
                                modifier = Modifier
                                    .menuAnchor()
                                    .testTag("input_city"),
                                shape = RoundedCornerShape(12.dp),
                                colors = tfColors
                            )
                            ExposedDropdownMenu(
                                expanded = cityDropdownExpanded,
                                onDismissRequest = { cityDropdownExpanded = false }
                            ) {
                                cities.forEach { item ->
                                    DropdownMenuItem(
                                        text = { Text(item) },
                                        onClick = {
                                            city = item
                                            cityDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    OutlinedTextField(
                        value = school,
                        onValueChange = { school = it },
                        label = { Text("المدرسة أو المعهد الأزهري") },
                        leadingIcon = { Icon(Icons.Default.School, contentDescription = null) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_school"),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = tfColors
                    )

                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("رقم الهاتف أو الواتساب") },
                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_phone"),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = tfColors
                    )

                    // Committee Selection
                    ExposedDropdownMenuBox(
                        expanded = committeeDropdownExpanded,
                        onExpandedChange = { committeeDropdownExpanded = !committeeDropdownExpanded }
                    ) {
                        OutlinedTextField(
                            value = committee,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("اللجنة البرلمانية المفضلة") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = committeeDropdownExpanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                                .testTag("input_committee"),
                            shape = RoundedCornerShape(12.dp),
                            colors = tfColors
                        )
                        ExposedDropdownMenu(
                            expanded = committeeDropdownExpanded,
                            onDismissRequest = { committeeDropdownExpanded = false }
                        ) {
                            committeesList.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(item) },
                                    onClick = {
                                        committee = item
                                        committeeDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = motivation,
                        onValueChange = { motivation = it },
                        label = { Text("لماذا ترغب في الانضمام لبرلمان طفل سيناء؟") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .testTag("input_motivation"),
                        maxLines = 4,
                        shape = RoundedCornerShape(12.dp),
                        colors = tfColors
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Button(
                        onClick = {
                            viewModel.submitRegistration(
                                fullName = fullName,
                                ageStr = ageStr,
                                city = city,
                                school = school,
                                phone = phone,
                                committee = committee,
                                motivation = motivation,
                                context = context,
                                onSuccess = {
                                    fullName = ""
                                    ageStr = ""
                                    school = ""
                                    phone = ""
                                    motivation = ""
                                }
                            )
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AccentIceBlue,
                            contentColor = AccentDeepNavy
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("submit_registration_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.HowToReg,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "حفظ وإرسال عبر Google Form",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        if (savedRegistrations.isNotEmpty()) {
            item {
                SectionHeader(
                    title = "طلباتي المحفوظة محلياً",
                    subtitle = "سجل الطلبات السابقة المخزنة بالهاتف"
                )
            }

            items(savedRegistrations) { reg ->
                SavedRegistrationCard(
                    registration = reg,
                    onOpenForm = { viewModel.openOfficialGoogleForm(context) }
                )
            }
        }
    }
}

@Composable
fun SavedRegistrationCard(
    registration: MemberRegistration,
    onOpenForm: () -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("yyyy/MM/dd - hh:mm a", Locale("ar")) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(width = 1.dp, color = DarkBorderColor, shape = RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = AccentIceBlue
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = registration.fullName,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = OnBackgroundLight
                        )
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(AccentNavyContainer)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = registration.city,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = AccentIceBlue
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "اللجنة: ${registration.preferredCommittee}",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = OnSurfaceSecondary
                )
            )

            Text(
                text = "تاريخ التسجيل: ${dateFormat.format(Date(registration.timestamp))}",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 10.sp,
                    color = OnSurfaceSecondary
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = onOpenForm,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Launch,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = AccentIceBlue
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "فتح الاستمارة الرسمية مجدداً", fontSize = 12.sp, color = AccentIceBlue)
            }
        }
    }
}
