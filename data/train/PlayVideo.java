package com.appradar.wrestlingquiz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import models.YoutubeVideo;
import utils.AppConstants;
import utils.AppController;

public class PlayVideo extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private static final int RECOVERY_REQUEST = 1;
    private YouTubePlayerView youTubeView;
    private YoutubeVideo video;
    private TextView videoTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);

        fetchYoutubeUrls();

        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        videoTitle = (TextView) findViewById(R.id.videoTitle);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored) {
            Log.e("onInitializationSuccess", "The url is " + video.getVideoId());
            player.loadVideo(video.getVideoId()); // Plays https://www.youtube.com/watch?v=fhWaJi1Hsfo
            player.play();
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
            String error = String.format(getString(R.string.player_error), errorReason.toString());
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(AppConstants.YOUTUBE_API_KEY, this);
        }
    }


    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return youTubeView;
    }

    private void fetchYoutubeUrls() {
        DatabaseReference databaseReference = AppController.getInstance().getDatabaseReference();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                video = dataSnapshot.getValue(YoutubeVideo.class);
                Log.e("fetchYoutubeUrls", "The url is " + video.getVideoId());
                setVideoDetails(video);
                youTubeView.initialize(AppConstants.YOUTUBE_API_KEY, PlayVideo.this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("fetchYoutubeUrls", databaseError.toString());
            }
        });
    }

    private void setVideoDetails(YoutubeVideo youtubeVideo) {
        videoTitle.setText("" + youtubeVideo.getTitle());
    }
}