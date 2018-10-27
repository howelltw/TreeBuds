package com.h2.treebuds.client

import com.h2.treebuds.client.models.Summaries
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * CIS cliet to handle FamilySearch authentication
 */
interface PopClient {
  @GET("/service/home/pop/users/{cisUserId}/opportunities/summaries/persons/{ancestorPid}/treebuds")
  fun getTreeBuds(@Path("cisUserId") userId : String,
                  @Path("ancestorPid") ancestorPid : String) : Call<Summaries>
}