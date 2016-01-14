package worldline.ssm.rd.ux.wltwitter.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by isen on 07/01/2016.
 */
public class WLTwitterDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "tweets.db";
    private static final int DATABASE_VERSION = 1;

    public WLTwitterDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //TODO Create the database
        db.execSQL(WLTwitterDatabaseContract.TABLE_TWEETS_CREATE_SCRIPT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO Upgrade the database
        db.execSQL("DROP TABLE IF EXISTS" + WLTwitterDatabaseContract.TABLE_TWEETS);

        //Recreate
        onCreate(db);
    }
}
