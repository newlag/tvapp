package com.newlag.tvapp.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.newlag.tvapp.Database.ChannelPresenter;
import com.newlag.tvapp.Models.Channel;
import com.newlag.tvapp.R;
import com.newlag.tvapp.VideoControllerView;


public class ChannelActivity extends AppCompatActivity implements SurfaceHolder.Callback, MediaPlayer.OnPreparedListener, VideoControllerView.MediaPlayerControl {

    private static final String CHANNEL_EXTRA = "CHANNEL";

    private SurfaceView videoView;

    private Channel channel;
    private Button favouriteButton;

    private ChannelPresenter presenter;
    private boolean isFavourite;

    private MediaPlayer player;
    private VideoControllerView controller;
    boolean isFullScrn = false;

    public static Intent newInstance(Context context, Channel channel) {
        Intent i = new Intent(context, ChannelActivity.class);
        Bundle args = new Bundle();
        args.putParcelable(CHANNEL_EXTRA, channel);
        i.putExtras(args);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);

        presenter = new ChannelPresenter(this);
        Bundle args = getIntent().getExtras();
        channel = args.getParcelable(CHANNEL_EXTRA);
        init();
        play();
    }

    private void init() {

        videoView = findViewById(R.id.video_view);
        ImageView channelCard = findViewById(R.id.channel_card);
        favouriteButton = findViewById(R.id.btn_fav);
        TextView title = findViewById(R.id.channel_title);
        TextView description = findViewById(R.id.channel_description);

        updateButton();


        Glide.with(this)
            .load(channel.getImageUrl())
            .into(channelCard);

        title.setText(channel.getName());
        description.setText(channel.getDescription());

        favouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFavourite) {
                    presenter.addChannel(channel.getName());
                } else {
                    presenter.removeChannel(channel.getName());
                }
                updateButton();
            }
        });
    }

    private void play() {
        SurfaceHolder videoHoleder = videoView.getHolder();
        videoHoleder.addCallback(this);
        player = new MediaPlayer();
        controller = new VideoControllerView(this);
        try {
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setDataSource(this, Uri.parse(channel.getStreamUrl()));
            player.setOnPreparedListener(this);
            player.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    finish();
                    return true;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    private void updateButton() {
        isFavourite = presenter.checkChannel(channel.getName());

        if (!isFavourite) {
            favouriteButton.setText(R.string.add_to_fav_title);
            favouriteButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        } else {
            favouriteButton.setText(R.string.rmv_frm_fav);
            favouriteButton.setBackgroundColor(getResources().getColor(R.color.colorGrey));
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) onBackPressed();
        return true;
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        player.setDisplay(holder);
        player.prepareAsync();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        controller.setMediaPlayer(this);
        controller.setAnchorView((FrameLayout) findViewById(R.id.video_view_container));
        updatePlayerSize();
        player.start();
    }

    @Override
    public void start() {
        player.start();
    }

    @Override
    public void pause() {
        player.pause();
    }



    @Override
    public boolean isPlaying() {
        return player.isPlaying();
    }

    @Override
    public boolean isFullScreen() {
        return isFullScrn;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        controller.show();
        return false;
    }

    @Override
    public void toggleFullScreen() {
        setVideoSize();
    }

    private void setVideoSize() {
        if (!isFullScrn) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        }
        isFullScrn = !isFullScrn;

    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        updatePlayerSize();
    }

    private void updatePlayerSize() {
        int videoWidth = player.getVideoWidth();
        int videoHeight = player.getVideoHeight();
        float videoProportion = (float) videoWidth / (float) videoHeight;

        int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        int screenHeight = getWindowManager().getDefaultDisplay().getHeight();
        float screenProportion = (float) screenWidth / (float) screenHeight;

        android.view.ViewGroup.LayoutParams lp = videoView.getLayoutParams();
        if (videoProportion > screenProportion) {
            lp.width = screenWidth;
            lp.height = (int) ((float) screenWidth / videoProportion);
        } else {
            lp.width = (int) (videoProportion * (float) screenHeight);
            lp.height = screenHeight;
        }
        videoView.setLayoutParams(lp);
    }

    @Override
    protected void onStop() {
        super.onStop();
        player.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.reset();
    }
}