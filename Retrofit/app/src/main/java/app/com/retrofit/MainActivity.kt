package app.com.retrofit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room
import androidx.viewpager2.widget.ViewPager2
import app.com.retrofit.comment.netApi.CommentRequest
import app.com.retrofit.post.dbApi.PostDatabase
import app.com.retrofit.post.netApi.PostRequest
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File

class MainActivity : AppCompatActivity() {
    companion object {
        const val userId = 404
        lateinit var instance: MainActivity
    }

    private lateinit var tabs: TabLayout
    private lateinit var tabPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        instance = this
        tabs = findViewById(R.id.tab_layout)
        tabPager = findViewById(R.id.tab_pager)
        tabPager.adapter = TabPagerAdapter()
        TabLayoutMediator(tabs, tabPager) { tab, position ->
            tab.text = TabPagerAdapter.titles[position]
            tabPager.setCurrentItem(tab.position, true)
        }.attach()
    }
}