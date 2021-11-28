package top.ntutn.zerohelper

import retrofit2.http.GET

data class ApkElement(
    val versionCode: Long,
    val versionName: String,
    val outputFile: String
)

data class ApkMetaData(
    val elements: List<ApkElement>)

interface CheckUpdateApi {
    @GET("/zerofancy/zerohelper/releases/download/latest/output-metadata.json")
    suspend fun getApkMetaData(): ApkMetaData
}