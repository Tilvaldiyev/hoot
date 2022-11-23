package kz.hoot.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import kz.hoot.R;
import kz.hoot.model.Cast;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.ViewHolder>{
    private ArrayList<Cast> casts = new ArrayList<>();
    private final Context context;
    boolean isAboutCardActive = false;
    private final CastActivityService castActivityService;

    @SuppressLint("NotifyDataSetChanged")
    public void setCasts(ArrayList<Cast> casts) {
        this.casts = casts;
        notifyDataSetChanged();
    }

    public CastAdapter(Context context, CastActivityService castActivityService) {
        this.context = context;
        this.castActivityService = castActivityService;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cast_card, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.castName.setText(casts.get(position).getCastName());
        holder.castCreatorName.setText(casts.get(position).getCastCreatorName());
        holder.castType.setText(String.valueOf(casts.get(position).getCastType()));

        holder.castDirector.setText(holder.castDirector.getText() + " " + casts.get(position).getCastCreatorName());
        holder.castAboutName.setText(casts.get(position).getCastName());
        holder.castDescription.setText(casts.get(position).getDescription());

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
                Animation fadeOut = fadeOut();
                Animation fadeIn = fadeIn();

                if (!isAboutCardActive) {
                    holder.castName.setAnimation(fadeOut);
                    holder.castType.setAnimation(fadeOut);
                    holder.castCreatorName.setAnimation(fadeOut);
                    holder.cardAboutFrame.setAnimation(fadeIn);

                    holder.castName.setVisibility(View.INVISIBLE);
                    holder.castCreatorName.setVisibility(View.INVISIBLE);
                    holder.castType.setVisibility(View.INVISIBLE);
                    holder.cardGradient.setVisibility(View.INVISIBLE);

                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) holder.cardAboutFrame.getLayoutParams();

                    lp.setMargins(0, -20, 0 ,0);

                    holder.cardAboutFrame.setLayoutParams(lp);
                    holder.cardAboutFrame.setVisibility(View.VISIBLE);
                    holder.cardAboutFrame.setTranslationZ(1);
                    isAboutCardActive = true;
                }else {
                    holder.castName.setAnimation(fadeIn);
                    holder.castType.setAnimation(fadeIn);
                    holder.castCreatorName.setAnimation(fadeIn);
                    holder.cardAboutFrame.setAnimation(fadeOut);

                    holder.castName.setVisibility(View.VISIBLE);
                    holder.castCreatorName.setVisibility(View.VISIBLE);
                    holder.castType.setVisibility(View.VISIBLE);
                    holder.cardGradient.setVisibility(View.VISIBLE);

                    holder.cardAboutFrame.setVisibility(View.GONE);
                    isAboutCardActive = false;
                }
            }
        });

        holder.respondButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int result = castActivityService.respondToCast(casts.get(position).getCastId());
                if (result == 208){
                    holder.respondButton.setEnabled(false);
                    holder.respondButton.setText("Вы уже отправили заявку!");
                    holder.respondButton.setBackgroundColor(Color.RED);
                    holder.respondCancelButton.setVisibility(View.VISIBLE);
                }else if (result == 200){
                    holder.respondButton.setEnabled(false);
                    holder.respondButton.setBackgroundColor(Color.GREEN);
                    holder.respondButton.setText("Вы успешно отправили заявку!");
                    holder.respondCancelButton.setVisibility(View.VISIBLE);
                }
            }

        });

        holder.respondCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                int result = castActivityService.respondToCast(casts.get(position).getCastId());
                int result = 200;
                if (result == 200){
                    holder.respondButton.setEnabled(true);
                    holder.respondButton.setText("Хочу в кастинг!");
                    holder.respondButton.setBackgroundColor(context.getResources().getColor(R.color.primary));
                    holder.respondCancelButton.setVisibility(View.GONE);
                }
            }

        });

    }

    @Override
    public int getItemCount() {
        return casts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final FrameLayout parent;
        private final TextView castName, castAboutName;
        private final TextView castType, castDescription;
        private final TextView castCreatorName , castDirector;
        private final ImageView poster;
        private final LinearLayout cardAboutFrame;
        private final View cardGradient;
        private final Button respondButton;
        private final Button respondCancelButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.cast_card_parent);
            castName = itemView.findViewById(R.id.cast_name);
            castCreatorName = itemView.findViewById(R.id.cast_director);
            castType = itemView.findViewById(R.id.cast_type);
            poster = itemView.findViewById(R.id.cast_image);
            cardAboutFrame = itemView.findViewById(R.id.cast_about_parent);
            cardGradient = itemView.findViewById(R.id.cast_card_gradient);
            castAboutName = itemView.findViewById(R.id.cast_about_name);
            castDirector = itemView.findViewById(R.id.cast_about_director);
            castDescription = itemView.findViewById(R.id.cast_about_text);
            respondButton = itemView.findViewById(R.id.card_respond_button);
            respondCancelButton = itemView.findViewById(R.id.card_respond_cancel_button);
        }
    }

    private Animation fadeOut(){
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(500);
        return fadeOut;
    }

    private Animation fadeIn(){
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator());
        fadeIn.setDuration(500);
        return fadeIn;
    }

    public interface CastActivityService{
        int respondToCast(Long castId);
    }
}


