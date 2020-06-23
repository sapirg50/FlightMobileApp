package architectureexample

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "url_table")
class URL(
    @PrimaryKey
    @ColumnInfo(name = "path")
    val path: String
){
    @ColumnInfo(name = "time")
    var time:Long = 0
}
