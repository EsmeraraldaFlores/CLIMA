package com.example.clima.view.home.viewModel


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.clima.model.User
import com.example.clima.network.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UpdatePersonalInfoViewModel @Inject constructor(
    private val repository: UserRepository
): ViewModel() {
    private val _loaderState = MutableLiveData<Boolean>()
    val loaderState: LiveData<Boolean>
        get() = _loaderState

    private val db = FirebaseFirestore.getInstance()

    private val _userInfo = MutableLiveData<User>()
    val userInfo: LiveData<User>
        get() = _userInfo

    private val _updateSuccess = MutableLiveData<Boolean>()
    val updateSuccess: LiveData<Boolean>
        get() = _updateSuccess

    private val _operationSuccess = MutableLiveData<Boolean>()
    val operationSuccess: LiveData<Boolean>
        get() = _operationSuccess


    fun getUserInfo(userId: String){
        _loaderState.value = true
        val userRef = FirebaseFirestore.getInstance().collection("Users").document(userId)

        userRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    _loaderState.value = false
                    _userInfo.value = document.toObject(User::class.java)
                }
            }
            .addOnFailureListener { exception ->
                Log.w("Firestore", "Error al obtener datos del usuario", exception)
            }
    }

    fun updateUserInfo(userId: String, user: User) {
        _loaderState.value = false
        val userRef = FirebaseFirestore.getInstance().collection("Users").document(userId)

        userRef.set(user)

            .addOnSuccessListener {
                _loaderState.value = false
                _operationSuccess.value = true
                Log.d("Firestore", "Datos actualizados correctamente")
            }
            .addOnFailureListener { exception ->
                _loaderState.value = false
                _operationSuccess.value = false
                Log.w("Firestore", "Error al actualizar datos", exception)
            }
    }
}