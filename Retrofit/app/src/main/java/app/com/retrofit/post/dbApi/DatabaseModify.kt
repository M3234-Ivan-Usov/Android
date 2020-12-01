package app.com.retrofit.post.dbApi

import android.os.AsyncTask
import app.com.retrofit.RetrofitApp
import app.com.retrofit.post.ui.Post
import app.com.retrofit.post.ui.PostList
import java.lang.ref.WeakReference

class DatabaseModify (private val operation : Int) : AsyncTask<Post, Void, Int>() {

    companion object {
        const val INSERT = 0
        const val DELETE = 1
    }

    override fun doInBackground(vararg params: Post?): Int {
        if (operation == INSERT) {
            RetrofitApp.instance.postDatabase.postDao().insert(params[0]!!)
        }
        if (operation == DELETE) {
            RetrofitApp.instance.postDatabase.postDao().delete(params[0]!!)
        }
        return 0
    }
}