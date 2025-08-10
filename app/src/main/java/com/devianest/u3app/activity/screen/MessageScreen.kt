package com.devianest.u3app.activity.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.devianest.u3app.activity.component.BottomNavigationBar
import com.devianest.u3app.model.ChatMessage
import com.devianest.u3app.model.ChatViewModel
import com.devianest.u3app.model.ChatViewModelFactory
import com.devianest.u3app.model.MessageStatus
import com.devianest.u3app.uiProject.theme.old
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MessageScreen(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    val context = LocalContext.current
    val viewModel: ChatViewModel = viewModel(
        factory = ChatViewModelFactory(context)
    )

    val messages by viewModel.messages.collectAsState()
    val isConnected by viewModel.isConnected.collectAsState()
    val adminRegistered by viewModel.adminRegistered.collectAsState()

    var messageInput by remember { mutableStateOf("") }
    var showSetupDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.statusBarsPadding(),
                title = {
                    Column {
                        Text("Hubungi Admin U3", color = Color.White)
                        Text(
                            text = if (isConnected) "● Admin Online" else "○ Admin Offline",
                            color = if (isConnected) Color.Green else Color.Red,
                            fontSize = 12.sp
                        )
                    }
                },
                backgroundColor = old,
                actions = {
                    IconButton(
                        onClick = { showSetupDialog = true }
                    ) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = "Info Setup",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(currentRoute = currentRoute, onNavigate = onNavigate)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            // Setup Info Card
            if (!adminRegistered) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    backgroundColor = Color(0xFFFFF3E0),
                    elevation = 2.dp
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Text(
                            "⚠️ Admin belum terdaftar",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFF6F00)
                        )
                        Text(
                            "Minta admin untuk chat ke @UnitUsahaUnida_Bot terlebih dahulu",
                            fontSize = 12.sp,
                            color = Color(0xFFFF6F00)
                        )
                    }
                }
            }

            // Messages List
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                reverseLayout = true
            ) {
                if (messages.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "💬 Mulai Percakapan",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Kirim pesan Anda dan admin akan membalas melalui Telegram",
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center,
                                color = Color.Gray
                            )
                        }
                    }
                } else {
                    items(messages.reversed()) { message ->
                        ChatBubble(message)
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }

            Divider()

            // Message Input
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = messageInput,
                    onValueChange = { messageInput = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Tulis pesan untuk admin...") },
                    maxLines = 3
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = {
                        if (messageInput.isNotBlank()) {
                            viewModel.sendUserMessage(messageInput.trim())
                            messageInput = ""
                        }
                    },
                    enabled = messageInput.isNotBlank()
                ) {
                    Icon(
                        Icons.Default.Send,
                        contentDescription = "Kirim",
                        tint = if (messageInput.isNotBlank()) old else Color.Gray
                    )
                }
            }
        }
    }

    // Setup Dialog
    if (showSetupDialog) {
        AlertDialog(
            onDismissRequest = { showSetupDialog = false },
            title = { Text("Setup Admin") },
            text = {
                Text(
                    viewModel.getAdminSetupInstructions(),
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                TextButton(onClick = { showSetupDialog = false }) {
                    Text("OK")
                }
            }
        )
    }
}

@Composable
fun ChatBubble(chat: ChatMessage) {
    val bubbleColor = when {
        chat.isUser -> Color(0xFFE3F2FD) // Biru untuk user
        chat.senderName == "System" -> Color(0xFFFFEBEE) // Merah muda untuk system
        else -> Color(0xFFE8F5E8) // Hijau untuk admin
    }

    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (chat.isUser) Arrangement.End else Arrangement.Start
    ) {
        Card(
            modifier = Modifier.widthIn(0.dp, 300.dp),
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (chat.isUser) 16.dp else 4.dp,
                bottomEnd = if (chat.isUser) 4.dp else 16.dp
            ),
            backgroundColor = bubbleColor,
            elevation = 2.dp
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                // Sender name
                Text(
                    text = chat.senderName,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = when {
                        chat.isUser -> Color(0xFF1565C0)
                        chat.senderName == "System" -> Color(0xFFD32F2F)
                        else -> Color(0xFF2E7D32)
                    }
                )
                Spacer(modifier = Modifier.height(4.dp))

                // Message text
                Text(
                    text = chat.message,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Time and status
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = timeFormat.format(Date(chat.timestamp)),
                        fontSize = 10.sp,
                        color = Color.Gray
                    )

                    if (chat.isUser) {
                        Text(
                            text = when (chat.status) {
                                MessageStatus.SENDING -> "⏳"
                                MessageStatus.SENT -> "✓"
                                MessageStatus.DELIVERED -> "✓✓"
                                MessageStatus.FAILED -> "❌"
                            },
                            fontSize = 10.sp,
                            color = if (chat.status == MessageStatus.FAILED) Color.Red else Color.Gray
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MessageScreenPreview() {
    MessageScreen(
        currentRoute = "message",
        onNavigate = {}
    )
}