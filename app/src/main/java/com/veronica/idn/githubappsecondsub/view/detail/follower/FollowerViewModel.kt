package com.veronica.idn.githubappsecondsub.view.detail.follower

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.veronica.idn.githubappsecondsub.domain.data.model.ItemsItem
import com.veronica.idn.githubappsecondsub.domain.data.model.UserResponse
import com.veronica.idn.githubappsecondsub.domain.data.network.ApiResult
import com.veronica.idn.githubappsecondsub.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FollowerViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {

    private var strUserName: String = ""

    private val _followersLiveData = MutableLiveData<List<ItemsItem?>?>()
    val followerLiveData get() = _followersLiveData

    private val _loading: MutableLiveData<Boolean> = MutableLiveData()
    val loading get() = _loading

    private val _error: MutableLiveData<Throwable?> = MutableLiveData()
    val error get() = _error

    fun getFollowers(username: String) {
        if (strUserName != username) {
            viewModelScope.launch {
                strUserName = username
                userRepository.getFollowers(username).onStart {
                    _loading.value = true
                }.onCompletion {
                    _loading.value = false
                }.collect {
                    when (it) {
                        is ApiResult.Success -> {
                            _error.postValue(null)
                            _followersLiveData.postValue(it.data)
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