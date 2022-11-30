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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

import kz.hoot.R;
import kz.hoot.model.Actor;

public class ActorAdapter extends RecyclerView.Adapter<ActorAdapter.ViewHolder> {
    private ArrayList<Actor> actors = new ArrayList<>();
    private Context context;
    private boolean isAboutCardActive = false;
    private ActorAdapter.ActorActivityService actorActivityService;

    public void setActors(ArrayList<Actor> actors) {
        this.actors = actors;
        notifyDataSetChanged();
    }

    public ArrayList<Actor> getActors() {
        return actors;
    }

    public ActorAdapter(Context context, ActorActivityService actorActivityService) {
        this.context = context;
        this.actorActivityService = actorActivityService;
    }

    public ActorAdapter(Context context) {
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.actor_item, parent, false);
        return new ActorAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.headerName.setText(actors.get(position).getLastname() + " " + actors.get(position).getName());
        holder.name.setText(actors.get(position).getLastname() + " " + actors.get(position).getName());
        holder.headerAge.setText(String.valueOf(actors.get(position).getAge()) + " лет");
        holder.age.setText("Возраст: " + String.valueOf(actors.get(position).getAge()) + " лет");
        holder.city.setText("Город: " + actors.get(position).getCountry() + ", " + actors.get(position).getCity());
        holder.about.setText(actors.get(position).getAbout());
        if(actors.get(position).getPhoto()!=null) {
            if (actors.get(position).getPhoto().length() > 0) {
                byte[] decodedString = Base64.decode(actors.get(position).getPhoto(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                Glide.with(context)
                        .asBitmap()
                        .load(decodedByte)
                        .into(holder.img);
            }
        }
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation fadeOut = fadeOut();
                Animation fadeIn = fadeIn();

                if (!isAboutCardActive) {
                    holder.headerAge.setAnimation(fadeOut);
                    holder.headerName.setAnimation(fadeOut);
//                    holder.cardAboutFrame.setAnimation(fadeOut);

                    holder.headerAge.setVisibility(View.INVISIBLE);
                    holder.headerName.setVisibility(View.INVISIBLE);
                    holder.gradient.setVisibility(View.INVISIBLE);

                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) holder.cardAboutFrame.getLayoutParams();

//                    lp.setMargins(0, -20, 0 ,0);

                    holder.cardAboutFrame.setLayoutParams(lp);
                    holder.cardAboutFrame.setVisibility(View.VISIBLE);
                    holder.cardAboutFrame.setTranslationZ(1);
                    isAboutCardActive = true;
                }else {
                    holder.headerAge.setAnimation(fadeIn);
                    holder.headerName.setAnimation(fadeIn);
                    holder.cardAboutFrame.setAnimation(fadeIn);

                    holder.headerAge.setVisibility(View.VISIBLE);
                    holder.headerName.setVisibility(View.VISIBLE);
                    holder.gradient.setVisibility(View.VISIBLE);

                    holder.cardAboutFrame.setVisibility(View.GONE);
                    isAboutCardActive = false;
                }
            }
        });

        holder.addToFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int result = actorActivityService.addToFavList(actors.get(position).getActorId());
                if (result == 200){
                    holder.addToFav.setEnabled(false);
                    holder.addToFav.setBackgroundColor(Color.GREEN);
                    holder.addToFav.setText("Добавлен");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return actors.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private MaterialCardView parent;
        private TextView headerName, headerAge, name, age, city, about;
        private ImageView img;
        private Button inviteBtn, cancelBtn, addToFav;
        private LinearLayout cardAboutFrame;
        private View gradient;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            headerName = itemView.findViewById(R.id.actor_item_header__name);
            headerAge = itemView.findViewById(R.id.actor_item_header__age);
            name = itemView.findViewById(R.id.actor_item_name);
            city = itemView.findViewById(R.id.actor_item_city);
            about = itemView.findViewById(R.id.actor_item_about);
            parent = itemView.findViewById(R.id.actor_item__parent);
            img = itemView.findViewById(R.id.actor_item_img);
            age = itemView.findViewById(R.id.actor_item_age);
            inviteBtn = itemView.findViewById(R.id.actor_item_invite_btn);
            cancelBtn = itemView.findViewById(R.id.actor_item_cancel_btn);
            addToFav = itemView.findViewById(R.id.actor_item_add_to_fav_btn);
            cardAboutFrame = itemView.findViewById(R.id.actor_item__about_parent);
            gradient = itemView.findViewById(R.id.actor_item_gradient);
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

    public interface ActorActivityService{
        int addToFavList(int castId);
    }
}
