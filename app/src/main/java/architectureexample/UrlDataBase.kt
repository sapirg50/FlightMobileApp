package architectureexample

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [URL::class], version = 1, exportSchema = false)
abstract class UrlDatabase : RoomDatabase() {
    abstract fun urlDao(): UrlDao
    /*private class PopulateDbAsyncTask(db: UrlDatabase?) :
        AsyncTask<Void?, Void?, Void?>() {
        private val urlDao: UrlDao = db!!.urlDao()
        protected override fun doInBackground(vararg voids: Void?): Void? {
            return null
        }*/
    companion object {
        private var instance: UrlDatabase? = null

        fun getInstance(context: Context): UrlDatabase? {
            var result = instance
            if (result == null) {
                synchronized(this){
                    result = instance
                    if (result==null){
                        result = Room.databaseBuilder(
                            context.applicationContext,
                            UrlDatabase::class.java, "url_table"
                        )
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .build()
                        instance = result
                    }
                }
            }
            return result
        }
}
}