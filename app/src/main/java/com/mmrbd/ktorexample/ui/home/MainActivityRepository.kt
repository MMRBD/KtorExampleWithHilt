package com.mmrbd.ktorexample.ui.home

import com.mmrbd.ktorexample.data.models.RepoResult
import com.mmrbd.ktorexample.data.network.ApiService
import io.ktor.client.call.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class MainActivityRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getRepoData(q: String): Flow<RepoResult> = flow {
        emit(apiService.getRepoData(q).body())
    }

}