package com.h2.treebuds.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TreeBudsDao {
  @Insert
  fun insertTreeBuds(treeBuds: TreeBudsEntity)

  @Update
  fun updateTreeBuds(treeBuds: TreeBudsEntity)

  @Delete
  fun deleteTreeBuds(treeBuds: TreeBudsEntity)

  @Query("SELECT * FROM TreeBudsEntity WHERE ancestorPid == :ancestorPid")
  fun getTreeBuds(ancestorPid: String): LiveData<List<TreeBudsEntity>>

}