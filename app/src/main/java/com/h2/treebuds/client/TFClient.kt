package com.h2.treebuds.client

import com.h2.treebuds.client.models.TfPersonCard
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * TF client, person card
 */
interface TFClient {
  @GET("/tf/person/{ancestorPid}/card")
  fun getTfPersonCard(@Path("ancestorPid") ancestorPid : String) : Call<TfPersonCard>
}