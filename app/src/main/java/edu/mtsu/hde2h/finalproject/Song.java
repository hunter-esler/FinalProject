package edu.mtsu.hde2h.finalproject;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class Song {
    private long id;
    private String path;
    private String title;
    private String album;
    private String artist;
    private long albumId;
    private long artistId;
    private long duration;
    private int trackNumber;
    private Uri uri;


    public Song(Cursor cursor) {
        id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
        path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
        title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
        album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
        artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
        albumId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
        artistId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID));
        duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
        trackNumber = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.TRACK));
        uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
    }
}
