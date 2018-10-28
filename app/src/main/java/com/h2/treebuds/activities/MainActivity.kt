package com.h2.treebuds.activities

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.h2.treebuds.R
import com.h2.treebuds.client.CisClient
import com.h2.treebuds.client.FsSession
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    setSupportActionBar(toolbar)

    login_button_login.setOnClickListener {
      if (username_edittext_login.text.isNotBlank() &&
              password_edittext_login.text.isNotBlank()) {
        CisLogin().execute(username_edittext_login.text.toString(), password_edittext_login.text.toString())
      }
    }
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    // Inflate the menu; this adds items to the action bar if it is present.
    menuInflater.inflate(R.menu.menu_main, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    return when (item.itemId) {
      R.id.action_settings -> true
      else -> super.onOptionsItemSelected(item)
    }
  }

  inner class CisLogin : AsyncTask<String, Void, Int>() {

    override fun doInBackground(vararg params: String): Int {
      Log.d("CisLogin:doInBackground", "userName = ${params[0]} password = ${params[1]}")

      val retrofit = Retrofit.Builder()
              .baseUrl("https://ident.familysearch.org/")
              .addConverterFactory(MoshiConverterFactory.create().asLenient())
              .build()
      val cisClient = retrofit.create(CisClient::class.java)
      val cisResponse = cisClient.login(params[0], params[1]).execute()
      Log.d("CisLogin:doInBackground", "httpStatus = ${cisResponse.code()}")

      if (cisResponse.isSuccessful && cisResponse.body() != null) {
        val sessionId = cisResponse.body()!!.access_token
        FsSession.sessionId = sessionId

        val cisUserResponse = cisClient.getUser(sessionId).execute()

        if (cisUserResponse.isSuccessful && cisUserResponse.body() != null) {
          FsSession.userId = cisUserResponse.body()!!.users[0].id
        }
        return cisUserResponse.code()
      }

      return cisResponse.code()
    }

    override fun onPostExecute(result: Int) {
      if (result == 200) {
        val intent = Intent(this@MainActivity, TreeBudsActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
      }
    }
  }
}
