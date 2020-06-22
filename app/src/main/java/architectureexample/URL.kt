package architectureexample

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "url_table")
class URL(val url_path: String, val priority: Int) {
    @PrimaryKey(autoGenerate = true)
    var id = 0
}