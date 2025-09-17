package com.example.krishimitrr

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.krishimitrr.ui.theme.KrishiMitrrTheme
import com.example.krishimitrr.ui.theme.PrimaryGreen
import com.example.krishimitrr.ui.theme.WarmOffWhite
import com.example.krishimitrr.ui.theme.DarkGrayText

// Data class for chat messages
data class ChatMessage(
    val id: String = "msg_${System.currentTimeMillis()}_${(0..1000).random()}", // Basic unique ID
    val text: String,
    val sender: Sender,
    val timestamp: Long = System.currentTimeMillis()
)

enum class Sender { USER, BOT }

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun KrishiSaathiScreen() {
    var messageInput by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    val initialMessages = listOf(
        ChatMessage(text = "Hello! I'm here to help. Ask me about crops, weather, or schemes.", sender = Sender.BOT),
        ChatMessage(text = "What to plant this season?", sender = Sender.USER),
        ChatMessage(text = "How to prevent pest X?", sender = Sender.USER)
    )
    val messages = remember { mutableStateListOf(*initialMessages.toTypedArray()) }

    fun sendMessage() {
        if (messageInput.isNotBlank()) {
            messages.add(ChatMessage(text = messageInput, sender = Sender.USER))
            messages.add(ChatMessage(text = "I am processing your query: '${messageInput}'", sender = Sender.BOT))
            messageInput = ""
            keyboardController?.hide()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Krishi Saathi", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryGreen
                )
            )
        },
        containerColor = WarmOffWhite
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                reverseLayout = false
            ) {
                items(messages, key = { it.id }) { message ->
                    ChatMessageItem(message)
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .background(WarmOffWhite),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = messageInput,
                    onValueChange = { messageInput = it },
                    placeholder = { Text("Type your question...") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryGreen,
                        unfocusedBorderColor = DarkGrayText.copy(alpha = 0.4f),
                        cursorColor = PrimaryGreen,
                        focusedTextColor = DarkGrayText,
                        unfocusedTextColor = DarkGrayText
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Send
                    ),
                    keyboardActions = KeyboardActions(onSend = {
                        sendMessage()
                    }),
                    singleLine = false,
                    maxLines = 3
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = { sendMessage() },
                    enabled = messageInput.isNotBlank(),
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(if (messageInput.isNotBlank()) PrimaryGreen else Color.Gray)
                ) {
                    Icon(
                        Icons.Filled.Send,
                        contentDescription = "Send Message",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun ChatMessageItem(message: ChatMessage) {
    val boxAlignment = if (message.sender == Sender.USER) Alignment.CenterEnd else Alignment.CenterStart // Changed to CenterEnd/CenterStart
    val backgroundColor = if (message.sender == Sender.USER) PrimaryGreen else Color.LightGray
    val textColor = if (message.sender == Sender.USER) Color.White else DarkGrayText

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        contentAlignment = boxAlignment // Used corrected alignment
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.8f) 
                .clip(RoundedCornerShape(
                    topStart = if (message.sender == Sender.USER) 16.dp else 4.dp,
                    topEnd = if (message.sender == Sender.USER) 4.dp else 16.dp,
                    bottomStart = 16.dp,
                    bottomEnd = 16.dp
                ))
                .background(backgroundColor)
                .padding(12.dp)
        ) {
            Text(
                text = message.text,
                color = textColor,
                fontSize = 15.sp
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun KrishiSaathiScreenPreview() {
    KrishiMitrrTheme {
        KrishiSaathiScreen()
    }
}
