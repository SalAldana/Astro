package net.level3studios.astro.utilities

import android.content.Context
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import net.level3studios.astro.models.AstroLocation
import java.io.File
import java.io.FileOutputStream

class DatabaseHelper(private val context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "worldcities"
        const val DATABASE_VERSION = 1
        const val PATH_NAME = "databases"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        //do nothing
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //do nothing
    }

    override fun getWritableDatabase(): SQLiteDatabase {
        throw RuntimeException("The $DATABASE_NAME is not writable")
    }

    override fun getReadableDatabase(): SQLiteDatabase {
        installOrUpdatedIfNeeded()
        return super.getReadableDatabase()
    }

    //install the database from assets directory
    private val preferences: SharedPreferences = context.getSharedPreferences(
        "${context.packageName}.database_version",
        Context.MODE_PRIVATE
    )

    private fun isDBOutdated(): Boolean {
        return preferences.getInt(DATABASE_NAME, 0) < DATABASE_VERSION
    }

    private fun saveDatabase() {
        preferences.edit().apply {
            putInt(DATABASE_NAME, DATABASE_VERSION)
            apply()
        }
    }

    @Synchronized
    private fun installOrUpdatedIfNeeded() {
        if (isDBOutdated()) {
            context.deleteDatabase(DATABASE_NAME)
            installFromAssets()
            saveDatabase()
        }
    }

    private fun installFromAssets() {
        val inputSteam = context.assets.open("$PATH_NAME/$DATABASE_NAME.db")
        try {
            val outputFile = File(context.getDatabasePath(DATABASE_NAME).absolutePath)
            val outputStream = FileOutputStream(outputFile)

            inputSteam.copyTo(outputStream)
            inputSteam.close()

            outputStream.flush()
            outputStream.close()

        } catch (exception: Throwable) {
            throw RuntimeException("The $DATABASE_NAME couldn't be found")
        }
    }
}

open class DatabaseController(context: Context) {
    private val database: SQLiteDatabase

    companion object {
        const val TABLE_NAME = "locations"
    }

    init {
        val helper = DatabaseHelper(context)
        database = helper.readableDatabase
    }

    fun getRandomLocations(): List<AstroLocation> {
        val query = """
            SELECT * 
            FROM $TABLE_NAME
            ORDER BY RANDOM() LIMIT 10
        """.trimIndent()
        val cities = mutableListOf<AstroLocation>()
        val cursor = database.rawQuery(query, null)
        while (!cursor.isClosed && cursor.moveToNext()) {
            cities.add(AstroLocation(cursor))
        }
        cursor.close()
        return cities
    }

    fun getLocationById(id: Int): AstroLocation? {
        val query = """
            SELECT *
            FROM $TABLE_NAME
            WHERE ${AstroLocation.Columns.ID.label} = '$id'
        """.trimIndent()
        var city: AstroLocation? = null
        val cursor = database.rawQuery(query, null)
        while (cursor.moveToNext()) {
            city = AstroLocation(cursor)
        }
        cursor.close()
        return city
    }
}