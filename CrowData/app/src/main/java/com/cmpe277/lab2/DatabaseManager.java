package com.cmpe277.lab2;

import java.util.HashMap;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import static android.content.Context.MODE_PRIVATE;

public class DatabaseManager {

    /** Instance of SQLite database */
    private HashMap<Integer, SQLiteDatabase> hmap = new HashMap<Integer,
            SQLiteDatabase>();

    /** Number of times database opens is called.
     * Workaround: It is not decremented on DB close
     */
    private int numDbOpens = 0;

    /**
     * Constructor
     */
    DatabaseManager(){

    }

    /**
     * Opens an existing database (or) creates a new one
     * @param dbName - Database name
     * @param mode - Mode
     * @return  - Handle to database instance.
     */
    int DbMgr_openOrCreateDb(Activity activity, String dbName, int mode)
    {
        SQLiteDatabase db = activity.openOrCreateDatabase(dbName, MODE_PRIVATE,
                null);
        ++numDbOpens;
        hmap.put(numDbOpens,db);
        return numDbOpens;
    }

    /**
     * Used for creating table, inserting values into the table
     * @param handle - Database handle
     * @param command - Command
     * @return  - Handle to database instance.
     */
    void DbMgr_execSQL(int handle, String command)
    {
        SQLiteDatabase db = hmap.get(handle);
        db.execSQL(command);
    }

    /**
     * Execute raw query
     * @param handle - Database handle
     * @param query - Query
     * @return  Query results cursor
     */
    Cursor DbMgr_rawQuery(int handle, String query)
    {
        SQLiteDatabase db = hmap.get(handle);
        return db.rawQuery(query, null);
    }
}
