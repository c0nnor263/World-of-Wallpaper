package com.doodle.core.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.doodle.core.ui.card.CardButton
import com.doodle.core.ui.state.DialogState
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import worldofwallpapers.core.ui.generated.resources.Res
import worldofwallpapers.core.ui.generated.resources.app_name
import worldofwallpapers.core.ui.generated.resources.no
import worldofwallpapers.core.ui.generated.resources.yes

@Composable
fun RequestPermissionDialog(
    modifier: Modifier = Modifier,
    state: DialogState,
    requestTitleMessage: StringResource,
    requestContentMessage: StringResource,
    onRequestPermission: () -> Unit,
    onDismiss: () -> Unit,
) {
//    val requestPermission = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.RequestPermission()
//    ) {
//        onDismiss()
//    }

    AnimatedVisibility(
        modifier = modifier,
        visible = state.isVisible,
        enter = fadeIn(tweenMedium()) + scaleIn(tweenMedium()),
        exit = scaleOut(tweenMedium()) + fadeOut()
    ) {
        Dialog(
            onDismissRequest = onDismiss, properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            )
        ) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Text(
                        text = stringResource(requestTitleMessage),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))


                    Text(
                        text = stringResource(
                            requestContentMessage,
                            stringResource(Res.string.app_name)
                        ),
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                        maxLines = 5
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        CardButton(onClick = {
                            onRequestPermission()
                        }
                        ) {
                            Text(
                                stringResource(Res.string.yes),
                                style = MaterialTheme.typography.titleLarge,
                            )
                        }

                        Spacer(
                            Modifier.width(8.dp)
                        )

                        CardButton(onClick = onDismiss) {
                            Text(
                                stringResource(Res.string.no),
                                style = MaterialTheme.typography.titleLarge,
                            )
                        }
                    }
                }
            }
        }
    }
}


