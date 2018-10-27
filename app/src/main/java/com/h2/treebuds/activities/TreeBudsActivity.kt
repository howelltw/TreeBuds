package com.h2.treebuds.activities

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.h2.treebuds.R
import com.h2.treebuds.client.AuthorizationInterceptor
import com.h2.treebuds.client.FsSession
import com.h2.treebuds.client.PopClient
import com.h2.treebuds.client.models.PersonSummary
import com.h2.treebuds.client.models.Summaries
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_tree_buds.*
import kotlinx.android.synthetic.main.content_tree_buds.*
import kotlinx.android.synthetic.main.tree_bud_row.view.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class TreeBudsActivity : AppCompatActivity() {

  val groupAdapter = GroupAdapter<ViewHolder>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_tree_buds)
    setSupportActionBar(toolbar)

    recyclerview_gettreebuds.layoutManager = LinearLayoutManager(this)
    recyclerview_gettreebuds.adapter = groupAdapter

    fab.setOnClickListener { view ->
      Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
              .setAction("Action", null).show()
    }
    supportActionBar?.setDisplayHomeAsUpEnabled(false)

    button_gettreebuds.setOnClickListener {
      if (edittext_ancestorpid.text.isNotBlank()) {
        GetTreeBuds().execute(edittext_ancestorpid.text.toString())
      }
      else {
        Toast.makeText(this, "Please enter a valid FamilySearch Person ID", Toast.LENGTH_LONG).show()
      }
    }
  }

  class TreeBudItem(val personSummary: PersonSummary): Item<ViewHolder>() {
    override fun getLayout(): Int {
      Log.d("TreeBudItem:getLayout", "Made it here")
      return R.layout.tree_bud_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
      Log.d("TreeBudItem:bind", "personSummary=$personSummary")
      viewHolder.itemView.textView_treePid.text = personSummary.personId
      viewHolder.itemView.textView_personas.text = personSummary.recordTypes!!.get("NEW_PERSON").toString()
    }
  }

  inner class GetTreeBuds : AsyncTask<String, Void, Summaries>() {

    override fun doInBackground(vararg params: String): Summaries {
      Log.d("GetTreeBuds:doInBackground", "ancestorPid = ${params[0]}")

      val httpClient = OkHttpClient.Builder().addInterceptor(AuthorizationInterceptor())

      val retrofit = Retrofit.Builder().client(httpClient.build())
              .baseUrl("https://www.familysearch.org/")
              .addConverterFactory(MoshiConverterFactory.create())
              .build()
      val popClient = retrofit.create(PopClient::class.java)
      val response = popClient.getTreeBuds(FsSession.userId, params[0]).execute()

      val body = response.body()
      if (response.isSuccessful && body != null) {
        return body
      }
      val headers = response.headers()
      Log.d("GetTreeBuds:doInBackground", "headers = $headers")
      Log.d("GetTreeBuds:doInBackground", "body = ${response.body()}")
      return Summaries(mutableListOf(PersonSummary()), false)
    }

    override fun onPostExecute(result: Summaries) {
      result.personSummaries.forEach{ groupAdapter.add(TreeBudItem(it)) }

      recyclerview_gettreebuds.adapter = groupAdapter
    }
  }
}
