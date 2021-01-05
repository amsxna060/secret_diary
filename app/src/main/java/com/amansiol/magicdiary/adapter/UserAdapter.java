package com.amansiol.magicdiary.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amansiol.magicdiary.R;
import com.github.siyamed.shapeimageview.CircularImageView;

import org.jetbrains.annotations.NotNull;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    @NonNull
    @NotNull
    @Override
    public UserAdapter.UserViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull UserAdapter.UserViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView status;
        CircularImageView image;
        public UserViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.allusers_name);
            status=itemView.findViewById(R.id.allusers_status);
            image=itemView.findViewById(R.id.allusers_pic);
        }

    }
}
