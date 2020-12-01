package app.com.retrofit.post.dbApi

import androidx.room.Database
import androidx.room.RoomDatabase
import app.com.retrofit.post.ui.Post

@Database(version = 1, entities = [Post::class])
abstract class PostDatabase : RoomDatabase() {
    abstract fun postDao() : PostDao
}