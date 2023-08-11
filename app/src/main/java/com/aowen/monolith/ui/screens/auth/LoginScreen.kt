package com.aowen.monolith.ui.screens.auth

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aowen.monolith.R
import com.aowen.monolith.ui.theme.CoolGrey
import com.aowen.monolith.ui.theme.DiscordBlurple
import com.aowen.monolith.ui.theme.MonolithTheme
import com.aowen.monolith.ui.theme.WarmGrey
import com.aowen.monolith.ui.theme.WarmWhite
import com.aowen.monolith.ui.theme.inputFieldDefaults
import com.aowen.monolith.viewmodel.auth.AuthViewModel
import com.aowen.monolith.viewmodel.auth.LoginUiState
import com.aowen.monolith.viewmodel.auth.PageState

@Composable
internal fun LoginRoute(
    modifier: Modifier = Modifier,
    navigateToHomeScreen: () -> Unit = {},
    viewModel: AuthViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val loginUiState by viewModel.uiState.collectAsState()

    LaunchedEffect(loginUiState.userId) {
        Toast.makeText(
            context,
            "Welcome ${loginUiState.userId}",
            Toast.LENGTH_SHORT
        ).show()
    }

    LoginScreen(
        uiState = loginUiState,
        modifier = modifier,
        setEmail = viewModel::setEmail,
        setPassword = viewModel::setPassword,
        submitLogin = viewModel::submitLogin
    )
}

@Composable
fun LoginScreen(
    uiState: LoginUiState,
    modifier: Modifier = Modifier,
    setEmail: (String) -> Unit = {},
    setPassword: (String) -> Unit = {},
    submitLogin: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .paint(
                painter = painterResource(id = R.drawable.login_bg),
                alignment = Alignment.BottomEnd,
                alpha = 0.2f,
                contentScale = ContentScale.Crop
            )
            .padding(horizontal = 16.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Monolith",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.size(40.dp))
            SignInDiscordButton(
                submitLogin = submitLogin
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                text = "Powered by Omeda.city",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}


@Composable
fun LoginForm(
    uiState: LoginUiState,
    modifier: Modifier = Modifier,
    setEmail: (String) -> Unit,
    setPassword: (String) -> Unit,
    submitLogin: () -> Unit
) {
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = uiState.emailTextField,
        label = { Text(text = "Email") },
        colors = inputFieldDefaults(),
        onValueChange = setEmail
    )
    Spacer(modifier = Modifier.size(8.dp))
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = uiState.passwordTextField,
        label = { Text(text = "Password") },
        colors = inputFieldDefaults(),
        visualTransformation = PasswordVisualTransformation(),
        onValueChange = setPassword
    )

    Spacer(modifier = Modifier.size(16.dp))

    val buttonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.secondary,
        disabledContainerColor = CoolGrey
    )

    ElevatedButton(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(4.dp),
        colors = buttonColors,
        enabled = uiState.emailTextField.isNotEmpty() && uiState.passwordTextField.isNotEmpty(),
        onClick = submitLogin
    ) {
        Text(text = "Login")
    }

    Spacer(modifier = Modifier.size(48.dp))
    TextButton(onClick = {

    }) {
        Text(
            text = "Sign up",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun SignInDiscordButton(
    modifier: Modifier = Modifier,
    submitLogin: () -> Unit = {}
) {

    val buttonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = WarmWhite
    )

    ElevatedButton(
        onClick = submitLogin,
        modifier = modifier,
        colors = buttonColors,
        contentPadding = PaddingValues(24.dp)
    ) {
        Icon(
            modifier = Modifier.size(16.dp),
            painter = painterResource(id = R.drawable.discord_mark_white),
            contentDescription = null
        )
        Spacer(modifier = Modifier.size(12.dp))
        Text(
            text = "Sign in with Discord"
        )
    }

}


@Composable
fun SignUpForm() {

}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun LoginScreenPreview() {
    MonolithTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            LoginScreen(
                uiState = LoginUiState(
                    emailTextField = "test@gmail.com",
                    passwordTextField = "123456",
                )
            )
        }
    }
}