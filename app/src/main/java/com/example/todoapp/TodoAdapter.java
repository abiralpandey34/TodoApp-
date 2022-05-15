package com.example.todoapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.media.Image;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder>{

    Context context;
    private List<Todo> data;
    RoomDB database;

    public TodoAdapter(List<Todo> data, Context context) {
        this.data = data;
        this.context = context;
        this.database = RoomDB.getInstance(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Todo item = data.get(position);

        holder.todoTitle.setText(item.getTitle());
        holder.completed.setChecked(item.isCompleted());
        holder.createdDate.setText(item.getCreatedDate());
        holder.favourite.setChecked(item.isFavourite());

        // Setting on click Action listener on To-do List items.
        holder.adapterContainer.setOnClickListener(new View.OnClickListener(){
            private Todo currentTodo;

            @Override
            public void onClick(View view) {
                Intent updateIntent = new Intent(TodoAdapter.this.context, AddTodoActivity.class);
                updateIntent.putExtra("TODO_CLASS", this.currentTodo);
                context.startActivity(updateIntent);
            }

            private View.OnClickListener init(Todo todo){
                this.currentTodo = todo;
                return this;
            }

        }.init(item));

        //Making text strike-through if checkbox is ticked.
        if(item.isCompleted()){
            holder.todoTitle.setPaintFlags(holder.todoTitle.getPaintFlags() |     Paint.STRIKE_THRU_TEXT_FLAG);
        }

        // Setting on-click Action Listener on Favourite Item
        holder.favourite.setOnClickListener(new View.OnClickListener(){

            private Todo currentTodo;
            private ViewHolder holder;
            private Context context;

            @Override
            public void onClick(View view) {

                currentTodo.setFavourite(!currentTodo.isFavourite());

                if(currentTodo.isFavourite()){
                    Toast.makeText(context, "Added to Favourites", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "Removed from Favourites", Toast.LENGTH_SHORT).show();
                }

                database.todoDao().update(currentTodo);
            }

            private View.OnClickListener init(Todo todo, ViewHolder holder, Context context){
                this.currentTodo = todo;
                this.holder = holder;
                this.context= context;
                return this;
            }
        }.init(item, holder, context));

        //Setting on click Action Listener on checkbox item
        holder.completed.setOnClickListener(new View.OnClickListener(){
            private Todo currentTodo;
            private ViewHolder holder;
            private Context context;

            @Override
            public void onClick(View view) {
                currentTodo.setCompleted(!currentTodo.isCompleted());
                database.todoDao().update(currentTodo);

                //This if-else block is responsible for add & removing strike-through on text when checkbox is enabled or disabled.
                if(currentTodo.isCompleted()){
                    holder.todoTitle.setPaintFlags(holder.todoTitle.getPaintFlags() |     Paint.STRIKE_THRU_TEXT_FLAG);
                    Toast.makeText(context, "Completed", Toast.LENGTH_SHORT).show();
                } else{ holder.todoTitle.setPaintFlags(holder.todoTitle.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG)); }
            }

            private View.OnClickListener init(Todo todo, ViewHolder holder, Context context){
                this.currentTodo = todo;
                this.holder = holder;
                this.context= context;
                return this;
            }
        }.init(item, holder, context));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout adapterContainer;
        TextView todoTitle;
        TextView createdDate;
        CheckBox completed;
        CheckBox favourite;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.adapterContainer = itemView.findViewById(R.id.adapter_container);
            this.todoTitle = itemView.findViewById(R.id.todo_title_text_view);
            this.createdDate = itemView.findViewById(R.id.todo_date_text_view);
            this.completed = itemView.findViewById(R.id.todo_completed_check_box);
            this.favourite = itemView.findViewById(R.id.favourite_button);

        }
    }

    


}
