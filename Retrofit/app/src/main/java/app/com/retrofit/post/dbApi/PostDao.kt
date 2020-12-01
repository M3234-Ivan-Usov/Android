package app.com.retrofit.post.dbApi

import androidx.room.*
import app.com.retrofit.post.ui.Post


@Dao
interface PostDao {
    @Query("SELECT * FROM post")
    fun collectAll() : List<Post>

    @Insert
    fun insert(post : Post)

    @Insert
    fun insertAll(posts : List<Post>)

    @Delete
    fun delete(post : Post)

    @Query("DELETE FROM post")
    fun deleteAll()
}