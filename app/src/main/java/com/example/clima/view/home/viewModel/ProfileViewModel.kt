package com.example.clima.view.home.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clima.repository.UserRepository
import com.example.clima.core.ResultWrapper
import com.example.clima.model.User
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: UserRepository
): ViewModel(){
    private val _loaderState = MutableLiveData<Boolean>()
    val loaderState: LiveData<Boolean>
        get() = _loaderState

    private val _userInfo = MutableLiveData<User>()
    val userInfo: LiveData<User>
        get() = _userInfo

    private val _userDelete = MutableLiveData<Boolean>()
    val userDelete: LiveData<Boolean>
        get() = _userDelete

    private val _navUpdate = MutableLiveData<Boolean>()
    val navUpdate: LiveData<Boolean>
        get() = _navUpdate

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    fun getUserInfo(){
        _loaderState.value = true
        viewModelScope.launch {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
                ?: return@launch
            when (val result = repository.getUser(userId)) {
                is ResultWrapper.Success -> {
                    _loaderState.value = false
                    _userInfo.value = result.data
                }
                is ResultWrapper.Error -> {
                    _loaderState.value = false
                    val errorMessage = result.exception.message?: "Error desconocido"

                }
            }
        }
    }

    fun deleteUser(userId: String) {
        _loaderState.value = true
        viewModelScope.launch {
            val user = FirebaseAuth.getInstance().currentUser
            user?.let {
                when (val result = repository.deleteUser(userId)) {
                    is ResultWrapper.Success -> {
                        user.delete()
                            .addOnSuccessListener{
                                _loaderState.value = false
                                _userDelete.value = true
                            }
                            .addOnFailureListener{ exception ->
                                _loaderState.value = false
                                _errorMessage.value = exception.message ?: "Error al eliminar el usuario"
                            }
                    }
                    is ResultWrapper.Error -> {
                        _loaderState.value = false
                        val errorMessage = result.exception.message ?: "Error desconocido"
                    }
                }
            }
        }
    }
}