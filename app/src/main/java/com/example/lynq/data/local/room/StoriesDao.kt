package com.example.lynq.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.lynq.data.local.entity.StoriesEntity


@Dao
interface StoriesDao {

    @Query("SELECT * FROM stories ORDER BY createdAt DESC")
    fun getAllStory(): LiveData<List<StoriesEntity>>

    @Query("SELECT * FROM stories Where id = :storyId")
    suspend fun getStoryById(storyId: Int): StoriesEntity



    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertStory(news: List<StoriesEntity>)

    @Update
    fun updateStory(event: StoriesEntity)

    @Query("DELETE FROM stories")
    suspend fun deleteAll()

 /*   @Query("SELECT * FROM stories where date('now') < endTime ORDER BY beginTime DESC")
    fun getListEventActive(): LiveData<List<EventEntity>>
    @Query("SELECT EXISTS(SELECT * FROM stories WHERE id = :title AND bookmarked = 1)")
    fun isNewsBookmarked(title: Int): Boolean
    @Query("SELECT * FROM stories WHERE isActive = :isActive limit :limit")
    fun getEventByStatusNLimit(isActive: Int,limit:Int): LiveData<List<EventEntity>>

    @Query("SELECT * FROM stories where date('now') > endTime ORDER BY beginTime DESC")
    fun getListEventNonActive(): LiveData<List<EventEntity>>
    @Query("SELECT * FROM stories WHERE bookmarked = 1")
    fun getFavoriteEvents(): LiveData<List<EventEntity>>
    @Query("SELECT * FROM stories where isActive = :active ORDER BY beginTime DESC")
    fun getListEvent(active:String): LiveData<List<EventEntity>>
    @Query("SELECT * FROM stories where bookmarked = 1")
    fun getBookmarkedNews(): LiveData<List<EventEntity>>
*/




}