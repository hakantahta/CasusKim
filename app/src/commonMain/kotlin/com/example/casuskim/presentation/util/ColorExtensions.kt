package com.example.casuskim.presentation.util

import androidx.compose.ui.graphics.Color

fun String.toColor(): Color {
    return when (this) {
        "#FF6B6B" -> Color(0xFFFF6B6B)
        "#4ECDC4" -> Color(0xFF4ECDC4)
        "#45B7D1" -> Color(0xFF45B7D1)
        "#96CEB4" -> Color(0xFF96CEB4)
        "#FFEAA7" -> Color(0xFFFFEAA7)
        "#DDA0DD" -> Color(0xFFDDA0DD)
        "#98D8C8" -> Color(0xFF98D8C8)
        "#F7DC6F" -> Color(0xFFF7DC6F)
        else -> Color.Black
    }
}
