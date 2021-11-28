package com.veronica.idn.githubappsecondsub.domain.repository

import android.util.Log
import com.veronica.idn.githubappsecondsub.domain.data.model.DetailUserResponse
import com.veronica.idn.githubappsecondsub.domain.data.model.ItemsItem
import com.veronica.idn.githubappsecondsub.domain.data.model.UserResponse
import com.veronica.idn.githubappsecondsub.domain.data.network.ApiResult
import com.veronica.idn.githubappsecondsub.domain.data.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject
constructor(private val apiService: ApiService) {
    suspend fun getAllUser(): kotlinx.coroutines.flow.Flow<ApiResult<List<ItemsItem?>?>> {
        return flow {
            try {
                val data = apiService.getListUser().items
                emit(ApiResult.Success(data))
            } catch (e: Throwable) {
                emit(ApiResult.Error(e))
                Log.v("DEBUG", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getSearchUser(username: String): Flow<ApiResult<List<ItemsItem?>?>> {
        return flow {
            try {
                val data = apiService.getSearchUser(username)
                emit(ApiResult.Success(data.items))
            } catch (e: Throwable) {
                emit(ApiResult.Error(e))
                Log.v("DEBUG", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getDetailUser(username: String): Flow<ApiResult<DetailUserResponse?>> {
        return flow {
            try {
                val data = apiService.getDetailUser(username)
                emit(ApiResult.Success(data))
            } catch (e: Throwable) {
                emit(ApiResult.Error(e))
                Log.d("DEBUG", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getFollowers(username: String):
            Flow<ApiResult<List<ItemsItem?>?>> {
        return flow {
            try {
                val data = apiService.getFollowers(username)
                emit(ApiResult.Success(data))
            } catch (e: Throwable) {
                emit(ApiResult.Error(e))
            }
        }
    }

    suspend fun getFollowing(username: String):
            Flow<ApiResult<List<ItemsItem?>?>> {
        return flow {
            try {
                val data = apiService.getFollowing(username)
                emit(ApiResult.Success(data))
            } catch (e: Throwable) {
                emit(ApiResult.Error(e))
            }
        }
    }
}