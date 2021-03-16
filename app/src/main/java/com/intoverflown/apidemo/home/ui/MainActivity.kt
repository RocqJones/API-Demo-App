package com.intoverflown.apidemo.home.ui

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.intoverflown.apidemo.R
import com.intoverflown.apidemo.databinding.ActivityMainBinding
import com.intoverflown.apidemo.databinding.CreatePostDialogBinding
import com.intoverflown.apidemo.home.data.PostModel
import com.intoverflown.apidemo.home.viewmodel.HomeViewModel

class MainActivity : AppCompatActivity(), HomeAdapter.HomeListener {

    private lateinit var vm: HomeViewModel
    private lateinit var adapter: HomeAdapter
    private lateinit var bindingMain: ActivityMainBinding
    private lateinit var bindingCreatePost: CreatePostDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        bindingMain = ActivityMainBinding.inflate(layoutInflater)
        val view  = bindingMain.root
        setContentView(view)

        vm = ViewModelProvider(this)[HomeViewModel::class.java]

        initAdapter()

        vm.fetchAllPosts()

        vm.postModelListLiveData?.observe(this, Observer {
            if (it!=null){
                bindingMain.rvHome.visibility = View.VISIBLE
                adapter.setData(it as ArrayList<PostModel>)
            }else{
                showToast("Something went wrong")
            }
            bindingMain.progressHome.visibility = View.GONE
        })
    }

    private fun initAdapter() {
        adapter = HomeAdapter(this)
        bindingMain.rvHome.layoutManager = LinearLayoutManager(this)
        bindingMain.rvHome.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_create_post -> showCreatePOstDialog()
        }
        return true
    }

    private fun showCreatePOstDialog() {
        val dialog = Dialog(this)
//        val view = LayoutInflater.from(this).inflate(R.layout.create_post_dialog, null)
        bindingCreatePost = CreatePostDialogBinding.inflate(layoutInflater)
        val view  = bindingCreatePost.root
        dialog.setContentView(view)

        var title = ""
        var body = ""

        bindingCreatePost.btnSubmit.setOnClickListener {
            title = bindingCreatePost.etTitle.text.toString().trim()
            body = bindingCreatePost.etBody.text.toString().trim()

            if (title.isNotEmpty() && body.isNotEmpty()){
                val postModel = PostModel()
                postModel.userId = 1
                postModel.title = title
                postModel.body = body

                vm.createPost(postModel)

                vm.createPostLiveData?.observe(this, Observer {
                    if (it!=null){
                        adapter.addData(postModel)
                        bindingMain.rvHome.smoothScrollToPosition(0)
                    }else{
                        showToast("Cannot create post at the moment")
                    }
                    dialog.cancel()
                })

            }else{
                showToast("Please fill data carefully!")
            }

        }

        dialog.show()

        val window = dialog.window
        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)

    }

    override fun onItemDeleted(postModel: PostModel, position: Int) {
        postModel.id?.let { vm.deletePost(it) }
        vm.deletePostLiveData?.observe(this, Observer {
            if (it!=null){
                adapter.removeData(position)
            }else{
                showToast("Cannot delete post at the moment!")
            }
        })
    }

    private fun showToast(msg:String){
        Toast.makeText(this,msg, Toast.LENGTH_SHORT).show()
    }
}