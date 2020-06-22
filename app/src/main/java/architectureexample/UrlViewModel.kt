package architectureexample

import android.app.Application
import android.provider.ContactsContract.CommonDataKinds.Note
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData


class UrlViewModel(application: Application) : AndroidViewModel(application) {
    private lateinit var repository: UrlRepository
    private var allUrls: LiveData<List<URL?>?>? = null

    fun UrlViewModel(application: Application) {
        repository = UrlRepository(application)
        allUrls = repository.getAllUrls()
    }
    fun insert(url: URL?) {
        repository.insert(url)
    }

    fun update(url: URL?) {
        repository.update(url)
    }

    fun delete(url: URL?) {
        repository.delete(url)
    }

    fun deleteAllNotes() {
        repository.deleteAllNotes()
    }

    fun getAllUrls(): LiveData<List<URL?>?>? {
        return allUrls
    }
}