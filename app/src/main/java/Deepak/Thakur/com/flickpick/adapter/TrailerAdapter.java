package Deepak.Thakur.com.flickpick.adapter;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import Deepak.Thakur.com.flickpick.R;
import Deepak.Thakur.com.flickpick.model.Trailer;


public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerHolder>{

    private Trailer[] Mtrailer;
    private TextView Tv_Trailer_Adapter;
    private ImageView Iv_Trailer_Image;
    private Context mContext;



    public TrailerAdapter(Trailer[] videos, Context context){
        this.Mtrailer=videos;
        this.mContext= context;

    }

    @NonNull
    @Override
    public TrailerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_trailer, parent, false);

        return new TrailerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerHolder holder, @SuppressLint("RecyclerView") final int position) {
        String name= Mtrailer[position].getmName();
        final String key = Mtrailer[position].getmKey();
        Tv_Trailer_Adapter.append(name);
        final String Url_Base_Youtube_Video= "https://www.youtube.com/watch?v=";
        String Url_Base_Youtube_Image = "http://i3.ytimg.com/vi/";
        final String Url_Base_Video = "vnd.youtube:";
        final String Url_Video= String.valueOf(Url_Base_Youtube_Video.concat(key));
        final String UImage = Url_Base_Youtube_Image.concat((key).concat("/1.jpg"));
        Picasso.with(mContext)
                .load(UImage)
                .into(Iv_Trailer_Image);
         Iv_Trailer_Image.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Animation shake = AnimationUtils.loadAnimation(mContext, R.anim.shake);
                 v.startAnimation(shake);
                 CountDownTimer countDownTimer= new CountDownTimer(70, 10) {
                     @Override
                     public void onTick(long millisUntilFinished) {
                     }
                     @Override
                     public void onFinish() {
                         Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Url_Base_Video.concat(key)));
                         Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Url_Video));
                         try {
                             mContext.startActivity(appIntent);
                         } catch (ActivityNotFoundException ex) {
                             mContext.startActivity(webIntent);
                         }
                     }
                 };
                 countDownTimer.start();

             }
         });
    }

    @Override
    public int getItemCount() {
        return Mtrailer.length;
    }


    class TrailerHolder extends RecyclerView.ViewHolder {

        TrailerHolder(View itemView) {
            super(itemView);
            Tv_Trailer_Adapter = itemView.findViewById(R.id.tv_trailer_adapter);
            Iv_Trailer_Image = itemView.findViewById(R.id.iv_trailer_image);

        }



    }
}
