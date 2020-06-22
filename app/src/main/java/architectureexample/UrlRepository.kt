/*
package architectureexample

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import retrofit2.http.Url

class UrlRepository(application: Application?) {
    private val urlDao: UrlDao
    private val allUrls: LiveData<List<URL>>

    fun insert(url: URL?) {
        InsertUrlAsyncTask(urlDao).execute(url)
    }

    fun update(url: URL?) {
        UpdateUrlAsyncTask(urlDao).execute(url)
    }

    fun delete(url: URL?) {
        DeleteUrlAsyncTask(urlDao).execute(url)
    }

    fun deleteAllUrls() {
        DeleteAllUrlsAsyncTask(urlDao).execute()
    }

    private class InsertUrlAsyncTask(urlDao: UrlDao) :
        AsyncTask<Url?, Void?, Void?>() {
        private val urlDao: UrlDao
        protected override fun doInBackground(vararg urls: URL): Void? {
            urlDao.insert(urls[0])
            return null
        }

        init {
            this.urlDao = urlDao
        }
    }

    private class UpdateUrlAsyncTask(urlDao: UrlDao) :
        AsyncTask<Url?, Void?, Void?>() {
        private val urlDao: UrlDao
        protected override fun doInBackground(vararg urls: URL): Void? {
            urlDao.update(urls[0])
            return null
        }

        init {
            this.urlDao = urlDao
        }
    }

    private class DeleteUrlAsyncTask(urlDao: UrlDao) :
        AsyncTask<Url?, Void?, Void?>() {
        private val urlDao: UrlDao
        protected override fun doInBackground(vararg urls: URL): Void? {
            urlDao.delete(urls[0])
            return null
        }

        init {
            this.urlDao = urlDao
        }
    }

    private class DeleteAllUrlsAsyncTask(urlDao: UrlDao) :
        AsyncTask<Void?, Void?, Void?>() {
        private val urlDao: UrlDao
        protected override fun doInBackground(vararg voids: Void): Void? {
            urlDao.deleteAllUrls()
            return null
        }

        init {
            this.urlDao = urlDao
        }
    }

    init {
        val database: UrlDatabase = UrlDatabase.getInstance(application)
        urlDao = database.urlDao()
        allUrls = urlDao.getAllUrls()
    }
}
*/
