package top.ntutn.zerohelper

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import retrofit2.http.GET

@Keep
data class ApkElement(
    @SerializedName("versionCode")
    val versionCode: Long,
    @SerializedName("versionName")
    val versionName: String,
    @SerializedName("outputFile")
    val outputFile: String,
)

@Keep
data class ApkMetaData(
    @SerializedName("elements")
    val elements: List<ApkElement>)

@Keep
interface CheckUpdateApi {
    @GET("/zerofancy/zerohelper/releases/download/latest/output-metadata.json")
    suspend fun getApkMetaData(): ApkMetaData
}