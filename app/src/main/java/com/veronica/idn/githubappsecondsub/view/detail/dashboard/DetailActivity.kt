package com.veronica.idn.githubappsecondsub.view.detail.dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.veronica.idn.githubappsecondsub.R
import com.veronica.idn.githubappsecondsub.adapter.ViewPagerAdapter
import com.veronica.idn.githubappsecondsub.databinding.ActivityDetailBinding
import com.veronica.idn.githubappsecondsub.domain.data.model.ItemsItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {
    private lateinit var detailBinding : ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel
    private var user : ItemsItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(detailBinding.root)
        getDataObject()
        setViewModelProvider()
        observeData()
        loading()
        errorSetup()
        setViewPager()
    }

    private fun setViewPager() {
        val viewPagerAdapter = ViewPagerAdapter(this)
        viewPagerAdapter.username = user?.login
        detailBinding.vpDetail.adapter = viewPagerAdapter
        val tabLayout: TabLayout = detailBinding.tlDetail
        TabLayoutMediator(tabLayout, detailBinding.vpDetail){tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    private fun errorSetup() {
        detailViewModel.error.observe(this, {
            if (it == null){
                detailBinding.ivErrorDetail.visibility = View.GONE
                detailBinding.clDetail.visibility = View.VISIBLE
            }else{
                detailBinding.ivErrorDetail.visibility = View.VISIBLE
                detailBinding.clDetail.visibility = View.GONE
            }
        })
    }

    private fun loading() {
        detailViewModel.loading.observe(this, {isLoading ->
            if (isLoading ){
                detailBinding.pbDetail.visibility = View.VISIBLE
            }else{
                detailBinding.pbDetail.visibility = View.GONE
            }
        })
    }

    private fun observeData() {
        detailViewModel.getDetailUser(user?.login ?: " ")
        detailViewModel.detailUser.observe(this, {detail ->
            detailBinding.apply {
                Glide.with(this.root).load(detail?.avatar_url).apply(
                    RequestOptions().centerCrop().override(100)
                ).into(ivDetail)

                tvNameDetail.text = detail?.name ?: "NO AVAILABLE"
            }
            detailViewModel.showFav(detail)
            detailBinding.ivFavDetail.setOnClickListener{
                detailViewModel.isFavUser(detail)
            }
            detailViewModel.isFav.observe(this, {isfav ->
                if (isfav){
                    detailBinding.ivFavDetail.setImageResource(R.drawable.ic_baseline_favorite_24_red)
                }else{
                    detailBinding.ivFavDetail.setImageResource(R.drawable.ic_baseline_favorite_border_24_red)
                }
            })
        })

    }
    private fun setViewModelProvider() {
        detailViewModel = ViewModelProvider(this)[DetailViewModel::class.java]
    }

    private fun getDataObject() {
        user = intent.getParcelableExtra(EXTRA_DATA)
    }

    companion object{
        const val EXTRA_DATA = "extra_data"
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.txt_followers,
            R.string.txt_following
        )
    }
}