package com.capstone.repoth.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.capstone.repoth.data.model.ListRepoth

@Dao
interface RepothDao {

    /**
     * Insert repoth to local database
     *
     * @param repoth Story to save
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRepoth(vararg repoth: ListRepoth)

    /**
     * Get all repoth from database
     *
     * @return PagingSource
     */
    @Query("SELECT * FROM repoth")
    fun getAllRepoths(): PagingSource<Int, ListRepoth>

    /**
     * Delete all saved repoths from database
     */
    @Query("DELETE FROM repoth")
    fun deleteAll()
}