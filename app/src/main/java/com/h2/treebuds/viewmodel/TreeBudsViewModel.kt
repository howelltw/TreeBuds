package com.h2.treebuds.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.h2.treebuds.repository.TreeBudsRepository
import com.h2.treebuds.room.TreeBudsEntity

class TreeBudsViewModel(private val treeBudsRepository: TreeBudsRepository): ViewModel() {
  fun getTreeBudPersons(pid: String) = treeBudsRepository.getTreeBudPersons(pid)

  fun addTreeBuds(treeBud: TreeBudsEntity) = treeBudsRepository.addTreeBud(treeBud)
}

class TreeBudsViewModelFactory(private val treeBudsRepository: TreeBudsRepository, application: Application): ViewModelProvider.AndroidViewModelFactory(application) {
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    @Suppress("UNCHECKED_CAST")
    return TreeBudsViewModel(treeBudsRepository) as T
  }
}