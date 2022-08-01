package com.mmrbd.ktorexample.ui.home

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mmrbd.ktorexample.R
import com.mmrbd.ktorexample.data.models.User
import com.mmrbd.ktorexample.data.network.ApiSate
import com.mmrbd.ktorexample.databinding.ActivityMainBinding
import com.mmrbd.ktorexample.ui.adapters.RepoViewAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var repoViewAdapter: RepoViewAdapter
    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        actionBar.apply { title = getString(R.string.home) }

        initRecyclerView()

        viewModel.getRepoData("md")
        lifecycleScope.launchWhenStarted {
            viewModel.apiStateFlow.collect {
                binding.apply {
                    when (it) {
                        is ApiSate.Success<*> -> {
                            repoViewAdapter.setDataList(it.data as List<User>)
                            rcv.isVisible = true
                            loading.isVisible = false
                            tvError.isVisible = false
                        }
                        is ApiSate.Failed -> {
                            tvError.text = it.toString()
                            rcv.isVisible = false
                            loading.isVisible = false
                            tvError.isVisible = true

                            Log.e(TAG, "onCreate: FAILED: $it")
                        }
                        is ApiSate.Loading -> {
                            rcv.isVisible = false
                            loading.isVisible = true
                            tvError.isVisible = false

                            Log.e(TAG, "onCreate: LOADING $it")
                        }
                        is ApiSate.Empty -> {
                            Log.e(TAG, "onCreate: EMPTY")

                        }
                    }
                }
            }
        }

        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(search: String?): Boolean {

                if (!search.isNullOrEmpty())
                    viewModel.getRepoData(search)
                Log.e(TAG, "onQueryTextSubmit: $search")

                return false
            }

            override fun onQueryTextChange(search: String?): Boolean {
//                if (!search.isNullOrEmpty())
//                    viewModel.getRepoData(search)

                Log.e(TAG, "onQueryTextChange: $search")

                return false

            }
        })


    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        binding.rcv.layoutManager = layoutManager
        repoViewAdapter = RepoViewAdapter(this) {
            Toast.makeText(this, it.name, Toast.LENGTH_LONG).show()
        }
        val dividerItemDecoration = DividerItemDecoration(
            this,
            layoutManager.orientation
        )
        binding.rcv.addItemDecoration(dividerItemDecoration)
        binding.rcv.adapter = repoViewAdapter
    }
}