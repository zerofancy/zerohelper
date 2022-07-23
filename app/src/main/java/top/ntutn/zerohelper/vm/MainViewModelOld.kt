package top.ntutn.zerohelper.vm

import android.content.Context
import android.os.Environment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import top.ntutn.zerohelper.ApkElement
import top.ntutn.zerohelper.util.UpdateUtil
import java.io.File
import java.util.*

class MainViewModelOld : ViewModel() {
    private val _haveUpdate = MutableLiveData<Pair<Boolean, ApkElement?>>()
    val haveUpdate: LiveData<Pair<Boolean, ApkElement?>>
        get() = _haveUpdate
    private val _downloadId = MutableLiveData<Long>()
    val downloadId: LiveData<Long>
        get() = _downloadId
    private var savedFile: File? = null

    fun checkForUpdate() {
        try {
            UpdateUtil.checkUpdate(viewModelScope) { res ->
                _haveUpdate.value = res
            }
        } catch (e: Exception) {
            _haveUpdate.value = false to null
        }

    }

    fun download(context: Context, targetUrl: String) {
        val fileName = UUID.randomUUID().toString() + ".apk"
        val targetFile =
            File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName)
        _downloadId.value = UpdateUtil.download(context, targetUrl, targetFile)
        savedFile = targetFile
    }

    fun installApk(context: Context) {
        savedFile?.let { UpdateUtil.installAPK(context, it) }

    }
}