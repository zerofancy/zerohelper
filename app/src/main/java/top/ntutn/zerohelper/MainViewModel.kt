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

class MainViewModel: ViewModel() {
    private val _result = MutableLiveData<String>()
    val result: LiveData<String>
        get() = _result

    fun checkForUpdate() {
        viewModelScope.launch {
            _result.value = withContext(Dispatchers.IO) {
                val retrofit = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://github.com/")
                    .build()
                val service = retrofit.create(CheckUpdateApi::class.java)
                service.getApkMetaData().run {
                    "loca: ${BuildConfig.VERSION_CODE}, remote: ${this.elements.firstOrNull()?.versionCode}"
                }
            }?:"nternet error"
        }
    }
}