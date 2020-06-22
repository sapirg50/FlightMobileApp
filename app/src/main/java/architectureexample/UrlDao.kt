package architectureexample

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface UrlDao {
    @Insert
    fun insert(url: URL?)

    @Update
    fun update(url: URL?)

    @Delete
    fun delete(url: URL?)

    @Query("DELETE FROM url_table")
    fun deleteAllUrls()

    //@Query("SELECT * FROM url_table ORDER BY priority DESC")
    fun getAllUrls(): LiveData<List<URL?>?>?
}