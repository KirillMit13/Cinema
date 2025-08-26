package com.example.cinema.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Неоновые цвета
object NeonColors {
    val Primary = Color(0xFF00D4FF)
    val Secondary = Color(0xFFFF0080)
    val Tertiary = Color(0xFF8A2BE2)
    val Background = Color(0xFF0A0A0A)
    val Surface = Color(0xFF1A1A1A)
    val SurfaceVariant = Color(0xFF2A2A2A)
    val TextPrimary = Color(0xFFFFFFFF)
    val TextSecondary = Color(0xFFB3B3B3)
    val GlowBlue = Color(0xFF00D4FF)
    val GlowPink = Color(0xFFFF0080)
    val GlowPurple = Color(0xFF8A2BE2)
}

// Неоновый градиент
val neonGradient = Brush.linearGradient(
    colors = listOf(
        NeonColors.GlowBlue,
        NeonColors.GlowPurple,
        NeonColors.GlowPink
    )
)

// Неоновый градиент для фона
val neonBackgroundGradient = Brush.radialGradient(
    colors = listOf(
        Color(0xFF1A1A1A),
        Color(0xFF0A0A0A)
    )
)

@Composable
fun NeonCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "neon_glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_animation"
    )

    Card(
        modifier = modifier
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = NeonColors.GlowBlue.copy(alpha = glowAlpha),
                spotColor = NeonColors.GlowPink.copy(alpha = glowAlpha)
            )
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        NeonColors.GlowBlue.copy(alpha = glowAlpha),
                        NeonColors.GlowPink.copy(alpha = glowAlpha)
                    )
                ),
                shape = RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = NeonColors.Surface.copy(alpha = 0.9f)
        )
    ) {
        content()
    }
}


@Composable
fun NeonText(
    text: String,
    modifier: Modifier = Modifier,
    style: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.bodyLarge,
    color: Color = NeonColors.TextPrimary
) {
    val infiniteTransition = rememberInfiniteTransition(label = "text_glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "text_glow_animation"
    )

    Text(
        text = text,
        modifier = modifier.shadow(
            elevation = 4.dp,
            ambientColor = NeonColors.GlowBlue.copy(alpha = glowAlpha),
            spotColor = NeonColors.GlowPink.copy(alpha = glowAlpha)
        ),
        style = style.copy(
            fontWeight = FontWeight.Medium,
            color = color
        )
    )
}

@Composable
fun NeonTitle(
    text: String,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "title_glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "title_glow_animation"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(8.dp),
                ambientColor = NeonColors.GlowBlue.copy(alpha = glowAlpha * 0.5f),
                spotColor = NeonColors.GlowPink.copy(alpha = glowAlpha * 0.5f)
            )
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        NeonColors.GlowBlue.copy(alpha = glowAlpha),
                        NeonColors.GlowPurple.copy(alpha = glowAlpha),
                        NeonColors.GlowPink.copy(alpha = glowAlpha)
                    )
                ),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(vertical = 6.dp, horizontal = 8.dp)
    ) {
        NeonText(
            text = text,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            ),
            color = NeonColors.TextPrimary,
            modifier = Modifier.fillMaxWidth()
        )
    }
}





@Composable
fun NeonSurface(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .background(
                brush = neonBackgroundGradient
            )
            .padding(16.dp)
    ) {
        content()
    }
}

