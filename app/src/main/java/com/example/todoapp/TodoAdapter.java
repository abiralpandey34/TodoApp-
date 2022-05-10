package com.example.todoapp;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder>{

    Context context;
    private List<Todo> data;
    public TodoAdapter(List<Todo> data, Context context) {
        this.data = data;
        this.context = context;
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

        // set on click listener
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

        //Action Listener on checkbox item
        holder.completed.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                item.
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout adapterContainer;
        TextView todoTitle;
        CheckBox completed;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.adapterContainer = itemView.findViewById(R.id.adapter_container);
            this.todoTitle = itemView.findViewById(R.id.todo_title_text_view);
            this.completed = itemView.findViewById(R.id.todo_completed_check_box);

        }
    }
}
