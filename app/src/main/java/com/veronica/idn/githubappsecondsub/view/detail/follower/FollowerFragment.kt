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


class FollowerFragment : Fragment() {

    private lateinit var followerBinding: FragmentFollowerBinding
    private lateinit var followerViewModel: FollowerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        followerBinding = FragmentFollowerBinding.inflate(layoutInflater)
        showFollower()
        setViewModelProvider()
        observeData()
        return inflater.inflate(R.layout.fragment_follower, container, false)
    }

    private fun observeData() {
        val username = arguments?.getString(USERNAME)
        followerViewModel.apply {
            getFollowers(username ?: "")
            followerLiveData.observe(viewLifecycleOwner, {it ->
                if ((it?.size ?: 0) == 0) {
                    followerBinding.apply {
                        pbFollower.visibility = View.GONE
                    }
                } else {
                    followerBinding.apply {
                        pbFollower.visibility = View.GONE
                        val mainAdapter = MainAdapter(it)

                    }
                }
            })
        }

    }

    private fun setViewModelProvider() {
        followerViewModel = ViewModelProvider(this)[FollowerViewModel::class.java]
    }

    private fun showFollower() {
        followerBinding.rvFollower.setHasFixedSize(true)
        followerBinding.rvFollower.layoutManager = LinearLayoutManager(context)
        followerBinding.rvFollower.adapter = MainAdapter(listOf())
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