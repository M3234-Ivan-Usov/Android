package app.com.retrofit.comment.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import app.com.retrofit.MainActivity
import app.com.retrofit.R
import app.com.retrofit.RetrofitApp
import app.com.retrofit.comment.netApi.PutCallback
import app.com.retrofit.comment.ui.Comment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CommentPut(private val floatButton: FloatingActionButton) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.comment_put, container, false)
        view.findViewById<ImageButton>(R.id.comment_close).setOnClickListener {
            floatButton.visibility = View.VISIBLE
            MainActivity.instance.supportFragmentManager
                .beginTransaction().remove(this).commit()
        }
        view.findViewById<Button>(R.id.comment_put_button).setOnClickListener {
            val name = view.findViewById<EditText>(R.id.comment_put_name).text.toString()
            val email = view.findViewById<EditText>(R.id.comment_put_email).text.toString()
            val body = view.findViewById<EditText>(R.id.comment_put_body).text.toString()
            val call = RetrofitApp.instance.commentRequest.upload(
                Comment(
                    MainActivity.userId,
                    0,
                    name,
                    email,
                    body
                )
            )
            call.enqueue(PutCallback())
            floatButton.visibility = View.VISIBLE
            MainActivity.instance.supportFragmentManager
                .beginTransaction().remove(this).commit()
        }
        return view
    }
}