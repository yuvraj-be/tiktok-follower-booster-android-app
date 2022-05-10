package app.witwork.boosterlike.common.di.module

import app.witwork.boosterlike.data.repository.*
import app.witwork.boosterlike.domain.repositoy.*
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindsUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun bindsAppRepository(appRepositoryImpl: AppRepositoryImpl): AppRepository

    @Binds
    @Singleton
    abstract fun bindsBillingRepository(billingRepositoryImpl: BillingRepositoryImpl): BillingRepository

    @Binds
    @Singleton
    abstract fun bindsFeedRepository(feedRepositoryImpl: FeedRepositoryImpl): FeedRepository

    @Binds
    @Singleton
    abstract fun bindsBoostPackageRepository(boostPackageRepositoryImpl: BoostPackageRepositoryImpl): BoostPackageRepository
}