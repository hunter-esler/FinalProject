package edu.mtsu.hde2h.finalproject;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.Random;

public class SongList {

    private ArrayList<Song> songList;
    private ArrayList<Integer> songHistory;

    private int currentSong = 0;
    private int nextSong = 0;
    private boolean hasNextSong = false;
    private boolean shuffle = false;
    private Random r;

    public SongList() {
        songList = new ArrayList<>();
        songHistory = new ArrayList<>();
        r = new Random();
    }

    public void addSong(Cursor cursor) {
        songList.add(new Song(cursor));
    }

    //sets the next song and returns given song
    public Song nextSong() {
        int song;
        if (hasNextSong) {
            song = nextSong;
        } else {
            hasNextSong = true;
            if (shuffle) {
                nextSong = r.nextInt(songList.size()-1);
            } else {
                nextSong = nextSong + 1;
                if (nextSong > songList.size() - 1)
                    nextSong = 0;
            }
            song = nextSong;
        }
        songHistory.add(currentSong);
        currentSong = song;

        return songList.get(song);
    }

    //looks through history and plays previous song. if no song returns current song
    public Song previousSong() {
        if (songHistory.size() > 0) {
            currentSong = songHistory.get(songHistory.size() - 1);
            songHistory.remove(songHistory.size() - 1);
        }
        return songList.get(currentSong);
    }

    public Song getCurrentSong() {
        return songList.get(currentSong);
    }

    public boolean isShuffle() {
        return shuffle;
    }

    public void setShuffle(boolean shuffle) {
        this.shuffle = shuffle;
    }


}
