package com.veronica.idn.githubappsecondsub.view.favorite

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.veronica.idn.githubappsecondsub.R
import com.veronica.idn.githubappsecondsub.databinding.ActivityFavoriteBinding
import com.veronica.idn.githubappsecondsub.domain.data.model.ItemsItem
import com.veronica.idn.githubappsecondsub.view.detail.dashboard.DetailActivity
import com.veronica.idn.githubappsecondsub.view.home.MainAdapter
import com.veronica.idn.githubappsecondsub.view.home.OnItemClickCallback
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteActivity : AppCompatActivity() {
    private lateinit var favoriteViewBinding: ActivityFavoriteBinding
    private lateinit var favoriteViewModel: FavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favoriteViewBinding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(favoriteViewBinding.root)
        customAppbar()
        setViewModelProvider()
        observeData()
        showRecyclerview()
    }

    private fun showRecyclerview() {
        favoriteViewBinding.rvFav.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@FavoriteActivity)
            adapter = MainAdapter(mutableListOf())
        }
    }

    private fun observeData() {
        favoriteViewModel.favoriteLiveData.observe(this, {
            if (it?.isEmpty() == true) {
                favoriteViewBinding.apply {
                    ivFavError.visibility = View.VISIBLE
                    rvFav.visibility = View.GONE
                }
            }else{
                favoriteViewBinding.apply {
                    ivFavError.visibility = View.GONE
                    rvFav.visibility = View.VISIBLE

                    val mainAdapter = MainAdapter(it?.map {detailUserResponse ->
                    ItemsItem(detailUserResponse.login, type = "", detailUserResponse.avatar_url ?: "")
                    })
                    mainAdapter.setItemClickCallback(object : OnItemClickCallback{
                        override fun onItemClicked(user: ItemsItem?) {
                            val intent = Intent(this@FavoriteActivity, DetailActivity::class.java)
                            intent.putExtra(DetailActivity.EXTRA_DATA,user )
                            startActivity(intent)
                        }
                    })
                    favoriteViewBinding.rvFav.adapter = mainAdapter
                }
            }
        })

    }

    private fun setViewModelProvider() {
        favoriteViewModel = ViewModelProvider(this)[FavoriteViewModel::class.java]
    }

    private fun customAppbar() {
        title = "Favorite"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return false
    }

    override fun onResume() {
        super.onResume()
    }

    companion object {
        fun getLaunchService(from: Context) =
            Intent(from, FavoriteActivity::class.java).apply {
                addFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK
                            or Intent.FLAG_ACTIVITY_CLEAR_TASK
                )
            }
    }
}