package com.example.alarmmanager


import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TimePickerLayoutType
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.example.alarmmanager.ui.theme.AlarmManagerTheme
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalTime

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AlarmManagerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @Composable
    fun MainScreen() {
        val context = LocalContext.current
        val notificationScheduler = remember {
            AndroidNotificationScheduler(context)
        }
        val localTime = LocalTime.now()

        var showTimePicker by remember { mutableStateOf(false) }
        val state = rememberTimePickerState()
        val snackState = remember { SnackbarHostState() }


        val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted ->
                if (isGranted) {
                    if (state.hour < localTime.hour) {
                        Toast.makeText(
                            context, "Please select time greater than present time",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        notificationScheduler.schedule(
                            notification = Notification(
                                title = "Food is ready",
                                description = "A courier is coming to you"
                            ),
                            hr = state.hour.toLong() - localTime.hour.toLong(),
                            min = state.minute.toLong() - localTime.minute.toLong()
                        )
                    }
                }
            }
        )
        Column {
            Box(propagateMinConstraints = false) {
                Button(
                    modifier = Modifier.align(Alignment.Center),
                    onClick = { showTimePicker = true }
                ) {
                    Text("Set Time")
                }
                SnackbarHost(hostState = snackState)
            }

            if (showTimePicker) {
                androidx.compose.material3.TimePicker(
                    modifier = Modifier,
                    state = state,
                    layoutType = TimePickerLayoutType.Vertical
                )
            }
            Button(
                onClick = {
                    when (PackageManager.PERMISSION_GRANTED) {
                        ContextCompat.checkSelfPermission(
                            context,
                            android.Manifest.permission.POST_NOTIFICATIONS
                        ) -> {
                            if (state.hour < localTime.hour) {
                                Toast.makeText(
                                    context, "Please select time greater than present time",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }else if(state.hour==localTime.hour && state.minute<=localTime.minute){
                                Toast.makeText(
                                    context, "Please select time greater than present time",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                notificationScheduler.schedule(
                                    notification = Notification(
                                        title = "Food is ready",
                                        description = "A courier is coming to you"
                                    ), hr = state.hour.toLong() - localTime.hour.toLong(),
                                    min = state.minute.toLong() - localTime.minute.toLong()
                                )
                            }
                        }

                        else -> {
                            permissionLauncher.launch(
                                android.Manifest.permission.POST_NOTIFICATIONS
                            )
                        }
                    }
                }
            ) {
                Text(text = "Show notification")
            }
        }
    }

}

