package kz.hoot.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import kz.hoot.R;
import kz.hoot.model.Actor;

public class ActorAdapter extends RecyclerView.Adapter<ActorAdapter.ViewHolder> {
    private ArrayList<Actor> actors = new ArrayList<>();
    private Context context;

    public void setActors(ArrayList<Actor> actors) {
        this.actors = actors;
        notifyDataSetChanged();
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
        holder.fullname.setText(actors.get(position).getLastname() + " " + actors.get(position).getName() + " " + actors.get(position).getPatronymic());
        holder.age.setText(String.valueOf(actors.get(position).getAge()));
        holder.location.setText(actors.get(position).getCountry() + ", " + actors.get(position).getCity());
        if (actors.get(position).getPhoto().length() > 0) {
            byte[] decodedString = Base64.decode(actors.get(position).getPhoto(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            Glide.with(context)
                    .asBitmap()
                    .load(decodedByte)
                    .into(holder.img);
        }
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                return;
            }
        });
    }

    @Override
    public int getItemCount() {
        return actors.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout parent;
        private TextView fullname, location, age;
        private ImageView img;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.actor_item__parent);
            img = itemView.findViewById(R.id.actor_item__img);
            fullname = itemView.findViewById(R.id.actor_item__fullname);
            location = itemView.findViewById(R.id.actor_item__location);
            age = itemView.findViewById(R.id.actor_item__age);
        }
    }

}
