package com.e.s;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NotificationActivity extends AppCompatActivity {
private  RecyclerView notification_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_notification);
        notification_list=findViewById (R.id.notificationrecyclerview);
        notification_list.setLayoutManager (new LinearLayoutManager (getApplicationContext ()));
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder
    {
        TextView usernametxt;
        ImageView profileImageView;
        RelativeLayout cardview;
        Button accceptbtn,cancelbtn;


        public NotificationViewHolder(@NonNull View itemView) {
            super (itemView);
        usernametxt=itemView.findViewById (R.id.name_notification);
            accceptbtn=itemView.findViewById (R.id.request_accept_btn);
            cancelbtn=itemView.findViewById (R.id.request_decline_btn);
            cardview=itemView.findViewById (R.id.card_view);
            profileImageView=itemView.findViewById (R.id.image_notification);
        }
    }

}
