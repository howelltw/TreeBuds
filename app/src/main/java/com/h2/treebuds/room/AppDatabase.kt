package com.h2.treebuds.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TreeBudsEntity::class, TreePersonEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
  abstract fun treeBudsDao(): TreeBudsDao
  abstract fun treePersonDao(): TreePersonDao
//  abstract fun treeBudsPersonDao(): TreeBudsPersonDao

  companion object {
    var INSTANCE: AppDatabase? = null

    fun getAppDataBase(context: Context): AppDatabase? {
      if (INSTANCE == null) {

        synchronized(AppDatabase::class){
          INSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "budDB").build()
        }
      }
      return INSTANCE
    }

    fun destroyDataBase(){
      INSTANCE = null
    }
  }
}