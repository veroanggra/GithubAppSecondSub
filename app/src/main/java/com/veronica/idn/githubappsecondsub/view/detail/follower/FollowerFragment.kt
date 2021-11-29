package com.veronica.idn.githubappsecondsub.view.detail.follower

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.veronica.idn.githubappsecondsub.R
import com.veronica.idn.githubappsecondsub.databinding.FragmentFollowerBinding
import com.veronica.idn.githubappsecondsub.view.home.MainAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FollowerFragment : Fragment() {

    private lateinit var followerBinding: FragmentFollowerBinding
    private lateinit var followerViewModel: FollowerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        followerBinding = FragmentFollowerBinding.inflate(layoutInflater)
        setViewModelProvider()
        showFollower()
        observeData()
        loading()
        setError()
        return followerBinding.root
    }

    private fun setError() {
        followerViewModel.error.observe(viewLifecycleOwner, {
            if (it == null){
                followerBinding.apply {
                    ivErrorFollower.visibility = View.GONE
                    rvFollower.visibility = View.VISIBLE  }
            }else{
                followerBinding.apply {
                    ivErrorFollower.visibility = View.VISIBLE
                    rvFollower.visibility = View.GONE
                }
            }
        })
    }

    private fun loading() {
        followerViewModel.loading.observe(viewLifecycleOwner, { isLoading ->
            if (isLoading){
                followerBinding.apply {
                    pbFollower.visibility = View.VISIBLE
                    rvFollower.visibility = View.GONE
                }
            }else{
                followerBinding.apply {
                    pbFollower.visibility = View.GONE
                    rvFollower.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun observeData() {
        val username = arguments?.getString(USERNAME)
        followerViewModel.apply {
            getFollowers(username ?: "")
            followerLiveData.observe(viewLifecycleOwner, {it ->
                if ((it?.size ?: 0) == 0) {
                    followerBinding.apply {
                        ivErrorFollower.visibility = View.VISIBLE
                        rvFollower.visibility = View.GONE
                    }
                } else {
                    followerBinding.apply {
                        ivErrorFollower.visibility = View.GONE
                        rvFollower.visibility = View.VISIBLE

                        val mainAdapter = MainAdapter(it)
                        rvFollower.adapter = mainAdapter
                    }
                }
            })
        }

    }

    private fun setViewModelProvider() {
        followerViewModel = ViewModelProvider(this)[FollowerViewModel::class.java]
    }

    private fun showFollower() {
        followerBinding.rvFollower.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = MainAdapter(listOf())
        }
    }

    companion object {
        private const val USERNAME = "username"
        fun newInstance(username: String): Fragment {
            val fragment = FollowerFragment()
            val bundle = Bundle()
            bundle.putString(USERNAME, username)
            fragment.arguments = bundle
            return fragment
        }
    }
}