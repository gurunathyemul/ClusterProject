package com.example.data.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao//data access objects
interface BaseDAO {
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun addData(newsList: NewsList)
//
//    @Query("select * from newslist")
//    suspend fun getData(): List<NewsList>
//
//    @Delete
//    suspend fun deleteData(newsList: NewsList)
//
//    @Query("delete from ")
}