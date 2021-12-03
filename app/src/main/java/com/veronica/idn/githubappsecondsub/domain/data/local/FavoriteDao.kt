package com.veronica.idn.githubappsecondsub.domain.data.local

import androidx.room.*
import com.veronica.idn.githubappsecondsub.domain.data.model.DetailUserResponse

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert (detailUserResponse: DetailUserResponse)

    @Query( "SELECT * from detailUserResponse")
    fun getFavorites() :List<DetailUserResponse>

    @Delete
    fun delete(detailUserResponse: DetailUserResponse)
}