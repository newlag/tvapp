package com.newlag.tvapp;

import android.media.MediaPlayer;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.newlag.tvapp.Adapters.ChannelsListAdapter;
import com.newlag.tvapp.Database.ChannelPresenter;
import com.newlag.tvapp.Models.Channel;

import java.nio.channels.Channels;
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

        // Подгрузка списка каналов
        /*ArrayList<Channel> channels = new ArrayList<>();
        channels.add(new Channel(
            "https://sc.id-tv.kz/NatGeoWildHD_34_35.m3u8",
            "National Geographic",
            "National Geographic Channel (Nat Geo, канал National Geographic) — американский телеканал, транслирующий научно-популярные фильмы производства Национального географического общества США. Канал транслирует документальные фильмы, основное содержание которых чаще всего имеет отношение к науке, природе, культуре, истории и открытиях.",
            "https://sasquatchchronicles.com/wp-content/uploads/2019/08/national-geographic.png"
        ));
        channels.add(new Channel(
                "https://zabava-htlive.cdn.ngenix.net/hls/CH_ULLUSIONPLUS/bw2000000/variant.m3u8",
                "Дорама HD",
                "Философия телеканала «История» – это самый широкий взгляд на прошлое: от древнейших цивилизаций и великих открытий прошлого до величайших загадок и тайн настоящего, а также великие войны и победы, великие люди, великая любовь и предательство – всё то, что делает историю интересной и увлекательной. Программное наполнение телеканала состоит из продуктов собственного производства и лучших зарубежных форматов крупнейших мировых студий.",
                "https://lh3.googleusercontent.com/proxy/fW39HcBR9hB6qEA-n9FdvXaT8c8RV9ZNL1holNSvvU2Ky8EA20cXEuwv-TqGxzDpfC-1Wh_k8GbDr-6PitP8AWaJp1_b8xBNg6AY_GPygRca"
        ));*/

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
        // Подгрукза списка каналов


        return v;
    }



    private void init(View v) {
        recyclerView = v.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        //MediaPlayer mediaPlayer = new MediaPlayer.c
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
