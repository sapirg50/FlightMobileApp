/*

package architectureexample

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData

class UrlRepository(application: Application?) {
    private lateinit var urlDao:UrlDao
    private var allUrls: LiveData<List<URL?>?>? = null

    fun UrlRepository(application: Application?) {
        val database = UrlDatabase.getInstance(application!!)
        urlDao = database!!.urlDao()
        allUrls = urlDao.getLastUrls()
    }

    fun insert(url: URL?) {
        InsertNoteAsyncTask(urlDao).execute(url)
        //TODO: check allurl size and delete if needed
    }

    fun update(url: URL?) {
        UpdateNoteAsyncTask(urlDao).execute(url)
    }

    fun delete(url: URL?) {
        DeleteNoteAsyncTask(urlDao).execute(url)
    }

    fun deleteAllNotes() {
        DeleteAllNotesAsyncTask(urlDao).execute()
    }

    fun getAllUrls(): LiveData<List<URL?>?>? {
        return allUrls;
    }

    private class InsertNoteAsyncTask(urlDao: UrlDao) :
        AsyncTask<URL?, Void?, Void?>() {
        private val urlDao: UrlDao = urlDao
        protected override fun doInBackground(vararg urls: URL?): Void? {
            urlDao.insert(urls[0])
            return null
        }
    }

    private class UpdateNoteAsyncTask(urlDao: UrlDao) :
        AsyncTask<URL?, Void?, Void?>() {
        private val urlDao: UrlDao = urlDao
        protected override fun doInBackground(vararg urls: URL?): Void? {
            urlDao.update(urls[0])
            return null
        }
    }

    private class DeleteNoteAsyncTask(urlDao: UrlDao) :
        AsyncTask<URL?, Void?, Void?>() {
        private val urlDao: UrlDao = urlDao
        protected override fun doInBackground(vararg urls: URL?): Void? {
            urlDao.delete(urls[0])
            return null
        }
    }


    private class DeleteAllNotesAsyncTask(urlDao: UrlDao) :
        AsyncTask<Void?, Void?, Void?>() {
        private var urlDao: UrlDao = urlDao
        protected override fun doInBackground(vararg voids: Void?): Void? {
            urlDao.deleteAllUrls()
            return null
        }
    }
}*/
