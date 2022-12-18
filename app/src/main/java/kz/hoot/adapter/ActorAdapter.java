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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

import kz.hoot.ActorsActivity;
import kz.hoot.R;
import kz.hoot.model.Actor;

public class ActorAdapter extends RecyclerView.Adapter<ActorAdapter.ViewHolder> {
    private ArrayList<Actor> actors = new ArrayList<>();
    private Context context;
    private ActorAdapter.ActorActivityService actorActivityService;
    private ArrayList<Actor> favActors;
    private View view;
    private BottomSheetDialog bottomSheetDialog;
    private RecyclerView botttomCastingsRecView;
    private CastHistoryAdapter castHistoryAdapter;

    public void setActors(ArrayList<Actor> actors) {
        this.actors = actors;
        notifyDataSetChanged();
    }

    public ArrayList<Actor> getActors() {
        return actors;
    }

    public ActorAdapter(Context context, ActorActivityService actorActivityService, CastHistoryAdapter castHistoryAdapter) {
        this.context = context;
        this.actorActivityService = actorActivityService;
        this.castHistoryAdapter = castHistoryAdapter;
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
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_actor, parent, false);
        favActors = actorActivityService.favActors();
        return new ActorAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        for (Actor a : favActors) {
            if (a.getActorId() == actors.get(position).getActorId()) {
                holder.addToFav.setImageResource(R.drawable.ic_baseline_favorite_24);
            }
        }
        holder.name.setText(actors.get(position).getLastname() + " " + actors.get(position).getName());
        holder.age.setText(actors.get(position).getAge() + " лет");
        holder.city.setText(actors.get(position).getCountry() + ", " + actors.get(position).getCity());
//        holder.about.setText(actors.get(position).getAbout());
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
            }
        });

        holder.addToFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Actor a : favActors) {
                    if (a.getActorId() == actors.get(position).getActorId()) {
                        Toast.makeText(context, "Уже в списке избранных", Toast.LENGTH_SHORT).show();
                    } else {
                        int result = actorActivityService.addToFavList(actors.get(position).getActorId());
                        if (result == 200) {
                            holder.addToFav.setImageResource(R.drawable.ic_baseline_favorite_24);
                        }
                    }
                }
            }
        });

        holder.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActorAdapter.ViewHolder.cancelBtn.setVisibility(View.GONE);
                ActorAdapter.ViewHolder.inviteBtn.setVisibility(View.VISIBLE);
            }
        });

        holder.inviteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActorsActivity.invite.setActorsUsername(actors.get(position).getName());
                bottomSheetDialog = new BottomSheetDialog(context);
                View sheetView = LayoutInflater.from(context).inflate(R.layout.bottom_castings, view.findViewById(R.id.bottom_castings__parent));
                botttomCastingsRecView = sheetView.findViewById(R.id.bottom_castings__recview);
                botttomCastingsRecView.setAdapter(castHistoryAdapter);
                botttomCastingsRecView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
                bottomSheetDialog.setContentView(sheetView);
                bottomSheetDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return actors.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private MaterialCardView parent;
        private TextView name, age, city, about;
        private ImageView img;
        public static Button inviteBtn, cancelBtn;
        private ImageButton addToFav;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.list_item_actor__fullname);
            city = itemView.findViewById(R.id.list_item_actor__address_txt);
//            about = itemView.findViewById(R.id.actor_item_about);
            parent = itemView.findViewById(R.id.list_item_actor__parent);
            img = itemView.findViewById(R.id.list_item_actor__img);
            age = itemView.findViewById(R.id.list_item_actor__age_txt);
            inviteBtn = itemView.findViewById(R.id.list_item_actor__invite_btn);
            cancelBtn = itemView.findViewById(R.id.list_item_actor__cancel_btn);
            addToFav = itemView.findViewById(R.id.list_item_actor__fav_btn);
        }
    }

    public interface ActorActivityService{
        int addToFavList(int castId);
        ArrayList<Actor> favActors();
    }
}
