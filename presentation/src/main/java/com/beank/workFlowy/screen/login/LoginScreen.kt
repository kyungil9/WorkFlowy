package com.beank.workFlowy.screen.login


import android.content.Intent
import android.os.Build
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.beank.presentation.R
import com.beank.workFlowy.component.BasicButton
import com.beank.workFlowy.component.EmailField
import com.beank.workFlowy.component.IconButton
import com.beank.workFlowy.component.PasswordField
import com.beank.workFlowy.component.VerticalSpacer
import com.beank.workFlowy.component.basicButton
import com.beank.workFlowy.component.fieldModifier
import com.beank.workFlowy.navigation.NavigationItem
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import com.beank.presentation.R.string as AppText

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = hiltViewModel(),
    openPopUpScreen : (String,String) -> Unit,
    openScreen : (String) -> Unit
){
    var user by rememberSaveable { mutableStateOf(Firebase.auth.currentUser) }
    val launcher = rememberFirebaseAuthLauncher(
        onAuthSuccess = { result ->
            if(result.additionalUserInfo!!.isNewUser)
                loginViewModel.initSetting()
            user = result.user
        },
        onAuthError = {
            user = null
        }
    )
    val token = stringResource(R.string.default_web_client_id)
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (user == null){
            Image(painter = painterResource(id = R.drawable.workflowy_foreground), contentDescription = "어플화면",
                modifier = Modifier.size(300.dp,300.dp))
            EmailField(value = loginViewModel.inputEmail, onNewValue = loginViewModel::onEmailChange, Modifier.fieldModifier())
            PasswordField(value = loginViewModel.inputPassword, onNewValue = loginViewModel::onPasswordChange, Modifier.fieldModifier())

            BasicButton(text = AppText.sign_in, modifier = Modifier.basicButton()) {
                loginViewModel.onSignInClick(openPopUpScreen)
            }
            VerticalSpacer(height = 30.dp)
            IconButton(icon = NavigationItem.SIGNUP.icon!!, text = AppText.create_account) {
                openScreen(NavigationItem.SIGNUP.route)
            }
            VerticalSpacer(height = 10.dp)
            IconButton(
                icon = com.google.android.gms.auth.api.R.drawable.googleg_standard_color_18,
                text = AppText.google_login
            ){
                val gso =
                    GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(token)
                        .requestEmail()
                        .build()
                val googleSignInClient = GoogleSignIn.getClient(context,gso)
                launcher.launch(googleSignInClient.signInIntent)
            }
        }else{
            openPopUpScreen(NavigationItem.HOME.route,NavigationItem.LOGIN.route)
        }
    }
}

@Composable
fun rememberFirebaseAuthLauncher(
    onAuthSuccess : (AuthResult) -> Unit,
    onAuthError : (ApiException) -> Unit
) : ManagedActivityResultLauncher<Intent, ActivityResult> {
    val scope = rememberCoroutineScope()
    return rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)!!
            val credential = GoogleAuthProvider.getCredential(account.idToken!!,null)
            scope.launch {
                val authResult = Firebase.auth.signInWithCredential(credential).await()
                onAuthSuccess(authResult)
            }
        }catch (e : ApiException){
            onAuthError(e)
        }

    }
}
