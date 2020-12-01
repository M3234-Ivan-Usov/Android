package app.com.retrofit.comment.netApi

import app.com.retrofit.MainActivity
import app.com.retrofit.R
import app.com.retrofit.comment.ui.Comment
import app.com.retrofit.comment.ui.CommentList
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.ref.WeakReference

class GetCallback(private val fragment : WeakReference<CommentList>) : Callback<List<Comment>> {
    override fun onFailure(call: Call<List<Comment>>, t: Throwable) {
        Snackbar.make(
            MainActivity.instance.findViewById(R.id.main_view),
            "Failed to download: " + t.message,
            Snackbar.LENGTH_INDEFINITE
        ).setAction("Retry") {
            MainActivity.instance.recreate()
        }.show()
    }

    override fun onResponse(call: Call<List<Comment>>, response: Response<List<Comment>>) {
        if (response.isSuccessful) {
            fragment.get()?.content = response.body()
            fragment.get()?.configureView()
        }
    }
}