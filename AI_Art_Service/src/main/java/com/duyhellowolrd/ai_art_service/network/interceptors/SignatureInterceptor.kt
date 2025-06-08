package com.duyhellowolrd.ai_art_service.network.interceptors

import android.util.Log
import com.apero.signature.SignatureParser
import com.duyhellowolrd.ai_art_service.AiArtServiceEntry
import com.duyhellowolrd.ai_art_service.network.consts.ServiceConstants
import okhttp3.Interceptor
import okhttp3.Response

class SignatureInterceptor : Interceptor {
    companion object {
        private const val TAG = "SignatureInterceptor"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val signatureData = SignatureParser.parseData(
            AiArtServiceEntry.API_KEY,
            // TODO
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAuVg0t47njOJoF2AtZRj2O7VfPESF4vc1BVmtvI59ZQqlgydmNTddhpKQISMJz94p45aZLoJpQikluV+ywg4a/40UTsPl53nL5nFAnDjCvaeMOYqI74iXoy6nYkF+0yM449qH8kceqLyViZylcetTXU6s/J3DV2eKXo7/eocEef8qtlZlLkFnoppi5UQ5WNZD/qHh9oMknkN/1qYzcckn4pzHmapOAJopLpO9AjudfD9rHD+5Tsg9O3lucnBO4MyZCx5po8K4PX57gvfEmfsSxlC7tQUAcFM5PytxD7j8HwCr1msQTT4otNhjyqPL3opDNgvrtfUhTbhPCEcUh200BQIDAQAB",
            AiArtServiceEntry.timeStamp
        )
        Log.d(TAG, "intercept: signatureData = $signatureData")
        val tokenIntegrity =
            signatureData.tokenIntegrity.ifEmpty { ServiceConstants.NOT_GET_API_TOKEN }
        Log.d(TAG, "intercept: tokenIntegrity = $tokenIntegrity")
        val headers = hashMapOf(
            "Accept" to "application/json",
            "Content-Type" to "application/json",
            "device" to "android",
            "x-api-signature" to signatureData.signature,
            "x-api-timestamp" to signatureData.timeStamp.toString(),
            "x-api-keyid" to signatureData.keyId,
            "x-api-token" to tokenIntegrity,
            "x-api-bundleId" to AiArtServiceEntry.BUNDLE_ID,
            "App-name" to AiArtServiceEntry.APP_NAME,
        )
        Log.d(TAG, "intercept: $headers")
        val requestBuilder = chain.request().newBuilder()
        for ((key, value) in headers) {
            requestBuilder.addHeader(key, value)
        }
        return chain.proceed(requestBuilder.build())
    }
}