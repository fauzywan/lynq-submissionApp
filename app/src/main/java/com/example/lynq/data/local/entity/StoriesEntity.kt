package com.example.lynq.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "stories")
class StoriesEntity(
    @field:ColumnInfo("id")
    @field:PrimaryKey
    val id: String,
    @field:ColumnInfo(name = "name")
    val name: String,
    @field:ColumnInfo(name = "description")
    val description: String,
    @field:ColumnInfo("photoUrl")
    val photoUrl: String,
    @field:ColumnInfo("lon")
    val lon: Any,
    @field:ColumnInfo("lat")
    val lat: Any,
    @field:ColumnInfo("createdAt")
    val createdAt: String,
)
