package com.h2.treebuds.client

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

/**
 * A simple Interceptor that adds an Authorization header to each outbound request
 */
class AuthorizationInterceptor : Interceptor {
  override fun intercept(chain: Interceptor.Chain): Response {
      val original = chain.request()

      // Add request Authorization header
      val sessionId = FsSession.sessionId
      val requestBuilder = original.newBuilder()
              .header("Authorization", "Bearer $sessionId")

      val request = requestBuilder.build()
    val response = chain.proceed(request)
    Log.d("AuthorizationInterceptor", "requestUri=${request.url()} httpStatus=${response.code()}")
    return response
  }
}