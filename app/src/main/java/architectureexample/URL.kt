package architectureexample

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class URL(
    @PrimaryKey(autoGenerate = true)
    val uid: Int,
    @ColumnInfo(name = "url")
    val url: String,
    val time:Long
)
