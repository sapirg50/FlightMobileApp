package architectureexample

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [URL::class], version = 1)
abstract class UrlDatabase : RoomDatabase() {
    abstract fun urlDao(): UrlDao?

    companion object {
        private var instance: UrlDatabase? = null

        @Synchronized
        fun getInstance(context: Context): UrlDatabase? {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    UrlDatabase::class.java, "url_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instance
        }
    }
}
