package app.com.retrofit

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import app.com.retrofit.comment.CommentList
import app.com.retrofit.post.PostList
import java.lang.RuntimeException

class TabPagerAdapter : FragmentStateAdapter(MainActivity.instance) {
    companion object {
        private const val PAGES = 2
        val titles = arrayOf("Posts", "Comments")
    }
    override fun getItemCount(): Int {
        return PAGES
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> PostList()
            1 -> CommentList()
            else -> throw RuntimeException("How did you get there?")
        }
    }
}