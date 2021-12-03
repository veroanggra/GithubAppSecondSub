package com.veronica.idn.githubappsecondsub.domain.repository

import android.app.Application
import com.veronica.idn.githubappsecondsub.domain.data.local.Db
import com.veronica.idn.githubappsecondsub.domain.data.local.FavoriteDao
import com.veronica.idn.githubappsecondsub.domain.data.model.DetailUserResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoriteRepository @Inject constructor(application: Application) {
    private val favoriteDao : FavoriteDao
    private val executorService : ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = Db.dataBase(application)
        favoriteDao = db.favDao()
    }

    fun getListFavorite() : Flow<List<DetailUserResponse>>{
        return flow {
            val favoriteList = favoriteDao.getFavorites()
            emit(favoriteList)
        }.flowOn(Dispatchers.IO)
    }
    fun insertFavorite(userResponse: DetailUserResponse){
        executorService.execute { favoriteDao.insert(userResponse) }
    }

    fun deleteFavorite(userResponse: DetailUserResponse){
        executorService.execute { favoriteDao.delete(userResponse) }
    }
}