package app.com.retrofit.post

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import app.com.retrofit.MainActivity
import app.com.retrofit.R
import retrofit2.Call
import retrofit2.Response


class PostDelete(private val post: Post) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context).apply {
            setMessage(resources.getString(R.string.delete_message))
            setPositiveButton("Yes") { dialog, which ->
                val call = MainActivity.instance.postRequest.delete(post.id, post.userId)
                call.enqueue(object : retrofit2.Callback<Post> {
                    override fun onFailure(call: Call<Post>, t: Throwable) {
                        Toast.makeText(
                            MainActivity.instance, "Delete failed: " +
                                    t.message, Toast.LENGTH_LONG
                        ).show()
                    }

                    override fun onResponse(call: Call<Post>, response: Response<Post>) {
                        Toast.makeText(
                            MainActivity.instance,
                            if (response.isSuccessful) "Delete Successful"
                            else "Delete failed: " + response.code(), Toast.LENGTH_LONG
                        ).show()
                    }
                })
                dialog.cancel()
            }
            setNegativeButton("No") { dialog, which ->
                dialog.cancel()
            }
        }
        return builder.create()
    }
}