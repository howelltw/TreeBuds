package com.h2.treebuds.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.h2.treebuds.client.AuthorizationInterceptor
import com.h2.treebuds.client.FsSession
import com.h2.treebuds.client.PopClient
import com.h2.treebuds.client.TFClient
import com.h2.treebuds.room.AppDatabase
import com.h2.treebuds.room.TreeBudsEntity
import com.h2.treebuds.room.TreePersonEntity
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class TreeBudsRepository private constructor(private val appDatabase: AppDatabase) {

  fun addTreeBud(treeBud: TreeBudsEntity) {
    appDatabase.treeBudsDao().insertTreeBuds(treeBud)
  }

  fun getTreeBudPersons(pid: String): LiveData<List<TreePersonEntity>> {
    val ONE_WEEK = 7 * 24 * 60 * 60 * 1000

    val httpClient = OkHttpClient.Builder().addInterceptor(AuthorizationInterceptor())

    val retrofit = Retrofit.Builder().client(httpClient.build())
            .baseUrl("https://www.familysearch.org/")
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    val popClient = retrofit.create(PopClient::class.java)
    val tfClient = retrofit.create(TFClient::class.java)

    GlobalScope.launch(Dispatchers.IO) {
      val request = popClient.getTreeBuds(FsSession.userId, pid)
      val response = request.await()
      if (response.isSuccessful) {
        for (summary in response.body()!!.personSummaries) {
          appDatabase.treeBudsDao().insertTreeBuds(TreeBudsEntity(null, pid, summary.personId, summary.recordTypes?.get("NEW_PERSON")!!))
          val tfResponse = tfClient.getTfPersonCard(summary.personId).await()
          val tfBody = tfResponse.body()
          if (tfResponse.isSuccessful && tfBody != null) {
            val treePerson = TreePersonEntity(
                    null,
                    tfBody.id,
                    tfBody.summary.name,
                    tfBody.summary.lifespan,
                    tfBody.summary.gender,
                    tfBody.summary.lifespanBegin?.date?.original,
                    tfBody.summary.lifespanBegin?.place?.original,
                    tfBody.summary.lifespanEnd?.date?.original,
                    tfBody.summary.lifespanEnd?.place?.original,
                    System.currentTimeMillis() + ONE_WEEK)
            appDatabase.treePersonDao().insertTreePerson(treePerson)
          }
        }
        var count = appDatabase.treeBudsDao().getTreeBuds(pid).value.orEmpty().size
        Log.d("getTreeBudPersons", "pid=$pid treeBudsSize=$count")

      } else {
        //Toast.makeText(this, "Error ${response.code()}", Toast.LENGTH_LONG).show
      }
    }

    return appDatabase.treePersonDao().getTreePerson()
  }

  companion object {
    // Singleton instantiation of the repository
    @Volatile private var instance: TreeBudsRepository? = null

    fun getInstance(appDatabase: AppDatabase) =
            instance ?: synchronized(this) {
              instance ?: TreeBudsRepository(appDatabase).also { instance = it }
            }
  }
}