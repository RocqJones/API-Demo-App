package com.intoverflown.apidemo.home.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.intoverflown.apidemo.home.data.HomeRepository
import com.intoverflown.apidemo.home.data.PostModel

class HomeViewModel(application: Application): AndroidViewModel(application) {
    private var homeRepository:HomeRepository?=null
    var postModelListLiveData : LiveData<List<PostModel>>?=null
    var createPostLiveData:LiveData<PostModel>?=null
    var deletePostLiveData:LiveData<Boolean>?=null

    init {
        homeRepository = HomeRepository()
        postModelListLiveData = MutableLiveData()
        createPostLiveData = MutableLiveData()
        deletePostLiveData = MutableLiveData()
    }

    // call fetch post from Home Repository
    fun fetchAllPosts(){
        postModelListLiveData = homeRepository?.fetchAllPosts()
    }

    // call create post from Home Repository
    fun createPost(postModel: PostModel){
        createPostLiveData = homeRepository?.createPost(postModel)
    }

    // call delete post from Home Repository
    fun deletePost(id:Int){
        deletePostLiveData = homeRepository?.deletePost(id)
    }
}