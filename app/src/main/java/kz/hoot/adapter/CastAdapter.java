package kz.hoot.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import kz.hoot.R;
import kz.hoot.model.Cast;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.ViewHolder>{
    private ArrayList<Cast> casts = new ArrayList<>();
    private Context context;

    public void setCasts(ArrayList<Cast> casts) {
        this.casts = casts;
        notifyDataSetChanged();
    }

    public CastAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cast_card, parent, false);
        return new CastAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.castName.setText(casts.get(position).getCastName());
        holder.castCreatorName.setText(casts.get(position).getCastCreatorName());
        holder.castType.setText(String.valueOf(casts.get(position).getCastType()));
        if (casts.get(position).getPoster().length() > 0) {
            byte[] decodedString = Base64.decode(casts.get(position).getPoster(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            Glide.with(context)
                    .asBitmap()
                    .load(decodedByte)
                    .into(holder.poster);
        }
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation fadeOut = new AlphaAnimation(1, 0);
                fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
                fadeOut.setStartOffset(0);
                fadeOut.setDuration(500);
                holder.castName.setAnimation(fadeOut);
                holder.castType.setAnimation(fadeOut);
                holder.castCreatorName.setAnimation(fadeOut);
                Cast cast = casts.get(position);
//                v.findViewById(holder.)
                holder.castName.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return casts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private FrameLayout parent;
        private TextView castName, castType, castCreatorName;
        private ImageView poster;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.cast_card_parent);
            castName = itemView.findViewById(R.id.cast_name);
            castCreatorName = itemView.findViewById(R.id.cast_director);
            castType = itemView.findViewById(R.id.cast_type);
            poster = itemView.findViewById(R.id.cast_image);
        }
    }
}
