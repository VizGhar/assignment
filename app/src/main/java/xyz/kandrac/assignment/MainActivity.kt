package xyz.kandrac.assignment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import xyz.kandrac.assignment.ui.theme.AssignmentTheme
import xyz.kandrac.assignment.widget.InputView
import xyz.kandrac.assignment.widget.PasswordInput

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AssignmentTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier.padding(innerPadding).padding(horizontal = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {

                        val errorText = remember { mutableStateOf("") }
                        val messageText = remember { mutableStateOf("") }
                        val inputText = remember { mutableStateOf("") }
                        val passwordText = remember { mutableStateOf("") }
                        val check = remember { mutableStateOf(false) }


                        OutlinedTextField(errorText.value, { errorText.value = it})
                        OutlinedTextField(messageText.value, { messageText.value = it})

                        InputView(inputText.value, {inputText.value = it}, "Standard", message = messageText.value, errorText = errorText.value.takeIf { it.isNotEmpty() }, error = errorText.value.isNotEmpty(), placeholder = "Placeholder")
                        PasswordInput(passwordText.value, check, {passwordText.value = it}, "Password", message = messageText.value, placeholder = "Placeholder")

                        Button(onClick = { check.value = true }) { Text("Validate Pass") }
                    }
                }
            }
        }
    }
}
