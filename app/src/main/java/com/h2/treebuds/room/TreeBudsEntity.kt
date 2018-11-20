package com.h2.treebuds.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity
@TypeConverters(ListConverter::class)
class TreeBudsEntity(
        @PrimaryKey(autoGenerate = true)
        val budId: Int? = null,
        val rootPid: String,
        val ancestorPid: String,
        val recordIds: List<String>
)