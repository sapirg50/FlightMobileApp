package architectureexample

import androidx.room.*


@Dao
interface UrlDao {
    @Insert
    fun insert(url: URL)

    @Update
    fun update(url: URL)

    @Delete
    fun delete(url: URL)

    @Query("SELECT * FROM url_table WHERE path LIKE :urlPath")
    fun any(urlPath:String):List<URL>

    fun exists(urlPath:String):Boolean{
        return any(urlPath).isNotEmpty()
    }

    @Query("DELETE FROM url_table")
    fun deleteAllUrls()

    @Query("SELECT * FROM url_table ORDER BY time DESC LIMIT 5")
    fun getLastUrls(): List<URL>
}