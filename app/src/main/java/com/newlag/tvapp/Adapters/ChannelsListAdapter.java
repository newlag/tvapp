package com.newlag.tvapp.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.newlag.tvapp.Activites.ChannelActivity;
import com.newlag.tvapp.Models.Channel;
import com.newlag.tvapp.R;

import java.util.ArrayList;

public class ChannelsListAdapter extends RecyclerView.Adapter<ChannelsListAdapter.ChannelsListHolder> {

    private Context context;
    private ArrayList<Channel> channels = new ArrayList<>();
    private LayoutInflater inflater;

    public ChannelsListAdapter(Context context, ArrayList<Channel> channels, LayoutInflater inflater) {
        this.context = context;
        this.channels = channels;
        this.inflater = inflater;
    }


    @NonNull
    @Override
    public ChannelsListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(context.getResources().getLayout(R.layout.channel_item), parent, false);
        return new ChannelsListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChannelsListHolder holder, int position) {
        Log.i("лолкек", channels.get(position).getName());
        holder.Bind(channels.get(position));
    }



    @Override
    public int getItemCount() {
        return channels.size();
    }

    public class ChannelsListHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private ImageView card;

        public ChannelsListHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.channel_title);
            card = itemView.findViewById(R.id.channel_card);
        }
        public void Bind(final Channel channel) {
            Resources res = context.getResources();
            title.setText(channel.getName());
            Glide.with(context)
                .load(channel.getImageUrl())
                .into(card);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (channel != null) context.startActivity(ChannelActivity.newInstance(context, channel));
                }
            });
        }
    }
}
