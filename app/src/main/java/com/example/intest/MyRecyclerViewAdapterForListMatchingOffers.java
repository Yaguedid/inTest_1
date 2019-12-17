package com.example.intest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.List;

public class MyRecyclerViewAdapterForListMatchingOffers extends RecyclerView.Adapter<MyRecyclerViewAdapterForListMatchingOffers.ViewHolder> {

    private List<String> OfferTitle;
    private List<String> MatchingAvList;
    private List<String> PicturesList;
    private List<String> CitiesList;
    private List<String> PaidOrNotList;
    private List<String> StartsAtList;
    private List<String> ReleaseDateList;
    private List<String> PeriodeList;
    private List<String> CompanyNameList;


    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private static DecimalFormat df2 = new DecimalFormat("#.#");

    // data is passed into the constructor
    MyRecyclerViewAdapterForListMatchingOffers(Context context, List<String> data, List<String> matchingAv,List<String> logos,
     List<String> cities,List<String> paidornot,List<String> startsat,List<String> release,List<String> periode,List<String> companyname) {


        this.mInflater = LayoutInflater.from(context);
        this.OfferTitle = data;
        this.MatchingAvList=matchingAv;
        this.PicturesList=logos;
        this.CitiesList=cities;
        this.PaidOrNotList=paidornot;
        this.StartsAtList=startsat;
        this.ReleaseDateList=release;
        this.PeriodeList=periode;
        this.CompanyNameList=companyname;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_layout_matching_offers, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String offerTitle = OfferTitle.get(position);
        String average=MatchingAvList.get(position);
        String paidOrNot=PaidOrNotList.get(position);
        String city=CitiesList.get(position);
        String startingDate=StartsAtList.get(position);
        String releaseDate=ReleaseDateList.get(position);
        String iconUrl=PicturesList.get(position);
        String periode=PeriodeList.get(position);
        String companyName=CompanyNameList.get(position);
        holder.CompanyNameView.setText(companyName);
        holder.PeriodeView.setText(periode);
        holder.offerIdTitleView.setText(offerTitle);
        holder.CityView.setText(city);
        if(paidOrNot.equals("yes"))
            holder.PaidOrNotView.setText("Payé");
            else
            holder.PaidOrNotView.setText("Non Payé");
            holder.StartsAtView.setText(startingDate);
            holder.ReleaseDateView.setText(releaseDate);
        double avragedouble=Double.valueOf(average);
        holder.matchingOfferAv.setText(df2.format(avragedouble) + "%");
        new MyRecyclerViewAdapterForListMatchingOffers.DownloadImageTask((ImageView)holder.LogoView)
                .execute(iconUrl);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return OfferTitle.size();
    }
    public void delete(int position)
    {
        MatchingAvList.remove(position);
        OfferTitle.remove(position);
        notifyItemRemoved(position);

    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView offerIdTitleView;
        TextView matchingOfferAv;
        ImageView LogoView;
        TextView CityView,PaidOrNotView,StartsAtView,ReleaseDateView,PeriodeView,CompanyNameView;



        ViewHolder(View itemView) {
            super(itemView);
            offerIdTitleView = itemView.findViewById(R.id.OfferTitleId);
            matchingOfferAv=itemView.findViewById(R.id.MatchinAvId);
            LogoView=itemView.findViewById(R.id.logoCompany);
            CityView=itemView.findViewById(R.id.CityOffer);
            PaidOrNotView=itemView.findViewById(R.id.paidOrNot);
            StartsAtView=itemView.findViewById(R.id.startsdate);
            ReleaseDateView=itemView.findViewById(R.id.DateOffer);
            PeriodeView=itemView.findViewById(R.id.periode);
            CompanyNameView=itemView.findViewById(R.id.CompanyName);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

                if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());

        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return OfferTitle.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}

