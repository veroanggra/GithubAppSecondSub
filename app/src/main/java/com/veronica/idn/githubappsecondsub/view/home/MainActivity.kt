package com.veronica.idn.githubappsecondsub.view.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.veronica.idn.githubappsecondsub.R
import com.veronica.idn.githubappsecondsub.databinding.ActivityMainBinding
import com.veronica.idn.githubappsecondsub.domain.data.model.ItemsItem
import com.veronica.idn.githubappsecondsub.view.detail.dashboard.DetailActivity
import com.veronica.idn.githubappsecondsub.view.favorite.FavoriteActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private var isDark: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        setViewModelProvider()
        showRecycler()
        observeData()
        getSearchData()
        errorSetup()
        loading()
        observeMode()
    }

    private fun observeMode() {
        mainViewModel.getThemeSetting().observe(this, { isDark: Boolean ->
            if (isDark) {
                this.isDark = true
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
            } else {
                this.isDark = false
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        inflater.inflate(R.menu.setting_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.ic_fav -> {
                val intent = Intent(
                    this,
                    FavoriteActivity::class.java
                )
                startActivity(intent)
            }
            R.id.ic_theme -> mainViewModel.saveThemeSetting(!isDark)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loading() {
        mainViewModel.loading.observe(this, { isLoading ->
            if (isLoading) {
                mainBinding.pbMain.visibility = View.VISIBLE
            } else {
                mainBinding.pbMain.visibility = View.GONE
            }
        })
    }

    private fun errorSetup() {
        mainViewModel.error.observe(this, {
            if (it == null) {
                mainBinding.ivError.visibility = View.GONE
                mainBinding.rvMain.visibility = View.VISIBLE
            } else {
                mainBinding.ivError.visibility = View.VISIBLE
                mainBinding.rvMain.visibility = View.GONE
            }
        })
    }

    private fun getSearchData() {
        mainBinding.svMain.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                mainViewModel.getSearchUser(query)
                try {
                    val inputMethodManager =
                        getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(mainBinding.root.windowToken, 0)
                } catch (e: Throwable) {
                    Log.v("DEBUG", e.toString())
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                mainViewModel.getSearchUser(newText)
                return false
            }
        })
    }

    private fun observeData() {
        mainViewModel.listUserLiveData.observe(this, { user ->
            if ((user?.size ?: 0) == 0) {
                mainBinding.apply {
                    ivError.visibility = View.VISIBLE
                    rvMain.visibility = View.GONE
                }
            } else {
                mainBinding.apply {
                    ivError.visibility = View.GONE
                    rvMain.visibility = View.VISIBLE
                    val mainAdapter = MainAdapter(user)
                    rvMain.adapter = mainAdapter
                    //kirim data ke detail
                    val intent = Intent(this@MainActivity, DetailActivity::class.java)
                    mainAdapter.setItemClickCallback(object : OnItemClickCallback {
                        override fun onItemClicked(user: ItemsItem?) {
                            intent.putExtra(DetailActivity.EXTRA_DATA, user)
                            startActivity(intent)
                        }
                    })
                }
            }
        })
    }

    private fun showRecycler() {
        mainBinding.rvMain.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = MainAdapter(listOf())
        }
    }

    private fun setViewModelProvider() {
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
    }


}