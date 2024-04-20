package com.example.data.local.model.news

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "NewsItems")
data class RomTable(
    @PrimaryKey
    val id:String,
    val isHosted:Boolean,
)
