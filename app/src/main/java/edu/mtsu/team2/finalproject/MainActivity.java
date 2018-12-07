package edu.mtsu.team2.finalproject;

import android.app.Activity;
import android.os.Bundle;
import android.os.IBinder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.view.MenuItem;
import android.view.View;
import android.widget.MediaController.MediaPlayerControl;

public class MainActivity extends Activity implements MediaPlayerControl {

    private MusicService mS;
    private Intent playIntent;
    private boolean connectted = false;
    private MusicController controller;
    private boolean paused=false;
    private boolean playbackPaused=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setController();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(playIntent==null){
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnnect, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }

    private ServiceConnection musicConnnect = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            mS = binder.getService();
            mS.getSongs(songList); //UNKNOWN VARIABLE
            connectted = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            connectted = false;
        }
    };

    public void songPicked(View view){
        mS.setSong(Integer.parseInt(view.getTag().toString()));
        mS.playSong();
        if(playbackPaused){
            setController();
            playbackPaused=false;
        }
        controller.show(0);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_end: //closing app
                stopService(playIntent);
                mS = null;
                System.exit(0);
                break;
            case R.id.action_shuffle: //shuffling
                mS.setShuffle();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //if app is destroyed
    @Override
    protected void onDestroy() {
        stopService(playIntent);
        mS=null;
        super.onDestroy();
    }

    private void setController(){
        controller = new MusicController(this);
        controller.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNext();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPrev();
            }
        });

        controller.setMediaPlayer(this);
        controller.setAnchorView(findViewById(R.id.songList));
        controller.setEnabled(true);
    }



    @Override
    public void start() {
        mS.go();
    }

    @Override
    public void pause() {
        playbackPaused=true;
        mS.pausePlayer();
    }

    @Override
    public int getDuration() {
        if(mS!=null & connectted & mS.isPng()){
            return mS.getDur();
        }else {return 0;}
    }

    @Override
    public int getCurrentPosition() {
        if(mS!=null & connectted & mS.isPng()){
            return mS.getPosn();
        }else {
            return 0;}
    }

    @Override
    public void seekTo(int pos) {
        mS.seek(pos);
    }

    @Override
    public boolean isPlaying() {
        if(mS!=null & connectted)
            return mS.isPng();
        return false;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    //play next
    private void playNext(){
        mS.playNext();
        if(playbackPaused){
            setController();
            playbackPaused=false;
        }
        controller.show(0);

    }

    //play previous
    private void playPrev(){
        mS.playPrev();
        if(playbackPaused){
            setController();
            playbackPaused=false;
        }
        controller.show(0);
    }

    @Override
    protected void onPause(){
        super.onPause();
        paused=true;
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(paused){
            setController();
            paused=false;
        }
    }

    @Override
    protected void onStop() {
        controller.hide();
        super.onStop();
    }



}
