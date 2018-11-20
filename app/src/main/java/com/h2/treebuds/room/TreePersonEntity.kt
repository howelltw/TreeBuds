package com.h2.treebuds.room

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index("personId")])
data class TreePersonEntity(
        @PrimaryKey(autoGenerate = true)
        val id: Int? = null,
        val personId: String,
        val personName: String,
        val lifeSpan: String,
        val gender: String,
        val birthDate: String?,
        val birthPlace: String?,
        val deathDate: String?,
        val deathPlace: String?,
        val ttl: Long)