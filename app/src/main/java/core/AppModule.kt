package com.example.clima.view.home

import android.app.Activity
import android.content.Context
import com.example.clima.core.LocationProvider
import com.example.clima.network.ClimaRepository
import com.example.clima.network.WeatherRepository
import com.example.clima.repository.UserRepository
import com.example.clima.network.RealtimeAPI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideContext(@ApplicationContext context: Context): Context = context

    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Singleton
    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("https://api.weatherapi.com/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Singleton
    @Provides
    fun provideRealtimeAPI(retrofit: Retrofit): RealtimeAPI =
        retrofit.create(RealtimeAPI::class.java)

    @Singleton
    @Provides
    fun provideClimaRepository(api: RealtimeAPI): ClimaRepository =
        ClimaRepository(api)

    @Singleton
    @Provides
    fun provideWeatherRepository(api: RealtimeAPI): WeatherRepository =
        WeatherRepository(api)

    @Singleton
    @Provides
    fun provideUserRepository(
        auth: FirebaseAuth,
        db: FirebaseFirestore
    ): UserRepository =
        UserRepository(auth, db)

    @Provides
    fun provideLocationProvider(@ApplicationContext context: Context): LocationProvider =
        LocationProvider(context as Activity)
}
