package app.witwork.boosterlike.common.di.module

import app.witwork.boosterlike.data.remote.*
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class RemoteModule {

    @Provides
    @Singleton
    fun provideUserService(retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }

    @Provides
    @Singleton
    fun provideAppService(retrofit: Retrofit): AppService {
        return retrofit.create(AppService::class.java)
    }

    @Provides
    @Singleton
    fun provideFeedService(retrofit: Retrofit): FeedService {
        return retrofit.create(FeedService::class.java)
    }

    @Provides
    @Singleton
    fun provideBoostPackageService(retrofit: Retrofit): BoostPackageService {
        return retrofit.create(BoostPackageService::class.java)
    }
}