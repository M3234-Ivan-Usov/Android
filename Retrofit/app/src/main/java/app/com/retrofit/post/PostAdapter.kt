package app.com.retrofit.post

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.com.retrofit.MainActivity
import app.com.retrofit.R

class PostAdapter(private val posts: List<Post>) : RecyclerView.Adapter<PostAdapter.PostHolder>() {

    class PostHolder(
        root: View,
        val title: Button = root.findViewById(R.id.post_title),
        val body: TextView = root.findViewById(R.id.post_body)
    ) : RecyclerView.ViewHolder(root)

    override fun getItemCount(): Int {
        return posts.size
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        val post = posts[position]
        with(holder) {
            body.text = post.body
            title.text = post.title
            title.setOnLongClickListener {
                val fm = MainActivity.instance.supportFragmentManager
                PostDelete(post).show(fm, "dialog")
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        return PostHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.post_item, parent, false)
        )
    }
}