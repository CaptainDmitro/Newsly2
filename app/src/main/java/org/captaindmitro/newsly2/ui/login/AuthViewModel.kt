package org.captaindmitro.newsly2.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val auth: FirebaseAuth
) : ViewModel() {

    private var _currentUser: MutableStateFlow<FirebaseUser?> = MutableStateFlow(auth.currentUser)
    val currentUser = _currentUser.asStateFlow()

    private fun signUpWithEmail(email: String, password: String) = auth.createUserWithEmailAndPassword(email, password).apply {
        addOnCompleteListener {
            if (it.isSuccessful) {
                _currentUser.value = auth.currentUser
            } else {
                Log.i("Main", "Error reg ${it.exception}")
            }
        }
    }

    private fun sinInWithEmail(email: String, password: String, action: (String) -> Unit) = auth.signInWithEmailAndPassword(email, password).apply {
        addOnCompleteListener {
            if (it.isSuccessful) {
                _currentUser.value = auth.currentUser
                Log.i("Main", "Successful login")
                action(email)
            } else {
                Log.i("Main", "Error login ${it.exception}")
            }
        }
    }

    private fun sinInWithAnonymously(action: (String) -> Unit) = auth.signInAnonymously().apply {
        addOnCompleteListener {
            if (it.isSuccessful) {
                _currentUser.value = auth.currentUser
                Log.i("Main", "Successful login")
                action("Anonymous")
            } else {
                Log.i("Main", "Error login ${it.exception}")
            }
        }
    }

    fun signIn(login: Login, action: (String) -> Unit) {
        when (login) {
            is Login.Anonymously -> {
                sinInWithAnonymously(action)
            }
            is Login.Email -> {
                sinInWithEmail(login.login, login.password, action)
            }
        }
    }

    fun signUp(login: Login.Email, action: (String) -> Unit) {
        signUpWithEmail(login.login, login.password)
        action(login.login)
    }

    fun signOut(action: () -> Unit) {
        auth.signOut()
        action()
    }

}