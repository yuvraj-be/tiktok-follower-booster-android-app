package app.witwork.boosterlike.common.di.component

import android.app.Application
import app.witwork.boosterlike.common.di.module.AppModule
import app.witwork.boosterlike.common.di.module.RemoteModule
import app.witwork.boosterlike.common.di.module.RepositoryModule
import app.witwork.boosterlike.presentation.MainFragment
import app.witwork.boosterlike.presentation.boost.BoostFragment
import app.witwork.boosterlike.presentation.buystars.BuyStarsFragment
import app.witwork.boosterlike.presentation.explore.ExploreFragment
import app.witwork.boosterlike.presentation.login.LoginFragment
import app.witwork.boosterlike.presentation.profile.ProfileFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        RemoteModule::class,
        RepositoryModule::class
    ]
)
interface AppComponent {
    fun inject(mainFragment: MainFragment)
    fun inject(boostFragment: BoostFragment)
    fun inject(buyStarsFragment: BuyStarsFragment)
    fun inject(exploreFragment: ExploreFragment)
    fun inject(loginFragment: LoginFragment)
    fun inject(profileFragment: ProfileFragment)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        @BindsInstance
        fun baseUrl(url: String): Builder

        fun build(): AppComponent
    }
}