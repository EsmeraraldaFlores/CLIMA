@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {
    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Singleton
    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore{
        return FirebaseFirestore.getInstance()
    }
}
