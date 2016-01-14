package worldline.ssm.rd.ux.wltwitter.providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import worldline.ssm.rd.ux.wltwitter.database.WLTwitterDatabaseContract;
import worldline.ssm.rd.ux.wltwitter.database.WLTwitterDatabaseHelper;
import worldline.ssm.rd.ux.wltwitter.utils.Constants;

/**
 * Created by isen on 07/01/2016.
 */
public class WLtwitterDatabaseProvider extends ContentProvider {

    private  static  final int TWEET_CORRECT_URI_CODE = 42;

    private WLTwitterDatabaseHelper mDBHelper;
    private UriMatcher uriMatcher;


    @Override
    public boolean onCreate() {
        mDBHelper = new WLTwitterDatabaseHelper(getContext());
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(WLTwitterDatabaseContract.CONTENT_PROVIDER_TWEETS_AUTHORITY, WLTwitterDatabaseContract.TABLE_TWEETS, TWEET_CORRECT_URI_CODE);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.v(Constants.General.LOG_TAG, "QUERY");
        return mDBHelper.getReadableDatabase().query(WLTwitterDatabaseContract.TABLE_TWEETS,projection,selection,selectionArgs,sortOrder,null,null);
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        if (uriMatcher.match(uri) == TWEET_CORRECT_URI_CODE){
            return WLTwitterDatabaseContract.TWEETS_CONTENT_TYPE;
        }
        throw new IllegalArgumentException("Unknown URI" + uri);
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.i(Constants.General.LOG_TAG, "INSERT");
        final SQLiteDatabase db = mDBHelper.getWritableDatabase();
        final long rowId = db.insert(WLTwitterDatabaseContract.TABLE_TWEETS, "", values);
        if (rowId > 0) {
            final Uri applicationUri = ContentUris.withAppendedId(WLTwitterDatabaseContract.TWEETS_URI, rowId);
            getContext().getContentResolver().notifyChange(applicationUri, null);
            return applicationUri;
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.e(Constants.General.LOG_TAG, "DELETE");
        final SQLiteDatabase db = mDBHelper.getWritableDatabase();
        final int count = db.delete(WLTwitterDatabaseContract.TABLE_TWEETS, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Log.d(Constants.General.LOG_TAG, "UPDATE");
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        int count = db.update(WLTwitterDatabaseContract.TABLE_TWEETS, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return  0;
    }
}
