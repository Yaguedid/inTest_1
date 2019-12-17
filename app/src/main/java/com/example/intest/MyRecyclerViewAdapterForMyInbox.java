package com.example.intest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.List;

public class MyRecyclerViewAdapterForMyInbox  extends RecyclerView.Adapter<MyRecyclerViewAdapterForMyInbox.ViewHolder> {

    private List<String> NameOfCandidate;
    private List<String> MatchingAvList;
    private LayoutInflater mInflater;
    private MyRecyclerViewAdapterForMyInbox.ItemClickListener mClickListener;
    private static DecimalFormat df2 = new DecimalFormat("#.#");

    // data is passed into the constructor
    MyRecyclerViewAdapterForMyInbox(Context context, List<String> data, List<String> matchingAv) {
        this.mInflater = LayoutInflater.from(context);
        this.NameOfCandidate = data;
        this.MatchingAvList=matchingAv;
    }

    // inflates the row layout from xml when needed
    @Override
    public MyRecyclerViewAdapterForMyInbox.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_layout, parent, false);
        return new MyRecyclerViewAdapterForMyInbox.ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(MyRecyclerViewAdapterForMyInbox.ViewHolder holder, int position) {
        String candidateName = NameOfCandidate.get(position);
        String average=MatchingAvList.get(position);
        holder.offerIdTitleView.setText(candidateName);
        double avragedouble=Double.valueOf(average);
        holder.matchingOfferAv.setText(df2.format(avragedouble)+"%");
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return NameOfCandidate.size();

    }
public void delete(int position)
{
    MatchingAvList.remove(position);
    NameOfCandidate.remove(position);
    notifyItemRemoved(position);

}

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView offerIdTitleView;
        TextView matchingOfferAv;
        Button deleteButt;


        ViewHolder(View itemView) {
            super(itemView);
            offerIdTitleView = itemView.findViewById(R.id.OfferTitleId);
            deleteButt=itemView.findViewById(R.id.deleteButt);
            matchingOfferAv=itemView.findViewById(R.id.MatchinAvId);
            itemView.setOnClickListener(this);
            deleteButt.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(view.getId()==R.id.deleteButt)
            {
                InboxForEmployers.getInstance().addToListRemove(getAdapterPosition());
                delete(getAdapterPosition());

            }else
            {
                if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
            }

        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return NameOfCandidate.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(MyRecyclerViewAdapterForMyInbox.ItemClickListener
    itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}

