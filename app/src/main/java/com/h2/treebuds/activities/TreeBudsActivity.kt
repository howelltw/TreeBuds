package com.h2.treebuds.activities

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.h2.treebuds.R
import com.h2.treebuds.room.TreePersonEntity
import com.h2.treebuds.viewmodel.InjectorUtils
import com.h2.treebuds.viewmodel.TreeBudsViewModel
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_tree_buds.*
import kotlinx.android.synthetic.main.content_tree_buds.*
import kotlinx.android.synthetic.main.person_popup.*
import kotlinx.android.synthetic.main.tree_bud_list_item.view.*

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
        recyclerview_gettreebuds.adapter = groupAdapter
        groupAdapter.clear()

        val factory = InjectorUtils.provideTreeBudsViewModelFactory(this.application)
        // Use ViewModelProviders class to create / get already created TreeBudsViewModel for this view (activity)
        val viewModel = ViewModelProviders.of(this, factory)
                .get(TreeBudsViewModel::class.java)
        viewModel.getTreeBudPersons(edittext_ancestorpid.text.toString().toUpperCase()).observe(this, Observer {

//          if (it.isEmpty()) {
//            Toast.makeText(applicationContext, "No TreeBuds found for this Ancestor", LENGTH_LONG).show()
//          }
          it.forEach { groupAdapter.add(TreeBudItem(it)) }
        })

        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(edittext_ancestorpid.windowToken, 0)
      }
      else {
        Toast.makeText(this, "Please enter a valid FamilySearch Person ID", LENGTH_LONG).show()
      }
    }
  }

  class TreeBudItem(val treeBudRow: TreePersonEntity): Item<ViewHolder>() {
    override fun getLayout(): Int {
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
}