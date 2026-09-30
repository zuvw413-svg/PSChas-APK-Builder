package com.pschas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PSChasTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PSChasApp()
                }
            }
        }
    }
}

@Composable
fun PSChasTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme,
        typography = MaterialTheme.typography,
        content = content
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PSChasApp() {
    val devices = remember {
        listOf(
            Device("PS4-01", "PS4", "PS4", true, "عبدالله", 15.0),
            Device("PS4-02", "PS4", "PS4", false, "", 15.0),
            Device("PS4-03", "PS4", "PS4", true, "أحمد", 18.0),
            Device("PS5-01", "PS5", "PS5", false, "", 25.0),
            Device("PS5-02", "PS5", "PS5", true, "سارة", 30.0),
            Device("PC-01", "PC", "PC", false, "", 12.5),
            Device("PC-02", "PC", "PC", true, "علي", 14.0)
        )
    }

    var selectedTab by remember { mutableIntStateOf(0) }

    val filteredDevices = remember(selectedTab, devices) {
        when (selectedTab) {
            1 -> devices.filter { it.type == "PS4" || it.type == "PS5" }
            2 -> devices.filter { it.type == "PC" }
            else -> devices
        }
    }

    val busyCount = devices.count { it.isBusy }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("PS Chas")
                        Text(
                            text = "$busyCount أجهزة مشغولة الآن",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            TabRow(selectedTabIndex = selectedTab) {
                Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }, text = { Text("الكل") })
                Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }, text = { Text("PS") })
                Tab(selected = selectedTab == 2, onClick = { selectedTab = 2 }, text = { Text("PC") })
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SummaryCard(title = "الإيراد اليوم", value = "2,350 ر.س", color = Color(0xFF0F766E))
                SummaryCard(title = "الديون", value = "420 ر.س", color = Color(0xFF7F1D1D))
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredDevices) { device ->
                    DeviceCard(device = device)
                }
            }
        }
    }
}

@Composable
fun SummaryCard(title: String, value: String, color: Color) {
    Card(
        modifier = Modifier.weight(1f),
        colors = CardDefaults.cardColors(containerColor = color),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(text = title, color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
            Text(text = value, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        }
    }
}

@Composable
fun DeviceCard(device: Device) {
    val accent = when (device.type) {
        "PS5" -> Color(0xFF8B5CF6)
        "PC" -> Color(0xFF3B82F6)
        else -> Color(0xFF10B981)
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF111827)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "${device.type} #${device.id.split("-").last()}",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Box(
                    modifier = Modifier
                        .background(
                            color = if (device.isBusy) Color(0xFFEF4444) else Color(0xFF22C55E),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (device.isBusy) "مشغول" else "متاح",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            if (device.isBusy) {
                Text(text = device.customerName, color = Color(0xFFBFDBFE), fontSize = 13.sp)
                Text(text = "01:42:15", color = Color(0xFF86EFAC), fontWeight = FontWeight.Bold, fontSize = 22.sp)
                Text(text = "${device.rate} ر.س / ساعة", color = Color(0xFFE5E7EB), fontSize = 12.sp)
            } else {
                Text(text = "جاهز للاستخدام", color = Color(0xFF86EFAC), fontSize = 13.sp)
            }

            Button(
                onClick = { },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(
                    imageVector = if (device.isBusy) Icons.Default.MonetizationOn else Icons.Default.PlayArrow,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(text = if (device.isBusy) "إنهاء الجلسة" else "ابدأ الجلسة")
            }
        }
    }
}

data class Device(
    val id: String,
    val name: String,
    val type: String,
    val isBusy: Boolean,
    val customerName: String,
    val rate: Double
)
