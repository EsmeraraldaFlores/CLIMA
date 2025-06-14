package com.example.proyectoshopifyka.core

suspend fun <T> safeCall(block: suspend () -> T) = try {
    ResultWrapper.Success(block())
} catch (e: Exception) {
    ResultWrapper.Error(
        when (e) {
            is retrofit2.HttpException -> Exception("HTTP ${e.code()}: ${e.response()?.errorBody()?.toString()}")
            is java.net.SocketException -> Exception("Tiempo de espera agotado")
            is java.net.UnknownHostException -> Exception("No hay conexion a internet")
            is com.google.firebase.FirebaseException -> Exception("Error de firebase: ${e.message}")
            else -> Exception("Error desconocido: ${e.message}")
        }
    )
}

