package kz.hoot.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import kz.hoot.ActorsActivity;
import kz.hoot.R;
import kz.hoot.model.Cast;

public class CastHistoryAdapter extends RecyclerView.Adapter<CastHistoryAdapter.ViewHolder> {
    private ArrayList<Cast> casts = new ArrayList<>();
    private final Context context;
    boolean isAboutCardActive = false;
    private CastHistoryAdapter.CastHistoryActivityService castHistoryActivityService;
    private boolean isDir = false;

    public CastHistoryAdapter(Context context, CastHistoryAdapter.CastHistoryActivityService castActivityService) {
        this.context = context;
        this.castHistoryActivityService = castActivityService;
    }

    public CastHistoryAdapter(Context context, CastHistoryAdapter.CastHistoryActivityService castHistoryActivityService, boolean isDir) {
        this.context = context;
        this.isDir = isDir;
        this.castHistoryActivityService = castHistoryActivityService;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setCasts(ArrayList<Cast> casts) {
        this.casts = casts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CastHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cast_history_card, parent, false);
        return new CastHistoryAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CastHistoryAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.castName.setText(casts.get(position).getCastName());
        holder.castCreatorName.setText(casts.get(position).getCastCreatorName());
        holder.castType.setText(String.valueOf(casts.get(position).getCastType()));

        holder.castDirector.setText(holder.castDirector.getText() + " " + casts.get(position).getCastCreatorName());

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

        holder.respondButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        holder.chooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int result = castHistoryActivityService.inviteToCast(casts.get(position).getCastId());
                if (result == 202) {
                    ActorAdapter.ViewHolder.inviteBtn.setVisibility(View.GONE);
                    ActorAdapter.ViewHolder.cancelBtn.setVisibility(View.VISIBLE);
                }
            }
        });

        if (isDir) {
            holder.castResult.setVisibility(View.GONE);
            holder.castType.setVisibility(View.GONE);
            holder.chooseBtn.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return casts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final FrameLayout parent;
        private final TextView castName;
        private final TextView castType, castDescription;
        private final TextView castCreatorName , castDirector;
        private final ImageView poster;
        private final LinearLayout cardAboutFrame;
        private final View cardGradient;
        private final Button respondButton, chooseBtn;
        private final Button respondCancelButton;
        private final CardView castResult;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.cast_history_card__parent);
            castName = itemView.findViewById(R.id.cast_history_card__name);
            castCreatorName = itemView.findViewById(R.id.cast_history_card__director);
            castType = itemView.findViewById(R.id.cast_history_card__type);
            poster = itemView.findViewById(R.id.cast_history_card__poster);
            cardAboutFrame = itemView.findViewById(R.id.cast_about_parent);
            cardGradient = itemView.findViewById(R.id.cast_card_gradient);
            castDirector = itemView.findViewById(R.id.cast_about_director);
            castDescription = itemView.findViewById(R.id.cast_about_text);
            respondButton = itemView.findViewById(R.id.card_respond_button);
            respondCancelButton = itemView.findViewById(R.id.card_respond_cancel_button);
            castResult = itemView.findViewById(R.id.cast_history_card__result);
            chooseBtn = itemView.findViewById(R.id.cast_history_card__choosebtn);
        }
    }

    public interface CastHistoryActivityService{
        int inviteToCast(Long castId);
    }
}
