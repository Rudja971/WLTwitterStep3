package worldline.ssm.rd.ux.wltwitter.ui.fragments;

import java.util.ArrayList;
import java.util.List;

import worldline.ssm.rd.ux.wltwitter.R;
import worldline.ssm.rd.ux.wltwitter.WLTwitterApplication;
import worldline.ssm.rd.ux.wltwitter.adapters.TweetsAdapter;
import worldline.ssm.rd.ux.wltwitter.async.RetrieveTweetsAsyncTask;
import worldline.ssm.rd.ux.wltwitter.database.WLTwitterDatabaseContract;
import worldline.ssm.rd.ux.wltwitter.database.WLTwitterDatabaseHelper;
import worldline.ssm.rd.ux.wltwitter.database.WLTwitterDatabaseManager;
import worldline.ssm.rd.ux.wltwitter.interfaces.TweetChangeListener;
import worldline.ssm.rd.ux.wltwitter.interfaces.TweetListener;
import worldline.ssm.rd.ux.wltwitter.pojo.Tweet;
import worldline.ssm.rd.ux.wltwitter.utils.PreferenceUtils;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import static worldline.ssm.rd.ux.wltwitter.database.WLTwitterDatabaseManager.testDatabase;

public class TweetsFragment extends Fragment implements TweetChangeListener, OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {

	// Keep a reference to the AsyncTask
	private RetrieveTweetsAsyncTask mTweetAsyncTask;
	
	// Keep a reference to the ListView
	private ListView mListView;
	
	
	public TweetsFragment() {}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_wltwitter, container, false);
		
		// Get the ListView
		mListView = (ListView) rootView.findViewById(R.id.tweetsListView);
		
		// Set a Progress Bar as empty view, and display it (set adapter with no elements))
		final ProgressBar progressBar = new ProgressBar(getActivity());
        progressBar.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER));
        progressBar.setIndeterminate(true);
        mListView.setEmptyView(progressBar);
        
        // Add the view in our content view
        ViewGroup root = (ViewGroup) rootView.findViewById(R.id.tweetsRootRelativeLayout);
        root.addView(progressBar);
        
        // Set adapter with no elements to let the ListView display the empty view
        mListView.setAdapter(new ArrayAdapter<Tweet>(getActivity(), android.R.layout.simple_list_item_1, new ArrayList<Tweet>()));
		
        // Add a listener when an item is clicked
        mListView.setOnItemClickListener(this);

		//Utilisation de CURSORLOADER
		//Load data using CursorLoader
		getLoaderManager().initLoader(0,null,this);
        
		return rootView;
	}


	
	// Keep a reference to our Activiyt (implementing TweetListener)
	private TweetListener mListener;
		
	
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		if (activity instanceof TweetListener){
			mListener = (TweetListener) activity;
		}
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		// On start launch our AsyncTask to retrieve the Tweets of the user
		final String login = PreferenceUtils.getLogin();
		if (!TextUtils.isEmpty(login)){
			mTweetAsyncTask = new RetrieveTweetsAsyncTask(this);
			mTweetAsyncTask.execute(login);
		}
	}
	
	@Override
	public void onTweetRetrieved(List<Tweet> tweets) {
		// Set the adapter
		final TweetsAdapter adapter = new TweetsAdapter(tweets);
		adapter.setTweetListener(mListener);
		mListView.setAdapter(adapter);
		
		// Set our asynctask to null
		mTweetAsyncTask = null;

		//Après avoir trouvé l'ensemble de nos tweets, on les stocke simplement
		//dans la base de données en faisant appel à la méthode testDatabase
		testDatabase(tweets);
	}


	
	@Override
	public void onStop() {
		super.onStop();
		
		// If we have an AsyncTask running, close it
		if (null != mTweetAsyncTask){
			mTweetAsyncTask.cancel(true);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
		if (null != mListener){
			final Tweet tweet = (Tweet) adapter.getItemAtPosition(position);
			mListener.onViewTweet(tweet);
		}
	}

	//Methode pour creer le CursorLoader
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		final CursorLoader cursorLoader = new CursorLoader(WLTwitterApplication.getContext());
		cursorLoader.setUri(WLTwitterDatabaseContract.TWEETS_URI);
		cursorLoader.setProjection(WLTwitterDatabaseContract.PROJECTION_FULL);
		cursorLoader.setSelection(null);
		cursorLoader.setSelectionArgs(null);
		cursorLoader.setSortOrder(null);
		return cursorLoader;
	}

	//Méthode pour obtenir le résultat
	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		if (null != data){
			while (data.moveToNext()){
				final Tweet tweet = WLTwitterDatabaseManager.tweetFromCursor(data);
				Log.d("TweetsFragment", tweet.toString());
			}
			if(!data.isClosed()){
				data.close();
			}
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		//TODO Handle reset on CursorLoader
	}
}
