package com.h2.treebuds.client

import com.h2.treebuds.client.models.TfPersonCard
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * TF client, person card
 */
interface TFClient {
  @GET("/tf/person/{ancestorPid}/card")
  fun getTfPersonCard(@Path("ancestorPid") ancestorPid : String) : Deferred<Response<TfPersonCard>>
}