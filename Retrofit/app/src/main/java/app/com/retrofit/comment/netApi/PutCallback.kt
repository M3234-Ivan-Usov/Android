package app.com.retrofit.comment.netApi

import android.widget.Toast
import app.com.retrofit.MainActivity
import app.com.retrofit.comment.ui.Comment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PutCallback : Callback<Comment> {
    override fun onFailure(call: Call<Comment>, t: Throwable) {
        Toast.makeText(
            MainActivity.instance, "Post failed: " +
                    t.message, Toast.LENGTH_LONG
        ).show()
    }

    override fun onResponse(call: Call<Comment>, response: Response<Comment>) {
        Toast.makeText(
            MainActivity.instance,
            if (response.isSuccessful) "Post is successful"
            else "Post failed: " + response.code(), Toast.LENGTH_LONG
        ).show()
    }

}