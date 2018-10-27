package com.h2.treebuds.client

import com.h2.treebuds.client.models.CisAuthResponse
import retrofit2.Call
import retrofit2.http.*

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

  @GET("/cis-public-api/v4/user")
  fun getUser(@Query("sessionId") sessionId : String) :Call<String>
}