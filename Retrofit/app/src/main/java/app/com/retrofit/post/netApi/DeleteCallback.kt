package app.com.retrofit.post.netApi

import android.widget.Toast
import app.com.retrofit.MainActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeleteCallback : Callback<ResponseBody> {
    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
        Toast.makeText(
            MainActivity.instance, "Delete failed: " +
                    t.message, Toast.LENGTH_LONG
        ).show()
    }

    override fun onResponse(
        call: Call<ResponseBody>,
        response: Response<ResponseBody>
    ) {
        Toast.makeText(
            MainActivity.instance,
            if (response.isSuccessful) "Delete is successful"
            else "Delete failed: " + response.code(), Toast.LENGTH_LONG
        ).show()
    }
}