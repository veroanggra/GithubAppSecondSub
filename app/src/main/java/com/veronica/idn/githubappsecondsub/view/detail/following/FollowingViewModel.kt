package com.veronica.idn.githubappsecondsub.view.detail.following

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.veronica.idn.githubappsecondsub.domain.data.model.UserResponse
import com.veronica.idn.githubappsecondsub.domain.data.network.ApiResult
import com.veronica.idn.githubappsecondsub.domain.repository.UserRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

class FollowingViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {
    private var strUsername: String = ""
    private val _followingLiveData = MutableLiveData<List<UserResponse?>?>()
    val followingLiveData get() = _followingLiveData

    private val _loading: MutableLiveData<Boolean> = MutableLiveData()
    val loading get() = _loading

    private val _error: MutableLiveData<Throwable?> = MutableLiveData()
    val error get() = _error

    fun getFollowing(username: String) {
        if (strUsername != username) {
            viewModelScope.launch {
                strUsername = username

                userRepository.getFollowing(username).onStart {
                    _loading.value = true
                }.onCompletion {
                    _loading.value = false
                }.collect {
                    when (it) {
                        is ApiResult.Success -> {
                            _error.postValue(null)
                            _followingLiveData.postValue(it.data)
                        }
                        is ApiResult.Error -> {
                            _error.postValue(it.throwable)
                        }
                    }
                }
            }
        }
    }

}