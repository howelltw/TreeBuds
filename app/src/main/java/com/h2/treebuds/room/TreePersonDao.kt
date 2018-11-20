package com.h2.treebuds.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TreePersonDao {
  @Insert
  fun insertTreePerson(treePerson: TreePersonEntity)

  @Update
  fun updateTreePerson(treePerson: TreePersonEntity)

  @Delete
  fun deleteTreePerson(treePerson: TreePersonEntity)

  @Query("SELECT * FROM TreePersonEntity")
  fun getTreePerson(): LiveData<List<TreePersonEntity>>
}