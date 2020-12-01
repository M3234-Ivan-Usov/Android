package app.com.retrofit.post.netApi

import android.widget.Toast
import app.com.retrofit.MainActivity
import app.com.retrofit.post.ui.Post
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PutCallback : Callback<Post> {
    override fun onFailure(call: Call<Post>, t: Throwable) {
        Toast.makeText(
            MainActivity.instance, "Post failed: " +
                    t.message, Toast.LENGTH_LONG
        ).show()
    }

    override fun onResponse(call: Call<Post>, response: Response<Post>) {
        Toast.makeText(
            MainActivity.instance,
            if (response.isSuccessful) "Post is successful"
            else "Post failed: " + response.code(), Toast.LENGTH_LONG
        ).show()
    }

}