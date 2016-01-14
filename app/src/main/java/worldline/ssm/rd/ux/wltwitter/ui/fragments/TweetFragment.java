package worldline.ssm.rd.ux.wltwitter.ui.fragments;

import worldline.ssm.rd.ux.wltwitter.R;
import worldline.ssm.rd.ux.wltwitter.utils.Constants;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TweetFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_wltwitter_tweet, container, false);
		
		// Set the name
		final String name = getArguments().getString(Constants.Tweet.EXTRA_NAME);
		((TextView) view.findViewById(R.id.tweetNameTextView)).setText(name);
		
		// Set the alias
		final String alias = getArguments().getString(Constants.Tweet.EXTRA_ALIAS);
		((TextView) view.findViewById(R.id.tweetAliasTextView)).setText("@" + alias);
		
		// Set the text
		final String text = getArguments().getString(Constants.Tweet.EXTRA_TEXT);
		((TextView) view.findViewById(R.id.tweetTextTextView)).setText(text);
		
		// Set the image
//		final String imageUrl = getArguments().getString(Constants.Tweet.EXTRA_IMAGEURL);
//		final ImageView imageView = (ImageView) view.findViewById(R.id.tweetImageView);
//		new DownloadImageAsyncTask(imageView, null).execute(imageUrl);
		
		return view;
	}
	
}
