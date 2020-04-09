package com.e.s;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Find_peopleActivity extends AppCompatActivity {
private RecyclerView FindFriendList;
private EditText searchET;
@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_find_people);
        FindFriendList=findViewById (R.id.find_friend_list);
        searchET=findViewById (R.id.search_user_text);
    FindFriendList.setLayoutManager (new LinearLayoutManager (getApplicationContext ()));


}
    public static class FindfriendViewHolder extends RecyclerView.ViewHolder {
        TextView usernametxt;
        ImageView profileImageView;
        RelativeLayout cardview;
        Button videocallbtn;


        public FindfriendViewHolder(@NonNull View itemView) {
            super (itemView);
  usernametxt=itemView.findViewById (R.id.image_contact);
       videocallbtn=itemView.findViewById (R.id.call_btn);
            cardview = itemView.findViewById (R.id.card_view1);
            profileImageView = itemView.findViewById (R.id.image_contact);
        }
    }
}
