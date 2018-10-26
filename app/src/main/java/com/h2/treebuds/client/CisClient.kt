package com.h2.treebuds.client

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * CIS cliet to handle FamilySearch authentication
 */
interface CisClient {
  @FormUrlEncoded
  @POST("/cis-web/oauth2/v3/token")
  fun login(@Field("username") userId : String,
            @Field("password") password : String,
            @Field("grant_type") grantType : String = "password",
            @Field("client_id") devKey : String = "") : Call<CisAuthResponse>
}