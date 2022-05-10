package app.witwork.boosterlike.common.di.module

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import com.steve.utilities.core.helper.Config
import dagger.Module
import dagger.Provides
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class AppModule {
    @Provides
    @Singleton
    fun provideContextApplication(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Config.provideGson()
    }

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return Config.provideHttpLoggingInterceptor()
    }

    @Provides
    @Singleton
    fun provideRetrofit(baseUrl: String, gson: Gson, logger: HttpLoggingInterceptor, context: Context): Retrofit {
        val version = "/v1/api/"
        return Config.provideRetrofit(baseUrl.plus(version), gson, logger, context = context)
    }
}