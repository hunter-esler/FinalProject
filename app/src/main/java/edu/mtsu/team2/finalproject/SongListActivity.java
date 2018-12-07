package edu.mtsu.team2.finalproject;

import android.Manifest;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
//help from https://www.youtube.com/watch?v=kf2fxYLOiSo
public class SongListActivity extends AppCompatActivity {//

    private static final int MY_PERMISSION_REQUEST = 1;

    ArrayList<String> arrayList;
    ArrayList<String> pathList;
    ListView listView;
    ArrayAdapter<String> adapter;
    public static SongList songList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_song_list);

        songList = new SongList();
        ActivityCompat.requestPermissions(SongListActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);

        listView = findViewById(R.id.songList);
        arrayList = new ArrayList<>();
        pathList = new ArrayList<>();
        getMusic();
        //adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        CustomAdapter adapter = new CustomAdapter(this, R.layout.list_item, songList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });


    }

    public void getMusic() {
        ContentResolver contentResolver = getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songCursor = contentResolver.query(songUri, null, null, null, null);

        if (songCursor != null && songCursor.moveToFirst()) {
            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int songDir = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            int id = songCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int albumIdi = songCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);

            do {
                String currentTitle = songCursor.getString(songTitle);
                String currentArtist = songCursor.getString(songArtist);
                String songId = songCursor.getString(id);
                int albumId = songCursor.getInt(albumIdi);


                //arrayList.add(currentTitle + "\n" + currentArtist);
                //pathList.add(songCursor.getString(songDir));
                Song s = songList.addSong(songCursor);

                Cursor acursor = getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                        new String[] {MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART},
                        MediaStore.Audio.Albums._ID+ "=?",
                        new String[] {String.valueOf(albumId)},
                        null);
                if (acursor.moveToFirst()) {
                    String albumPath = acursor.getString(acursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
                    s.setAlbumPath(albumPath);
                    System.out.println(albumPath);
                }


            } while (songCursor.moveToNext());
        }
    }
}

