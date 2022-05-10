package com.steve.utilities.core.helper

import android.content.Context
import android.provider.Settings
import android.util.Log
import com.google.gson.*
import com.steve.utilities.core.BuildConfig
import com.steve.utilities.core.extensions.bodyToString
import com.steve.utilities.core.extensions.hash256
import com.steve.utilities.core.extensions.mergeJsonObject
import github.nisrulz.easydeviceinfo.base.EasyDeviceMod
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject

object Config {
    fun provideGson(): Gson {
        return GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()
    }

    fun modifyFormBody(context: Context) = object : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val unixtime = System.currentTimeMillis() / 1000L
            val tmp: String = "$unixtime|shared_secret_key"
            val hash = tmp.hash256()
            var request: Request = chain.request()

            val postBodyString: String = request.body?.bodyToString().let {
                if (it.isNullOrBlank() || it.isNullOrEmpty() || it.isEmpty()) "{}" else it
            }

            val easyDeviceMod = EasyDeviceMod(context)
            val previousFormBody = JsonParser().parse(postBodyString).asJsonObject
            val packageName = context.packageManager.getPackageInfo(context.packageName, 0).packageName
            val userId: String? = context.getSharedPreferences("APP", Context.MODE_PRIVATE).getString("OWNER_USER_ID", "")
            val jsonObject = JsonObject().apply {
                addProperty("hash", hash)
                addProperty("time", unixtime.toString())
                addProperty("imei", Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID))
                addProperty("screen_display_id", easyDeviceMod.screenDisplayID)
                addProperty("model", easyDeviceMod.model)
                addProperty("manufacturer", easyDeviceMod.manufacturer)
                addProperty("os_codename", easyDeviceMod.osCodename)
                addProperty("os_version", easyDeviceMod.osVersion)
                addProperty("product", easyDeviceMod.product)
                addProperty("hardware", easyDeviceMod.hardware)
                addProperty("display_version", easyDeviceMod.displayVersion)
                addProperty("bundleId", context.packageName)
                addProperty("versionCode", context.packageManager.getPackageInfo(context.packageName, 0).versionCode.toString() ?: "")
                addProperty("versionName", context.packageManager.getPackageInfo(context.packageName, 0).versionName ?: "")
                addProperty("packageName", packageName)

                //if login, remove userid
                if (!request.url.toString().contains("login")) {
                    addProperty("userid", userId)
                    addProperty("userId", userId)
                }
            }

            val formBody: RequestBody = RequestBody.create(
                "application/json; charset=utf-8".toMediaType(),
                (previousFormBody.mergeJsonObject(jsonObject)).toString()
            )

            request = request.newBuilder().post(formBody).build()
            return chain.proceed(request)
        }
    }

    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        val level = if (BuildConfig.DEBUG)
            HttpLoggingInterceptor.Level.BODY
        else
            HttpLoggingInterceptor.Level.NONE
        return logging.setLevel(level)
    }

    fun provideRetrofit(url: String, gson: Gson, vararg interceptor: Interceptor, context: Context): Retrofit {
        val builder = UnsafeOkHttpClient.getUnsafeOkHttpClientBuilder()
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)

        builder.addInterceptor(modifyFormBody(context))
        interceptor.forEach {
            builder.addInterceptor(it)
        }

        val okHttpClient = builder.build()
        return Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
    }
}