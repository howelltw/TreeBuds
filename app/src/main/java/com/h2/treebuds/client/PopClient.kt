package com.h2.treebuds.client

import com.h2.treebuds.client.models.Summaries
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * POP client for retrieving TreeBuds
 */
interface PopClient {
  @GET("/service/home/pop/users/{cisUserId}/opportunities/summaries/persons/{ancestorPid}/treebuds")
  fun getTreeBuds(@Path("cisUserId") userId: String,
                  @Path("ancestorPid") ancestorPid: String,
                  @Query("limit") limit: String = "20") : Deferred<Response<Summaries>>
}