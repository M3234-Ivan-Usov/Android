package app.com.retrofit.comment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.com.retrofit.MainActivity
import app.com.retrofit.R

class CommentAdapter(private val comments: List<Comment>) :
    RecyclerView.Adapter<CommentAdapter.CommentHolder>() {

    class CommentHolder(
        root: View,
        val from: Button = root.findViewById(R.id.comment_from),
        val body: TextView = root.findViewById(R.id.comment_body)
    ) : RecyclerView.ViewHolder(root)

    override fun getItemCount(): Int {
        return comments.size
    }

    override fun onBindViewHolder(holder: CommentHolder, position: Int) {
        val comment = comments[position]
        with(holder) {
            body.text = comment.body
            from.text = "From: " + comment.name + "\ne-mail: " + comment.email
            from.setOnLongClickListener {
                val fm = MainActivity.instance.supportFragmentManager
                CommentDelete(comment).show(fm, "dialog")
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentHolder {
        return CommentHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.comment_item, parent, false)
        )
    }
}