package com.amansiol.magicdiary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.amansiol.magicdiary.adapter.ChatListAdapter;
import com.amansiol.magicdiary.model.Chat;
import com.amansiol.magicdiary.model.Chatlist;
import com.amansiol.magicdiary.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChatListActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    RecyclerView recyclerView;
    List<Chatlist> chatlistModels;
    List<User> userList;
    DatabaseReference reference;
    FirebaseUser currentuser;
    ChatListAdapter adapterChatList;
    String theLastMessage="default";
    FloatingActionButton gotouserfab;
    ProgressBar chatlistprogress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        firebaseAuth=FirebaseAuth.getInstance();
        recyclerView=findViewById(R.id.chatlistRecyler);
        currentuser=firebaseAuth.getCurrentUser();
        chatlistModels=new ArrayList<>();
        gotouserfab=findViewById(R.id.gotouserfab);
        chatlistprogress=findViewById(R.id.chatlistprogressbar);


        reference= FirebaseDatabase.getInstance().getReference("Chatlist").child(currentuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatlistModels.clear();
                for(DataSnapshot ds:snapshot.getChildren()){
                    Chatlist chatlistModel=ds.getValue(Chatlist.class);
                    chatlistModels.add(chatlistModel);
                }

                loadChats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        gotouserfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AllUserActivity.class));
//                Animatoo.animateSlideUp(getActivity());
            }
        });


    }
    private void loadChats() {
        userList=new ArrayList<>();
        reference=FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for(DataSnapshot ds:snapshot.getChildren()){
                    User user=ds.getValue(User.class);
                    for(Chatlist chatlist:chatlistModels){
                        assert user != null;
                        if(user.getUID()!=null&&user.getUID().equals(chatlist.getId())){
                            userList.add(user);
                            break;
                        }
                    }
                    //adapter
                    adapterChatList=new ChatListAdapter(ChatListActivity.this,userList);
                    adapterChatList.notifyDataSetChanged();
                    recyclerView.setAdapter(adapterChatList);
                    for(int i=0;i<userList.size();i++){
                        lastMessage(userList.get(i).getUID());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void SearchChatList(final String s){
        userList=new ArrayList<>();
        reference=FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for(DataSnapshot ds:snapshot.getChildren()){
                    User user=ds.getValue(User.class);
                    for(Chatlist chatlist:chatlistModels){
                        assert user != null;
                        if(user.getUID()!=null&&user.getUID().equals(chatlist.getId())){
                            if(user.getName().toLowerCase().contains(s.toLowerCase())){
                                userList.add(user);
                                break;
                            }

                        }
                    }
                    //adapter
                    adapterChatList=new ChatListAdapter(getApplicationContext(),userList);
                    adapterChatList.notifyDataSetChanged();
                    recyclerView.setAdapter(adapterChatList);
                    for(int i=0;i<userList.size();i++){
                        lastMessage(userList.get(i).getUID());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void lastMessage(final String uid) {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Chats");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String theLastMessage="default";
                for(DataSnapshot ds: snapshot.getChildren()){
                    Chat chat=ds.getValue(Chat.class);
                    if(chat==null){
                        continue;
                    }
                    String sender=chat.getSender();
                    String receiver=chat.getReceiver();
                    if(sender==null||receiver==null){
                        continue;
                    }
                    if(chat.getReceiver().equals(currentuser.getUid())
                            &&chat.getSender().equals(uid)
                            ||chat.getReceiver().equals(uid)
                            && chat.getSender().equals(currentuser.getUid())){
                        theLastMessage=chat.getMessage();


                    }

                }
                adapterChatList.setLastMessageMap(uid,theLastMessage);
                adapterChatList.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}