package app.com.retrofit.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import app.com.retrofit.MainActivity
import app.com.retrofit.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostPut(private val floatButton: FloatingActionButton) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.post_put, container, false)
        view.findViewById<Button>(R.id.post_put_button).setOnClickListener {
            val title = view.findViewById<EditText>(R.id.post_put_title).text.toString()
            val body = view.findViewById<EditText>(R.id.post_put_body).text.toString()
            val call = MainActivity.instance.postRequest.upload(
                Post(MainActivity.userId, 0, title, body)
            )

            call.enqueue(object : Callback<Post> {
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

            })
            floatButton.visibility = View.VISIBLE
            MainActivity.instance.supportFragmentManager
                .beginTransaction().remove(this).commit()
        }
        return view
    }


}