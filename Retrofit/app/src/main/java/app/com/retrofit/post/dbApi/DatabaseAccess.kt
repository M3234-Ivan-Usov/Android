package app.com.retrofit.post.dbApi

import android.os.AsyncTask
import app.com.retrofit.MainActivity
import app.com.retrofit.RetrofitApp
import app.com.retrofit.post.ui.Post
import app.com.retrofit.post.ui.PostAdapter
import app.com.retrofit.post.ui.PostList
import java.lang.ref.WeakReference

class DatabaseAccess(
    private val postList: WeakReference<PostList>,
    private val operation: Int
) : AsyncTask<List<Post>, Void, Int>() {

    companion object {
        const val REWRITE = -1
        const val READ_ALL = 0
        const val DELETE_ALL = 1
    }

    override fun onPreExecute() {
        postList.get()?.switchView(true)
        super.onPreExecute()
    }

    override fun doInBackground(vararg params: List<Post>?): Int {
        if (operation == READ_ALL) {
            postList.get()?.content =
                RetrofitApp.instance.postDatabase.postDao().collectAll() as ArrayList<Post>
        }
        if (operation == DELETE_ALL) {
            RetrofitApp.instance.postDatabase.postDao().deleteAll()
        }
        if (operation == REWRITE) {
            RetrofitApp.instance.postDatabase.postDao().deleteAll()
            RetrofitApp.instance.postDatabase.postDao().insertAll(params[0]!!)
        }
        return 0
    }

    override fun onPostExecute(result: Int?) {
        if (operation == READ_ALL) {
            postList.get()?.contentRecycler?.adapter?.notifyDataSetChanged()
        }
        postList.get()?.switchView(false)
        super.onPostExecute(result)
    }

}