package com.example.fitpho.Database

import androidx.room.Dao
import androidx.room.Query

@Dao
interface GuideDao {

    @Query("SELECT * FROM GuideEntity WHERE id BETWEEN :minId AND :maxId")
    fun getGuide(minId: Int, maxId: Int):GuideEntity


}