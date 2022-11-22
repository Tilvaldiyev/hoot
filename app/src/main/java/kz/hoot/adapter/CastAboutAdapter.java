package kz.hoot.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import kz.hoot.R;
import kz.hoot.model.Cast;

public class CastAboutAdapter extends RecyclerView.Adapter<CastAboutAdapter.ViewHolder>{
    private ArrayList<Cast> casts = new ArrayList<>();
    private Context context;

    public void setCasts(ArrayList<Cast> casts) {
        this.casts = casts;
        notifyDataSetChanged();
    }

    public CastAboutAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.about_card, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.castName.setText(casts.get(position).getCastName());
        holder.castCreatorName.setText(casts.get(position).getCastCreatorName());
        holder.castDescription.setText(String.valueOf(casts.get(position).getCastType()));
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

            }
        });
    }

    @Override
    public int getItemCount() {
        return casts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private FrameLayout parent;
        private TextView castName, castDescription, castCreatorName;
        private ImageView poster;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.cast_card_about_parent);
            castName = itemView.findViewById(R.id.cast_about_name);
            castCreatorName = itemView.findViewById(R.id.cast_about_director);
            castDescription = itemView.findViewById(R.id.cast_description);
            poster = itemView.findViewById(R.id.cast_about_image);
        }
    }
}
