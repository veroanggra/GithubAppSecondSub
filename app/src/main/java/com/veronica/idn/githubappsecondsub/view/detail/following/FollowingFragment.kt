package com.veronica.idn.githubappsecondsub.view.detail.following

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.veronica.idn.githubappsecondsub.R
import com.veronica.idn.githubappsecondsub.databinding.FragmentFollowingBinding
import com.veronica.idn.githubappsecondsub.view.detail.follower.FollowerFragment
import com.veronica.idn.githubappsecondsub.view.home.MainAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FollowingFragment : Fragment() {
    private lateinit var followingBinding: FragmentFollowingBinding
    private lateinit var followingViewModel: FollowingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        followingBinding = FragmentFollowingBinding.inflate(layoutInflater)
        setViewModelProvider()
        setRecyclerView()
        observeData()
        loading()
        setError()
        return followingBinding.root
    }

    private fun setError() {
        followingViewModel.error.observe(viewLifecycleOwner, {
            if (it == null){
                followingBinding.apply {
                    ivErrorFollowing.visibility = View.GONE
                    rvFollowing.visibility = View.VISIBLE
                }
            }else{
                followingBinding.apply {
                    ivErrorFollowing.visibility = View.VISIBLE
                    rvFollowing.visibility = View.GONE
                }
            }
        })
    }

    private fun loading() {
        followingViewModel.loading.observe(viewLifecycleOwner, { isLoading ->
            if (isLoading) {
                followingBinding.apply {
                    pbFollowing.visibility = View.VISIBLE
                    rvFollowing.visibility = View.GONE
                }
            } else {
                followingBinding.apply {
                    pbFollowing.visibility = View.GONE
                    rvFollowing.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun observeData() {
        val username = arguments?.getString(USERNAME)
        followingViewModel.getFollowing(username ?: "")
        followingViewModel.followingLiveData.observe(viewLifecycleOwner, { listFollowing ->
            if ((listFollowing?.size ?: 0) == 0) {
                followingBinding.apply {
                    ivErrorFollowing.visibility = View.VISIBLE
                    rvFollowing.visibility = View.GONE
                }
            } else {
                followingBinding.apply {
                    pbFollowing.visibility = View.GONE
                    rvFollowing.visibility = View.VISIBLE

                    val mainAdapter = MainAdapter(listFollowing)
                    rvFollowing.adapter = mainAdapter
                }
            }
        })
    }

    private fun setViewModelProvider() {
        followingViewModel = ViewModelProvider(this)[FollowingViewModel::class.java]
    }

    private fun setRecyclerView() {
        followingBinding.rvFollowing.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = MainAdapter(listOf())
        }
    }

    companion object {
        private const val USERNAME = "username"
        fun newInstance(username: String): Fragment {
            val fragment = FollowingFragment()
            val bundle = Bundle()
            bundle.putString(USERNAME, username)
            fragment.arguments = bundle
            return fragment
        }
    }
}