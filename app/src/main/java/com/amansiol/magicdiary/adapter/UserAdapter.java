package com.amansiol.magicdiary.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amansiol.magicdiary.ChatActivity;
import com.amansiol.magicdiary.R;
import com.amansiol.magicdiary.model.User;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    Context context;
    List<User> Users_list;

    public UserAdapter(Context context, List<User> users_list) {
        this.context = context;
        Users_list = users_list;
    }

    @NonNull
    @NotNull
    @Override
    public UserAdapter.UserViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View mView= LayoutInflater.from(parent.getContext()).inflate(R.layout.oneuser,parent,false);
        return new UserViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull UserAdapter.UserViewHolder holder, int i) {

        final String his_UID= Users_list.get(i).getUID();
        final String user_name=Users_list.get(i).getName();
        final String user_image=Users_list.get(i).getImage();
        holder.name.setText(Users_list.get(i).getName());
        holder.status.setText(Users_list.get(i).getStatus());
        try{
            Picasso.get().load(Users_list.get(i).getImage()).into(holder.image);
        }
        catch (Exception e){
            Picasso.get().load(R.drawable.profile).into(holder.image);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(AllUserActivity.alluserActivityvar){

                    Intent intent=new Intent(context, ChatActivity.class);
                    intent.putExtra("hisUID",his_UID);
                    context.startActivity(intent);

//                    Animatoo.animateInAndOut(context);
//                }
//                else {
////                    Intent intent=new Intent(context,OtherProfileActivity.class);
////                    intent.putExtra("hisUID",his_UID);
////                    context.startActivity(intent);
////                    Animatoo.animateInAndOut(context);
//                }

            }
        });
//        holder.image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                AlertDialog.Builder image=new AlertDialog.Builder(context);
//                View dialog=LayoutInflater.from(context).inflate(R.layout.showuserimage,null);
//                ImageView imageView=dialog.findViewById(R.id.dialog_user_image);
//                try{
//                    Picasso.get().load(user_image).into(imageView);
//                }catch (Exception e){
//                    Picasso.get().load(R.drawable.profilepic).into(imageView);
//                }
//
//                image.setView(dialog);
//                image.setCancelable(true);
//                image.create().show();
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return Users_list.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
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
