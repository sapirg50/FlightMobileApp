package architectureexample

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel


class UrlViewModel(application: Application, context:Context) : AndroidViewModel(application) {
    private val db = UrlDatabase.getInstance(context)!!
    //private var allUrls: LiveData<List<URL>> = db.urlDao().getLastUrls()

    fun insert(url: URL) {
        url.time = System.currentTimeMillis() / 1000
        if (db.urlDao().exists(url.path)) {
            this.update(url)
        }else {
            db.urlDao().insert(url)
        }
    }

    private fun update(url: URL) {
        db.urlDao().update(url)
    }

    fun delete(url: URL) {
        db.urlDao().delete(url)
    }

    fun deleteAll() {
        db.urlDao().deleteAllUrls()
    }

    fun getAllUrls(): List<URL> {
        return db.urlDao().getLastUrls()
    }
}