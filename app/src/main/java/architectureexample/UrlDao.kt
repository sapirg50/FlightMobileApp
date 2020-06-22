package architectureexample

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface UrlDao {
    @Insert
    fun insert(url: URL)

    @Update
    fun update(url: URL)

    @Delete
    fun delete(url: URL)

    @Query("DELETE FROM URL")
    fun deleteAllUrls()

    @Query("SELECT 5 FROM url ORDER BY time DESC")
    fun getLastUrls(): LiveData<List<URL>>
}