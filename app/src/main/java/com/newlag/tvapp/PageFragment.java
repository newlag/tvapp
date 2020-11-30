package com.newlag.tvapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.newlag.tvapp.Adapters.ChannelsListAdapter;
import com.newlag.tvapp.Database.ChannelPresenter;
import com.newlag.tvapp.Models.Channel;

import java.util.ArrayList;

public class PageFragment extends Fragment {

    private static final String FRAGMENT_TYPE = "TYPE";
    private RecyclerView recyclerView;
    private int fragment_type;

    private ChannelPresenter presenter;
    private ArrayList<Channel> c;
    private ArrayList<Channel> channels;

    public static PageFragment newInstance(int type) {
        PageFragment fragment = new PageFragment();
        Bundle args = new Bundle();
        args.putInt(FRAGMENT_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_channels, container, false);
        init(v);

        presenter = new ChannelPresenter(getActivity());
        presenter.loadChannels(new ChannelPresenter.onChannelsLoaded() {
            @Override
            public void onSuccess(ArrayList<Channel> channels_list) {
                channels = channels_list;
                Bundle args = getArguments();
                fragment_type = args.getInt(FRAGMENT_TYPE);
                switch (fragment_type) {
                    case 0:
                        recyclerView.setAdapter(new ChannelsListAdapter(getActivity(), channels, getLayoutInflater()));
                        break;
                    case 1:

                        checkChannels();
                        break;
                }
            }

            @Override
            public void onFailure() {
                Toast.makeText(getActivity(), R.string.connection_error, Toast.LENGTH_SHORT).show();
            }
        });


        return v;
    }



    private void init(View v) {
        recyclerView = v.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
    }

    private void checkChannels() {
        c = new ArrayList<>();
        for (Channel channel : channels) {
            if (presenter.checkChannel(channel.getName())) {
                c.add(channel);
            }
        }
        recyclerView.setAdapter(new ChannelsListAdapter(getActivity(), c, getLayoutInflater()));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (fragment_type == 1) {
            if (c != null) {
                checkChannels();
            }

        }
    }
}
