package com.h2.treebuds.client

import okhttp3.Interceptor
import okhttp3.Response

/**
 * A simple Interceptor that adds an Authorization header to each outbound request
 */
class AuthorizationInterceptor : Interceptor {
  override fun intercept(chain: Interceptor.Chain): Response {
      val original = chain.request()

      // Add request Authorization header
      // TODO: get session from DB
      val sessionId = "2696d0e3-8fe6-448f-b6e5-cbd52353594f-aws-prod"
      val requestBuilder = original.newBuilder()
              .header("Authorization", "Bearer $sessionId")

      val request = requestBuilder.build()
      return chain.proceed(request)
  }
}