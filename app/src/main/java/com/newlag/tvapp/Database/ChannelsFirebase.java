package com.newlag.tvapp.Database;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.newlag.tvapp.Models.Channel;

import java.util.ArrayList;

public class ChannelsFirebase {

    private String path = "channels";

    public void loadChannels(final onChannelsLoaded callback) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(path);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ArrayList<Channel> channels = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    channels.add(dataSnapshot.getValue(Channel.class));
                }

                callback.onSucess(channels);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onFailure();
            }
        });
    }

    public interface onChannelsLoaded {
        void onSucess(ArrayList<Channel> channels);
        void onFailure();
    }
}
