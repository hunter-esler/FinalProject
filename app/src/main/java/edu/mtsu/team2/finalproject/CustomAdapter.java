package edu.mtsu.team2.finalproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

//https://www.simplifiedcoding.net/custom-listview-android/
public class CustomAdapter extends ArrayAdapter<Song> {

    SongList list;

    Context context;

    int resource;

    public CustomAdapter(Context context, int resource, SongList list) {
        super(context, resource, list.getSongs());
        this.context = context;
        this.resource = resource;
        this.list = list;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(resource, null, false);

        ImageView imageView = view.findViewById(R.id.imageView);
        TextView title = view.findViewById(R.id.textView_title);
        TextView artist = view.findViewById(R.id.textView_artist);

        Song song = list.getSongs().get(position);
        try {
            Bitmap bm = BitmapFactory.decodeFile(song.getAlbumPath());
            imageView.setImageBitmap(bm);
        } catch (Exception e) {
            //do nothing
        }
        title.setText(song.getTitle());
        artist.setText(song.getArtist());

        return view;

    }

}
