package app.com.retrofit.post.ui

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Entity
@JsonClass(generateAdapter = true)
data class Post(
    @ColumnInfo(name = "userId")
    @Json(name = "userId")
    val userId: Int,

    @PrimaryKey
    @Json(name = "id")
    val id: Int,

    @ColumnInfo(name = "title")
    @Json(name = "title")
    val title: String,

    @ColumnInfo(name = "body")
    @Json(name = "body")
    val body: String
)

