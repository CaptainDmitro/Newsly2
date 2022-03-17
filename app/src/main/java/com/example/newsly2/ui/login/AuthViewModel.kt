package com.example.newsly2.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow

class AuthViewModel : ViewModel() {

    private val auth = Firebase.auth

    val currentUser = MutableStateFlow(auth.currentUser)

    fun signUpWithEmail(email: String, password: String) = auth.createUserWithEmailAndPassword(email, password).apply {
        addOnCompleteListener {
            if (it.isSuccessful) {
                currentUser.value = auth.currentUser
                Log.i("Main", "Successful reg")
            } else {
                Log.i("Main", "Error reg ${it.exception}")
            }
        }
    }

    fun sinInWithEmail(email: String, password: String, action: (String) -> Unit) = auth.signInWithEmailAndPassword(email, password).apply {
        addOnCompleteListener {
            if (it.isSuccessful) {
                currentUser.value = auth.currentUser
                Log.i("Main", "Successful login")
                action(email)
            } else {
                Log.i("Main", "Error login ${it.exception}")
            }
        }
    }

    fun sinInWithAnonymously(action: (String) -> Unit) = auth.signInAnonymously().apply {
        addOnCompleteListener {
            if (it.isSuccessful) {
                currentUser.value = auth.currentUser
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
        currentUser.value = null
        action()
    }

}