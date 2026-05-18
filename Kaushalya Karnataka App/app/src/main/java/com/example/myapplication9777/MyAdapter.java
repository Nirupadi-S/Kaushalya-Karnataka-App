package com.example.myapplication9777;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private Context context;
    private List<DataClass> datalist;

    public MyAdapter(Context context, List<DataClass> datalist) {
        this.context = context;
        this.datalist = datalist;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Glide.with(context)
                .load(datalist.get(position).getDataImage())
                .into(holder.recImage);

        holder.recName.setText(
                datalist.get(position).getWorkerName()
        );

        holder.recWorkerId.setText(
                datalist.get(position).getWorkerId()
        );

        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, DetailActivity.class);

                intent.putExtra(
                        "Image",
                        datalist.get(holder.getAdapterPosition()).getDataImage()
                );

                intent.putExtra(
                        "WorkerName",
                        datalist.get(holder.getAdapterPosition()).getWorkerName()
                );

                intent.putExtra(
                        "WorkerId",
                        datalist.get(holder.getAdapterPosition()).getWorkerId()
                );

                intent.putExtra(
                        "Department",
                        datalist.get(holder.getAdapterPosition()).getDepartment()
                );

                intent.putExtra(
                        "Salary",
                        datalist.get(holder.getAdapterPosition()).getSalary()
                );

                intent.putExtra(
                        "PhoneNumber",
                        datalist.get(holder.getAdapterPosition()).getPhoneNumber()
                );

                intent.putExtra(
                        "Shift",
                        datalist.get(holder.getAdapterPosition()).getShift()
                );

                intent.putExtra(
                        "Key",
                        datalist.get(holder.getAdapterPosition()).getKey()
                );

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    public void searchDataList(ArrayList<DataClass> searchList) {

        datalist = searchList;

        notifyDataSetChanged();
    }
}

class MyViewHolder extends RecyclerView.ViewHolder {

    ImageView recImage;

    TextView recWorkerId, recName;

    CardView recCard;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        recImage = itemView.findViewById(R.id.recImage);

        recWorkerId = itemView.findViewById(R.id.recRegNo);

        recName = itemView.findViewById(R.id.recName);

        recCard = itemView.findViewById(R.id.recCard);
    }
}