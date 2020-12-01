package app.com.retrofit.post.ui

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
import app.com.retrofit.post.dbApi.DatabaseModify
import app.com.retrofit.post.netApi.PutCallback
import app.com.retrofit.post.ui.Post
import app.com.retrofit.post.ui.PostAdapter
import app.com.retrofit.post.ui.PostList
import java.lang.ref.WeakReference

class PostPut(private val postList: WeakReference<PostList>) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.post_put, container, false)
        view.findViewById<ImageButton>(R.id.post_close).setOnClickListener {
            postList.get()?.switchFloatingButtons(true)
            MainActivity.instance.supportFragmentManager
                .beginTransaction().remove(this).commit()
        }

        view.findViewById<Button>(R.id.post_put_button).setOnClickListener {
            val title = view.findViewById<EditText>(R.id.post_put_title).text.toString()
            val body = view.findViewById<EditText>(R.id.post_put_body).text.toString()
            val newId = postList.get()?.content?.stream()
                ?.max { o1, o2 -> o1.id - o2.id }?.get()?.id ?: -1
            val newPost = Post(MainActivity.userId, newId + 1, title, body)

            val call = RetrofitApp.instance.postRequest.upload(newPost)
            call.enqueue(PutCallback())
            DatabaseModify(DatabaseModify.INSERT).execute(newPost)

            postList.get()?.content?.add(newPost)
            postList.get()?.contentRecycler?.adapter?.notifyDataSetChanged()
            postList.get()?.switchFloatingButtons(true)
            MainActivity.instance.supportFragmentManager
                .beginTransaction().remove(this).commit()
        }
        return view
    }


}