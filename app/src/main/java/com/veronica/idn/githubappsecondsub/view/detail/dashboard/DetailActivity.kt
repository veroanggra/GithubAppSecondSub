package com.veronica.idn.githubappsecondsub.view.detail.dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
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
        Loading()
        errorSetup()
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

    private fun Loading() {
        detailViewModel.loading.observe(this, {isLoading ->
            if (isLoading){
                detailBinding.pbDetail.visibility = View.VISIBLE
            }else{
                detailBinding.pbDetail.visibility = View.GONE
            }
        })
    }

    private fun observeData() {
        detailViewModel.getDetailUser(user?.login ?: "")
        detailViewModel.detailUser.observe(this, {detail ->
            detailBinding.apply {
                Glide.with(this.root).load(detail?.avatar_url).apply(
                    RequestOptions().centerCrop().override(100)
                ).into(ivDetail)

                tvNameDetail.text = detail?.name ?: "NO AVAILABLE"
            }
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
    }
}