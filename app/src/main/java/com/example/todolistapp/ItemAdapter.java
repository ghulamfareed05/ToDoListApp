package com.example.todolistapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemHolder> {

    private ArrayList<ReminderModel> modelArrayList;
    private Context mContext;
    private ItemClickListener itemClickListener;
    public ItemAdapter(ArrayList<ReminderModel> reminderModels, Context context) {
        this.mContext = context;
        this.modelArrayList = reminderModels;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.list_item,parent,false);

        ItemHolder itemHolder = new ItemHolder(view);
        return itemHolder;
    }
    private String cutString(String str){
        if(str.length()>25){
            return str.substring(0,25);
        }
        return str;
    }
    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.counter.setText(String.valueOf(position+1));
        holder.title.setText(cutString(modelArrayList.get(position).getNotes()));
        holder.date.setText(modelArrayList.get(position).getDueDate());
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onDeleteItem(modelArrayList.get(position),position);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClicked(modelArrayList.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public void setOnItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void remove(int pos) {
        modelArrayList.remove(pos);
        notifyDataSetChanged();
    }

    class ItemHolder extends RecyclerView.ViewHolder{
            TextView counter;
            TextView title;
            TextView date;
            CheckBox checkBox;
        public ItemHolder(View itemView) {
            super(itemView);
            counter = itemView.findViewById(R.id.counter);
            title = itemView.findViewById(R.id.title_text);
            date = itemView.findViewById(R.id.current_date);
            checkBox=itemView.findViewById(R.id.checkbox);
        }
    }
    public interface ItemClickListener{
        void onItemClicked(ReminderModel reminderModel);
        void onDeleteItem(ReminderModel reminderModel,int pos);
    }
}
