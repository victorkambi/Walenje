package kambi.victor.walenje.feature.welcome

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kambi.victor.walenje.core.designsystem.WalenjeText
import kambi.victor.walenje.core.designsystem.icons.WalenjeIcons
import kambi.victor.walenje.core.designsystem.theme.WalenjeTheme
import kotlin.math.abs
import kotlinx.coroutines.launch

@Composable
fun SecureYourWalletScreen(onNavigateToNext: () -> Unit) {
    val coroutineScope = rememberCoroutineScope()

    val translationY = remember { Animatable(0f) }
    var columnHeight by remember { mutableFloatStateOf(0f) }
    var totalDrag by remember { mutableFloatStateOf(0f) }

    val draggableState = rememberDraggableState { dragAmount ->
        coroutineScope.launch {
            totalDrag += dragAmount
            translationY.snapTo(translationY.value + dragAmount)
        }
    }

    val decay = rememberSplineBasedDecay<Float>()

    Scaffold { paddingValues ->
        Column(
            modifier =
                Modifier.onGloballyPositioned { coordinates ->
                        columnHeight = coordinates.size.height.toFloat()
                    }
                    .graphicsLayer { this.translationY = translationY.value }
                    .draggable(
                        draggableState,
                        Orientation.Vertical,
                        onDragStopped = { velocity ->
                            coroutineScope.launch {
                                val decayTarget =
                                    decay.calculateTargetValue(translationY.value, velocity)
                                val dragDistance = abs(totalDrag)
                                val isFlickUp = velocity < 0 && decayTarget < -columnHeight * 0.5f
                                val isSlowDragUp =
                                    totalDrag < 0 && dragDistance > columnHeight * 0.3f

                                if (isFlickUp || isSlowDragUp) {
                                    onNavigateToNext()
                                    translationY.animateDecay(
                                        initialVelocity = velocity,
                                        animationSpec = decay,
                                    )
                                } else {
                                    translationY.animateTo(0f)
                                }

                                totalDrag = 0f // reset
                            }
                        },
                    )
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(
                        start = WalenjeTheme.spacing.margin,
                        top = 64.dp,
                        end = WalenjeTheme.spacing.margin,
                        bottom = 40.dp,
                    ),
        ) {
            Column {
                ProvideTextStyle(WalenjeTheme.typography.display) {
                    Text(
                        text =
                            buildAnnotatedString {
                                append("SECURE\n")
                                append("YOUR\n")
                                append("WALLET\n")
                            },
                    )
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                    ProvideTextStyle(WalenjeTheme.typography.body) {
                        Text(text = "We need to be sure that it’s you that’s accessing your wallet")
                    }
                }
            }
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Button(
                    onClick = { onNavigateToNext() },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                ) {
                    Icon(WalenjeIcons.Lock, contentDescription = null)
                    Spacer(Modifier.padding(WalenjeTheme.spacing.x1))
                    WalenjeText("Proceed to secure Wallet", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
