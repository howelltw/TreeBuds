package com.h2.treebuds.activities

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.h2.treebuds.R
import com.h2.treebuds.client.AuthorizationInterceptor
import com.h2.treebuds.client.FsSession
import com.h2.treebuds.client.PopClient
import com.h2.treebuds.client.TFClient
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_tree_buds.*
import kotlinx.android.synthetic.main.content_tree_buds.*
import kotlinx.android.synthetic.main.person_popup.*
import kotlinx.android.synthetic.main.tree_bud_list_item.view.*
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

    supportActionBar?.setDisplayHomeAsUpEnabled(false)

    button_gettreebuds.setOnClickListener {
      if (edittext_ancestorpid.text.isNotBlank()) {
        GetTreeBuds().execute(edittext_ancestorpid.text.toString().toUpperCase())

        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(edittext_ancestorpid.windowToken, 0)
      }
      else {
        Toast.makeText(this, "Please enter a valid FamilySearch Person ID", LENGTH_LONG).show()
      }
    }
  }

  class TreeBudItem(val treeBudRow: TreeBudRow): Item<ViewHolder>() {
    override fun getLayout(): Int {
      Log.d("TreeBudItem:getLayout", "Made it here")
      return R.layout.tree_bud_list_item
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
      Log.d("TreeBudItem:bind", "treeBudRow=$treeBudRow")
      viewHolder.itemView.person_photo_progress_spinner.visibility = View.GONE
      viewHolder.itemView.person_name.text = treeBudRow.personName
      viewHolder.itemView.person_pid.text = treeBudRow.personId
      viewHolder.itemView.person_lifespan.text = treeBudRow.lifeSpan

      val photoResource = when (treeBudRow.gender) {
        "FEMALE" -> R.drawable.female_empty_portrait
        "MALE" -> R.drawable.male_empty_portrait
        else -> R.drawable.unknown_empty_portrait
      }
      viewHolder.itemView.person_photo.setImageResource(photoResource)
      viewHolder.itemView.person_photo.visibility = View.VISIBLE
      viewHolder.itemView.record_hint.visibility = View.VISIBLE

      viewHolder.itemView.isClickable = true
      viewHolder.itemView.setOnClickListener { view ->
        val dialog = Dialog(view.context)
        dialog.setContentView(R.layout.person_popup)
        dialog.setCancelable(true)

        dialog.popup_name.text = treeBudRow.personName
        dialog.popup_pid.text = treeBudRow.personId
        dialog.popup_birth.text = treeBudRow.birthDate
        dialog.popup_birth_place.text = treeBudRow.birthPlace
        dialog.popup_death.text = treeBudRow.deathDate
        dialog.popup_death_place.text = treeBudRow.deathPlace
        dialog.popup_photo.setImageResource(photoResource)
        dialog.popup_photo.visibility = View.VISIBLE

        dialog.show()
      }

      viewHolder.itemView.isLongClickable = true
      viewHolder.itemView.setOnLongClickListener { view ->
        showDialog(view)
        return@setOnLongClickListener true
      }
    }

    private fun showDialog(view: View) {
      lateinit var dialog: AlertDialog

      val builder = AlertDialog.Builder(view.context)
      builder.setTitle("Create ToDo Item")
      builder.setMessage("Create FamilySearch ToDo Item for this TreeBud?")

      val dialogClickListener = DialogInterface.OnClickListener { _, which ->
        when (which){
          DialogInterface.BUTTON_POSITIVE -> Toast.makeText(view.context, "Yes button clicked.", Toast.LENGTH_SHORT).show()
          DialogInterface.BUTTON_NEGATIVE -> dialog.dismiss()
          DialogInterface.BUTTON_NEUTRAL -> dialog.dismiss()
        }
      }

      builder.setPositiveButton("YES", dialogClickListener)
      builder.setNegativeButton("NO", dialogClickListener)
      builder.setNeutralButton("CANCEL", dialogClickListener)

      dialog = builder.create()
      dialog.show()
    }
  }

  inner class GetTreeBuds : AsyncTask<String, Void, MutableList<TreeBudRow>>() {

    override fun doInBackground(vararg params: String): MutableList<TreeBudRow> {
      Log.d("GetTreeBuds:doInBackground", "ancestorPid = ${params[0]}")

      // This will make POP and TF calls to get a list of TreeBuds
      return buildTreeBudsList(params[0])
    }

    override fun onPostExecute(result: MutableList<TreeBudRow>) {
      recyclerview_gettreebuds.adapter = groupAdapter
      groupAdapter.clear()

      if (result.isEmpty()) {
        Toast.makeText(applicationContext, "No TreeBuds found for this Ancestor", LENGTH_LONG).show()
      }
      result.forEach { groupAdapter.add(TreeBudItem(it)) }

    }

    /**
     * Call POP to get a list of TreeBuds and then TF to decorate the person
     */
    private fun buildTreeBudsList(ancestorPid: String): MutableList<TreeBudRow> {
      val httpClient = OkHttpClient.Builder().addInterceptor(AuthorizationInterceptor())

      val retrofit = Retrofit.Builder().client(httpClient.build())
              .baseUrl("https://www.familysearch.org/")
              .addConverterFactory(MoshiConverterFactory.create())
              .build()
      val popClient = retrofit.create(PopClient::class.java)
      val popResponse = popClient.getTreeBuds(FsSession.userId, ancestorPid).execute()

      val popBody = popResponse.body()
      if (popResponse.isSuccessful) {
        val treeBudRowList = mutableListOf<TreeBudRow>()

        if (popBody != null) {
          // For each person in the Summary list, call TF to get name and lifespan
          val tfClient = retrofit.create(TFClient::class.java)
          popBody.personSummaries.forEach {
            val tfResponse = tfClient.getTfPersonCard(it.personId).execute()

            val tfBody = tfResponse.body()
            if (tfResponse.isSuccessful && tfBody != null) {
              treeBudRowList.add(TreeBudRow(it.personId,
                      tfBody.summary.name,
                      tfBody.summary.lifespan,
                      tfBody.summary.gender,
                      tfBody.summary.lifespanBegin?.date?.original,
                      tfBody.summary.lifespanBegin?.place?.original,
                      tfBody.summary.lifespanEnd?.date?.original,
                      tfBody.summary.lifespanEnd?.place?.original))
            }
          }
        }
        return treeBudRowList
      }

      return mutableListOf(TreeBudRow(popResponse.code().toString(), "POP Error", "httpStatus  =", "Unknown", null, null, null,null))
    }


  }
}

data class TreeBudRow(val personId: String, val personName: String, val lifeSpan: String, val gender: String, val birthDate: String?, val birthPlace: String?, val deathDate: String?, val deathPlace: String?)