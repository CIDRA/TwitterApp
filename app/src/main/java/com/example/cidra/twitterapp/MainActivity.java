package com.example.cidra.twitterapp;

import twitter4j.AsyncTwitter;
import twitter4j.AsyncTwitterFactory;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.TwitterAdapter;
import twitter4j.TwitterListener;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.view.Menu;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity {

    private final String API_KEY = "8HHCk3sis7MvZNLJx8tlVfNZZ";
    private final String API_SECRET = "HeymQMo0UhWSLwaONEdWGkPDKE09qv7FAuK9BK0Cfwz4TmtG1y";

    private AsyncTwitter mTwitter;
    private RequestToken mReqToken;

    private final TwitterListener mListener = new TwitterAdapter() {
        @Override
        public void gotOAuthRequestToken(RequestToken token) {
            mReqToken = token;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mReqToken.getAuthorizationURL()));
            startActivity(intent);
        }

        @Override
        public void gotOAuthAccessToken(AccessToken token) {
            //token.getToken()とtoken.getTokenSecret()を保存する
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTwitter = new AsyncTwitterFactory().getInstance();
        mTwitter.addListener(mListener);
        mTwitter.setOAuthConsumer(API_KEY, API_SECRET);
        mTwitter.getOAuthRequestTokenAsync("twittercallback://callback");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        //ブラウザからのコールバックで呼ばれる
        final Uri uri = intent.getData();
        final String verifier = uri.getQueryParameter("oauth_verifier");
        if (verifier != null) {
            mTwitter.getOAuthAccessTokenAsync(mReqToken, verifier);
        }
    }


}
