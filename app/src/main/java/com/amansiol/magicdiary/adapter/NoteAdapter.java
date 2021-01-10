package com.amansiol.magicdiary.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amansiol.magicdiary.EditAndShowNotes;
import com.amansiol.magicdiary.R;
import com.amansiol.magicdiary.model.Note;
import com.google.firebase.Timestamp;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    Context context;
    ArrayList<Note> notes;

    public NoteAdapter(Context context, ArrayList<Note> notes) {
        this.context = context;
        this.notes = notes;
    }

    @NonNull
    @NotNull
    @Override
    public NoteAdapter.NoteViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.one_note,parent,false);
        return new NoteViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull NoteAdapter.NoteViewHolder holder, int position) {
       String title= notes.get(position).getTitle();
       String description= notes.get(position).getDescription();
       String timestamp = notes.get(position).getTimestamp();

        Calendar cal= Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(Long.parseLong(timestamp));
        final String date= DateFormat.format("dd/MM/yyyy",cal).toString();
        final String time= DateFormat.format("hh:mm aa",cal).toString();


       if(title!=null){
           if(title.length()>10)
           holder.heading.setText(title.substring(0,9)+"...");
           else if(title.isEmpty())
               holder.heading.setText("No Title");
           else
           holder.heading.setText(title);

       }else {

       }
       if(description!=null)
       {if(title.length()>24)
           holder.description.setText(description.substring(0,24)+"...");
       else
           holder.description.setText(description);
       }
       else {
           holder.heading.setText("No Written Yet");
       }
       holder.time.setText(time);
       holder.date.setText(date);

       holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent= new Intent(context, EditAndShowNotes.class);
               intent.putExtra("title",title);
               intent.putExtra("description",description);
               intent.putExtra("edit","edit");
               intent.putExtra("timestamp",timestamp);
               context.startActivity(intent);
           }
       });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView heading,description,time,date;
        public NoteViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            heading=itemView.findViewById(R.id.heading);
            description=itemView.findViewById(R.id.description);
            time=itemView.findViewById(R.id.time);
            date=itemView.findViewById(R.id.date);
        }
    }
}
