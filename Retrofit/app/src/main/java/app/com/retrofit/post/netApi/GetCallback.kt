package app.com.retrofit.post.netApi

import app.com.retrofit.MainActivity
import app.com.retrofit.R
import app.com.retrofit.post.dbApi.DatabaseAccess
import app.com.retrofit.post.ui.Post
import app.com.retrofit.post.ui.PostList
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.ref.WeakReference

class GetCallback(private val postList: WeakReference<PostList>) : Callback<List<Post>> {
    override fun onFailure(call: Call<List<Post>>, t: Throwable) {
        Snackbar.make(
            MainActivity.instance.findViewById(R.id.main_view),
            "Failed to download: " + t.message, Snackbar.LENGTH_LONG
        ).show()
        postList.get()?.switchView(false)
    }

    override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
        if (response.isSuccessful) {
            DatabaseAccess(postList, DatabaseAccess.REWRITE).execute(response.body()!!)
            postList.get()?.content = response.body() as ArrayList<Post>
            postList.get()?.contentRecycler?.adapter?.notifyDataSetChanged()
        }
        postList.get()?.switchView(false)
    }
}