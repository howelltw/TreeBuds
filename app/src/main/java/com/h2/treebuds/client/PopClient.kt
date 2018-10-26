package com.h2.treebuds.client

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * CIS cliet to handle FamilySearch authentication
 */
interface PopClient {
  @GET("/service/home/pop/users/{cisUserId}/opportunities/summaries/persons/{ancestorPid}/treebuds")
  fun getTreeBuds(@Path("cisUserId") userId : String,
            @Field("ancestorPid") ancestorPid : String) : Call<CisAuthResponse>
}