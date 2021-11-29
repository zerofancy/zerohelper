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
        viewModelScope.launch {
            val apkMetaData = withContext(Dispatchers.IO) {
                val retrofit = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://github.com/")
                    .build()
                val service = retrofit.create(CheckUpdateApi::class.java)
                service.getApkMetaData()
            }
            apkMetaData.elements.firstOrNull()?.let {
                _haveUpdate.value = (it.versionCode > BuildConfig.VERSION_CODE) to it
            } ?: kotlin.run {
                _haveUpdate.value = false to null
            }
        }
    }
}