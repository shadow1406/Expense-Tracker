package com.prathmesh.moneyx.ui.auth

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.prathmesh.moneyx.comman.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val fireStore: FirebaseFirestore,
    private val pref: SharedPreferences
) : ViewModel() {

    private val _authState = MutableLiveData<Resource<String>>()
    val authState: LiveData<Resource<String>> = _authState

    fun login(email: String, pass: String) = viewModelScope.launch(Dispatchers.IO) {
        firebaseAuth.signInWithEmailAndPassword(email,pass)
            .addOnSuccessListener {
                _authState.postValue(Resource.Success("Logged in Successfully"))
            }
            .addOnFailureListener {
                _authState.postValue(Resource.Error(it.localizedMessage ?: "An Error Occurred. try again."))
            }
    }

    fun signup(username: String, email: String, pass: String) = viewModelScope.launch(Dispatchers.IO) {
        _authState.postValue(Resource.Loading())
        firebaseAuth.createUserWithEmailAndPassword(email,pass)
            .addOnSuccessListener {
                val body = HashMap<String, String>()
                body["username"] = username
                fireStore.collection("users")
                    .document(it.user!!.uid)
                    .set(body)
                    .addOnCompleteListener {
                        _authState.postValue(Resource.Success("User created Successfully"))
                    }
            }
            .addOnFailureListener {
                _authState.postValue(Resource.Error(it.localizedMessage ?: "An Error Occurred. try again."))
            }
    }

    fun savePin(pin: String) = viewModelScope.launch(Dispatchers.IO) {
        pref.edit().putString("pin",pin).apply()
    }

    fun checkPin(pin: String) : Boolean = pref.getString("pin","").equals(pin)

    fun logout() = viewModelScope.launch(Dispatchers.IO) {
        firebaseAuth.signOut()
    }
}