package com.h2.treebuds

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.h2.treebuds.client.CisAuthResponse
import com.h2.treebuds.client.CisClient
import kotlinx.android.synthetic.main.activity_tree_buds.*
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class TreeBudsActivity : AppCompatActivity() {

  private lateinit var access_token: String

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_tree_buds)
    setSupportActionBar(toolbar)

    fab.setOnClickListener { view ->
      Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
              .setAction("Action", null).show()
    }
    supportActionBar?.setDisplayHomeAsUpEnabled(true)

    if (intent.hasExtra("access_token")) {
      access_token = intent.getStringExtra("access_token")
    }
  }

  inner class CisLogin : AsyncTask<String, Void, CisAuthResponse>() {

    override fun doInBackground(vararg params: String): CisAuthResponse {
      Log.d("CisLogin:doInBackground", "userName = ${params[0]} password = ${params[1]}")
      val retrofit = Retrofit.Builder()
              .baseUrl("https://ident.familysearch.org/")
              .addConverterFactory(MoshiConverterFactory.create())
              .build()
      val cisClient = retrofit.create(CisClient::class.java)
      val response = cisClient.login(params[0], params[1]).execute()

      val body = response.body()
      if (response.isSuccessful && body != null) {
        return body
      }
      val headers = response.headers()
      Log.d("CisLogin:doInBackground", "headers = $headers")
      Log.d("CisLogin:doInBackground", "body = ${response.body()}")
      return buildTestResponse(response.code())
    }

    private fun buildTestResponse(httpStatusCode: Int): CisAuthResponse {
      return CisAuthResponse("failed", "failed with $httpStatusCode")
    }

    override fun onPostExecute(result: CisAuthResponse) {
      Log.d("CisLogin:onPostExecute", "token_type = ${result.token_type} ")
      val intent = Intent(this@TreeBudsActivity, TreeBudsActivity::class.java)
      intent.putExtra("access_token", result.access_token)
      intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
      startActivity(intent)
    }
  }
}
