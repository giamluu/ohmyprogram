package com.example.notificationapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MusicAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<Track> musicList;

    public MusicAdapter(Context context, int layout, List<Track> musicList) {
        this.context = context;
        this.layout = layout;
        this.musicList = musicList;
    }

    @Override
    public int getCount() {
        return musicList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder {
        ImageView musicImage, isPlay;
        TextView musicName, musicArtist;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(layout, null);

            viewHolder.musicName = convertView.findViewById(R.id.musicName);
            viewHolder.musicArtist = convertView.findViewById(R.id.musicArtist);
            viewHolder.musicImage = convertView.findViewById(R.id.img_music);
            viewHolder.isPlay = convertView.findViewById(R.id.isPlay);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }



        //gan gia tri
        Track track = musicList.get(position);

        viewHolder.musicName.setText(track.getTitle());
        viewHolder.musicArtist.setText(track.getArtist());
        viewHolder.musicImage.setImageResource(track.getImage());

        return convertView;
    }
}
