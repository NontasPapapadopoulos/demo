package com.example.demo

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import com.example.demo.ui.theme.DemoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var mediaPlayer: MediaPlayer
    private val viewModel: DemoViewModel by viewModels()
    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mediaPlayer = MediaPlayer.create(this, R.raw.keypress)

        setContent {
            DemoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val state by viewModel.uiState.collectAsState()

                    val focusRequester = remember { FocusRequester() }
                    val keyboardController = LocalSoftwareKeyboardController.current

                    val text by remember {
                        mutableStateOf(state.text1)
                    }

                    Box(
                        contentAlignment = Alignment.Center
                    ) {


                        OutlinedTextField(
                            value = state.text1,
                            onValueChange = { newValue ->
                                viewModel.add(Event.TextChanged(newValue))
                            },
                            modifier = Modifier
                                .focusRequester(focusRequester)
                        )

                        LaunchedEffect(Unit) {
                            focusRequester.requestFocus()
                            keyboardController?.hide()
                        }

                    }
                }
            }
        }
    }



    override fun onDestroy() {
        mediaPlayer.release()
        super.onDestroy()
    }



    @SuppressLint("RestrictedApi")
    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (event?.action == KeyEvent.ACTION_DOWN) {
            playSound()
        }
        return super.dispatchKeyEvent(event)
    }

    private fun playSound() {
        if (::mediaPlayer.isInitialized) {
            mediaPlayer.start()
        }
    }

}



@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DemoTheme {
        Greeting("Android")
    }
}

