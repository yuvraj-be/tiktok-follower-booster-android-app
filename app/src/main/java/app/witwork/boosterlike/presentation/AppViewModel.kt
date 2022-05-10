package app.witwork.boosterlike.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.witwork.boosterlike.domain.model.Config
import app.witwork.boosterlike.domain.model.Login

class AppViewModel : ViewModel() {
    val profile: MutableLiveData<Login> = MutableLiveData()
    val config: MutableLiveData<Config> = MutableLiveData()
}