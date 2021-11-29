package top.ntutn.zerohelper

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainViewModel : ViewModel() {
    private val _haveUpdate = MutableLiveData<Pair<Boolean, ApkElement?>>()
    val haveUpdate: LiveData<Pair<Boolean, ApkElement?>>
        get() = _haveUpdate

    fun checkForUpdate() {
        UpdateUtil.checkUpdate(viewModelScope) { res ->
            _haveUpdate.value = res
        }
    }
}