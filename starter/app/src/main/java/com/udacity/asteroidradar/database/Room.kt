package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.asteroidradar.Util

@Dao
interface AsteriodDao {
    @Query("select * from asteroid where date(close_approach_date) >= DATE('now') ORDER BY (close_approach_date)")
    fun getAsteroids(): LiveData<List<DatabaseAsteroid>>

    @Query("select * from pic_of_the_day LIMIT 1")
    fun getPicOfTheDay(): LiveData<DatabasePictureOfTheDay?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg asteroid: DatabaseAsteroid)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPicOfTheDay(pod: DatabasePictureOfTheDay)

    // Delete one day or less old asteroid data
    @Query("delete from asteroid where date(close_approach_date) <= date('now', '-1 day')")
    suspend fun deleteOldAsteroids()

    @Query("delete from pic_of_the_day where url not like :expectedUrl")
    suspend fun deletePicOfTheDay(expectedUrl: String)

    // Delete one day or less pic of the day data
//    @Query("delete from pic_of_the_day where create_date <= date('now', '-1 day')")
//    suspend fun deletePicOfTheDay()


}

@Database(entities = [DatabaseAsteroid::class, DatabasePictureOfTheDay::class], version = 1, exportSchema = false)
@TypeConverters(Util.Converters::class)
abstract class AsteroidDatabase : RoomDatabase() {
    abstract fun asteroidDao(): AsteriodDao

    companion object {
        @Volatile
        private lateinit var INSTANCE: AsteroidDatabase

        fun getDatabaseInstance(context: Context): AsteroidDatabase {
            synchronized(AsteroidDatabase::class.java)
            {
                if (!::INSTANCE.isInitialized) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AsteroidDatabase::class.java,
                        "asteroid_database"
                    ).build()
                }
            }
            return INSTANCE
        }
    }
}