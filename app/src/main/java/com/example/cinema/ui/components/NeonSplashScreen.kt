package com.example.cinema.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun NeonSplashScreen(
    onSplashComplete: () -> Unit
) {
    var startAnimation by remember { mutableStateOf(false) }
    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 2000),
        label = "splash_alpha"
    )

    val scaleAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.3f,
        animationSpec = tween(durationMillis = 1000),
        label = "splash_scale"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "splash_glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "splash_glow_animation"
    )

    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(3000L)
        onSplashComplete()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        NeonColors.Background,
                        Color(0xFF000000)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Animated background particles
        repeat(20) { index ->
            val particleAlpha by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 2000 + (index * 100),
                        easing = LinearEasing
                    ),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "particle_$index"
            )

            Box(
                modifier = Modifier
                    .size(4.dp)
                    .offset(
                        x = (index * 50).dp,
                        y = (index * 30).dp
                    )
                    .background(
                        color = NeonColors.Primary.copy(alpha = particleAlpha * 0.5f),
                        shape = RoundedCornerShape(2.dp)
                    )
            )
        }

        // Main content
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo with glow effect
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .scale(scaleAnim.value)
                    .shadow(
                        elevation = 20.dp,
                        shape = RoundedCornerShape(60.dp),
                        ambientColor = NeonColors.GlowBlue.copy(alpha = glowAlpha),
                        spotColor = NeonColors.GlowPink.copy(alpha = glowAlpha)
                    )
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                NeonColors.Primary.copy(alpha = 0.8f),
                                NeonColors.Secondary.copy(alpha = 0.6f),
                                NeonColors.Tertiary.copy(alpha = 0.4f)
                            )
                        ),
                        shape = RoundedCornerShape(60.dp)
                    )
                    .clip(RoundedCornerShape(60.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Movie,
                    contentDescription = "Cinema",
                    modifier = Modifier.size(60.dp),
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // App title with glow
            Text(
                text = "NEON CINEMA",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    color = NeonColors.TextPrimary
                ),
                modifier = Modifier.shadow(
                    elevation = 8.dp,
                    ambientColor = NeonColors.GlowBlue.copy(alpha = glowAlpha),
                    spotColor = NeonColors.GlowPink.copy(alpha = glowAlpha)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Subtitle
            Text(
                text = "Киберпанк кинотеатр будущего",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium,
                    color = NeonColors.TextSecondary
                ),
                modifier = Modifier.shadow(
                    elevation = 4.dp,
                    ambientColor = NeonColors.GlowPurple.copy(alpha = glowAlpha * 0.5f)
                )
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Loading indicator
            Box(
                modifier = Modifier
                    .width(200.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(NeonColors.SurfaceVariant)
            ) {
                val progressAnim by infiniteTransition.animateFloat(
                    initialValue = 0f,
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(2000, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart
                    ),
                    label = "progress_animation"
                )

                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(progressAnim)
                        .shadow(
                            elevation = 4.dp,
                            ambientColor = NeonColors.GlowBlue.copy(alpha = glowAlpha),
                            spotColor = NeonColors.GlowPink.copy(alpha = glowAlpha)
                        )
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    NeonColors.GlowBlue,
                                    NeonColors.GlowPurple,
                                    NeonColors.GlowPink
                                )
                            ),
                            shape = RoundedCornerShape(2.dp)
                        )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Loading text
            Text(
                text = "Загрузка...",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = NeonColors.TextSecondary
                ),
                modifier = Modifier.shadow(
                    elevation = 2.dp,
                    ambientColor = NeonColors.GlowBlue.copy(alpha = glowAlpha * 0.3f)
                )
            )
        }
    }
}
