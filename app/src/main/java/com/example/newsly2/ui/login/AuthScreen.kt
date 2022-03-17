package com.example.newsly2.ui.login

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import coil.compose.rememberImagePainter
import com.example.newsly2.R
import com.example.newsly2.navigation.NavDestination

sealed class Login {
    object Anonymously : Login()
    class Email(val login: String, val password: String) : Login()
}

@Composable
fun AuthScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val signIn = authViewModel::signIn
    val signUp = authViewModel::signUp
    val navAction: (String) -> Unit = {
        navController.navigate(
            "${NavDestination.HOME}/$it",
            NavOptions.Builder().setPopUpTo(NavDestination.AUTH, true).build()
        )
    }
    val isLoggedIn = authViewModel.currentUser.collectAsState()

    LaunchedEffect(true) {
        Log.i("Main", "${isLoggedIn.value?.email}")
        isLoggedIn.value?.email?.let { email ->
            if (email.isNotBlank()) navAction(email)
        }
    }

    Surface(
        color = MaterialTheme.colors.surface,
        modifier = Modifier.fillMaxSize()
    ) {
        var loginText by remember { mutableStateOf("") }
        var passwordText by remember { mutableStateOf("") }

        val loginRequiements = !loginText.contains("@") && loginText.isNotEmpty()

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = rememberImagePainter(data = R.mipmap.app_logo_round),
                contentDescription = "",
                modifier = Modifier
                    .padding(top = 24.dp)
                    .size(148.dp)
                    .clip(
                        RoundedCornerShape(24.dp)
                    )
            )

            Column(
                modifier = Modifier.padding(48.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = loginText,
                    onValueChange = { newText -> loginText = newText },
                    label = { Text("Email") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    isError = loginRequiements,
                    modifier = Modifier.testTag("LOGIN_TEXTFIELD")
                )
                Spacer(modifier = Modifier.size(8.dp))
                OutlinedTextField(
                    value = passwordText,
                    onValueChange = { newPassword -> passwordText = newPassword },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Send
                    ),
                    keyboardActions = KeyboardActions(onSend = {
                        signIn(
                            Login.Email(
                                loginText,
                                passwordText,
                            ), navAction
                        )
                    }),
                    modifier = Modifier.testTag("PASSWORD_TEXTFIELD")
                )
                Spacer(modifier = Modifier.size(24.dp))
                Row {
                    Button(onClick = { signIn(Login.Email(loginText, passwordText), navAction) }
                    ) {
                        Text("Login")
                    }
                    Spacer(modifier = Modifier.size(8.dp))
                    Button(onClick = { signUp(Login.Email(loginText, passwordText), navAction) }
                    ) {
                        Text("Registration")
                    }
                }
            }
        }
        Text(
            text = stringResource(id = R.string.continue_without_registration),
            modifier = Modifier
                .wrapContentHeight(Alignment.Bottom)
                .wrapContentWidth(Alignment.CenterHorizontally)
                .padding(bottom = 48.dp)
                .clickable { signIn(Login.Anonymously, navAction) }
        )
    }
}