package com.example.proyectoshopifyka.network

import com.example.proyectoshopifyka.core.ResultWrapper
import com.example.proyectoshopifyka.core.safeCall
import com.example.proyectoshopifyka.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) {
    private val users = db.collection("Users")

    suspend fun login(email: String, pass: String): ResultWrapper<FirebaseUser> = safeCall {
        auth.signInWithEmailAndPassword(email, pass).await().user ?: error("usuario no encontrado")
    }

    suspend fun requestSignUp(email: String, pass: String): ResultWrapper<FirebaseUser> = safeCall {
        auth.createUserWithEmailAndPassword(email, pass).await().user ?: error("No se pudo crear el usuario")
    }

    suspend fun createUser(user: User): ResultWrapper<Void> = safeCall {
        require(user.id.isNotEmpty()) { "Error: userId está vacío" }
        users.document(user.id).set(user).await()
    }

    suspend fun getUser(id: String): ResultWrapper<User> = safeCall {
        users.document(id).get().await().toObject(User::class.java) ?: error("Usuario no encontrado")
    }

    suspend fun updateUser(user: User): ResultWrapper<Void> = safeCall {
        users.document(user.id).set(user).await()
    }

    suspend fun deleteUser(id: String): ResultWrapper<Void> = safeCall {
        users.document(id).delete().await()
    }
}
