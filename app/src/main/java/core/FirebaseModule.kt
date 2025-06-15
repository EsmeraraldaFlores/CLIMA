@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {
    @Provides @Singleton fun provideFirebaseAuth() = FirebaseAuth.getInstance()
    @Provides @Singleton fun provideFirebaseFirestore() = FirebaseFirestore.getInstance()
}

