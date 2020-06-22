package architectureexample

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.room.Room


class UrlViewModel(application: Application) : AndroidViewModel(application) {
    private val db = Room
        .databaseBuilder(application.applicationContext,UrlDatabase::class.java,"URL").build()
    private var allUrls: LiveData<List<URL>> = db.urlDao().getLastUrls()

    fun insert(url: URL) {
        db.urlDao().insert(url)
    }

    fun update(url: URL) {
        db.urlDao().update(url)
    }

    fun delete(url: URL) {
        db.urlDao().delete(url)
    }

    fun getAllUrls(): LiveData<List<URL>> {
        return allUrls
    }
}