package com.llsit.joinsphere.feature.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.llsit.joinsphere.core.design.Button
import org.koin.androidx.compose.koinViewModel

private val BLUE = Color(0xFF2563EB)
private val SURFACE = Color(0xFFFFFFFF)
private val SLATE_900 = Color(0xFF0F172A)
private val SLATE_600 = Color(0xFF475569)
private val SLATE_400 = Color(0xFF94A3B8)
private val SLATE_200 = Color(0xFFE2E8F0)
private val WHITE = Color(0xFFFFFFFF)

data class OnboardingStep(
    val title: String,
    val description: String,
    val showSkip: Boolean = true,
    val features: List<String>? = null,
    val illustration: @Composable () -> Unit,
)

private val SCREENS = listOf(
    OnboardingStep(
        title = "Discover local\nadventures",
        description = "Find interesting activities and meetups happening right in your neighborhood.",
        illustration = { PlaceholderIllustration(Color(0xFFDBEAFE)) }
    ),
    OnboardingStep(
        title = "Connect with\nlike-minded people",
        description = "Join groups that share your interests, from hiking to board games and tech talks.",
        illustration = { PlaceholderIllustration(Color(0xFFD1FAE5)) }
    ),
    OnboardingStep(
        title = "Create and host\nyour own events",
        description = "Ready to lead? Organize your own meetup and build your community today.",
        features = listOf("Easy Hosting", "RSVP Tracking", "Chat Groups"),
        illustration = { PlaceholderIllustration(Color(0xFFFEF3C7)) }
    )
)

@Composable
fun OnboardingScreen(
    onComplete: () -> Unit,
    viewModel: OnboardingViewModel = koinViewModel()
) {
    var currentStep by remember { mutableIntStateOf(0) }
    var isDone by remember { mutableStateOf(false) }

    val handleComplete = {
        viewModel.completeOnboarding()
        onComplete()
    }

    if (isDone) {
        OnboardingCompleteScreen(onExplore = handleComplete)
    } else {
        OnboardingFlowContent(
            currentStep = currentStep,
            onNext = {
                if (currentStep < SCREENS.size - 1) {
                    currentStep++
                } else {
                    isDone = true
                }
            },
            onSkip = handleComplete
        )
    }
}

@Composable
private fun OnboardingFlowContent(
    currentStep: Int,
    onNext: () -> Unit,
    onSkip: () -> Unit
) {
    val step = SCREENS[currentStep]
    val isLast = currentStep == SCREENS.size - 1

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SURFACE)
    ) {
        // ── Top bar ──
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProgressDots(count = SCREENS.size, current = currentStep)
            
            if (step.showSkip) {
                TextButton(onClick = onSkip) {
                    Text(
                        text = "Skip",
                        color = SLATE_400,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            } else {
                Spacer(modifier = Modifier.size(48.dp))
            }
        }

        // ── Animated Content ──
        AnimatedContent(
            targetState = currentStep,
            transitionSpec = {
                val direction = if (targetState > initialState) 1 else -1
                slideInHorizontally(
                    animationSpec = tween(300),
                    initialOffsetX = { it * direction }
                ) + fadeIn(animationSpec = tween(300)) togetherWith
                slideOutHorizontally(
                    animationSpec = tween(300),
                    targetOffsetX = { -it * direction }
                ) + fadeOut(animationSpec = tween(300))
            },
            label = "OnboardingTransition",
            modifier = Modifier.weight(1f)
        ) { targetIndex ->
            val targetStep = SCREENS[targetIndex]
            Column(modifier = Modifier.fillMaxSize()) {
                // ── Illustration ──
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(340f / 268f)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    targetStep.illustration()
                }

                // ── Text content ──
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 28.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = targetStep.title,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = SLATE_900,
                        lineHeight = 32.sp,
                        letterSpacing = (-0.6).sp,
                        modifier = Modifier.padding(bottom = 14.dp)
                    )

                    Text(
                        text = targetStep.description,
                        fontSize = 15.sp,
                        lineHeight = 25.sp,
                        color = SLATE_600,
                        modifier = Modifier.padding(bottom = 20.dp)
                    )

                    // Feature chips
                    targetStep.features?.let { features ->
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            features.forEach { feature ->
                                FeatureChip(label = feature)
                            }
                        }
                    }
                }
            }
        }

        // ── Bottom Section (CTA) ──
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 28.dp)
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = onNext,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = if (isLast) "Get Started" else "Continue",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = WHITE
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = WHITE
                    )
                }
            }

            Text(
                text = "${currentStep + 1} of ${SCREENS.size}",
                modifier = Modifier.padding(top = 16.dp),
                fontSize = 12.sp,
                color = SLATE_400,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun OnboardingCompleteScreen(
    onExplore: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SURFACE)
            .padding(horizontal = 28.dp, vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(BLUE),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = WHITE
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "You're all set!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = SLATE_900,
            letterSpacing = (-0.5).sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Welcome to JoinSphere. Your next adventure is waiting.",
            fontSize = 15.sp,
            color = SLATE_600,
            lineHeight = 25.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(36.dp))

        Button(
            onClick = onExplore,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(
                text = "Explore activities →",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = WHITE
            )
        }
    }
}

@Composable
private fun ProgressDots(count: Int, current: Int) {
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        repeat(count) { i ->
            val isActive = i == current
            Box(
                modifier = Modifier
                    .size(if (isActive) 18.dp else 6.dp, 6.dp)
                    .clip(CircleShape)
                    .background(if (isActive) BLUE else SLATE_200)
            )
        }
    }
}

@Composable
private fun FeatureChip(label: String) {
    Surface(
        color = Color(0xFFF1F5F9),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = SLATE_600
        )
    }
}

@Composable
private fun PlaceholderIllustration(color: Color) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(24.dp))
            .background(color)
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FlowRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: @Composable () -> Unit
) {
    androidx.compose.foundation.layout.FlowRow(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        verticalArrangement = verticalArrangement
    ) {
        content()
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingScreenPreview() {
    OnboardingScreen(onComplete = {})
}
