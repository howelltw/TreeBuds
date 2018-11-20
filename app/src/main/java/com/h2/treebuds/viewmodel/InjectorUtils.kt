package com.h2.treebuds.viewmodel

import android.app.Application
import com.h2.treebuds.repository.TreeBudsRepository
import com.h2.treebuds.room.AppDatabase

object InjectorUtils {
  private lateinit var db: AppDatabase

  fun provideTreeBudsViewModelFactory(application: Application): TreeBudsViewModelFactory {
    db = AppDatabase.getAppDataBase(application)!!
    val treeBudsRepository = TreeBudsRepository.getInstance(db)
    return TreeBudsViewModelFactory(treeBudsRepository, application)
  }
}