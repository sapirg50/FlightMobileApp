package architectureexample

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [URL::class], version = 1)
abstract class UrlDatabase : RoomDatabase() {
    abstract fun urlDao(): UrlDao
    /*private class PopulateDbAsyncTask(db: UrlDatabase?) :
        AsyncTask<Void?, Void?, Void?>() {
        private val urlDao: UrlDao = db!!.urlDao()
        protected override fun doInBackground(vararg voids: Void?): Void? {
            return null
        }*/

    }

/*
    companion object {
        private var instance: UrlDatabase? = null

        @Synchronized
        fun getInstance(context: Context): UrlDatabase? {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    UrlDatabase::class.java, "note_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build()
            }
            return instance
        }

        private val roomCallback: Callback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                PopulateDbAsyncTask(instance).execute()
            }
        }
    }
}
*/
