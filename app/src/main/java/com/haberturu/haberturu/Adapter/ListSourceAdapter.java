package com.haberturu.haberturu.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haberturu.haberturu.Common.Common;
import com.haberturu.haberturu.Interface.IconBetterIdeaService;
import com.haberturu.haberturu.Interface.ItemClickListener;
import com.haberturu.haberturu.ListNews;
import com.haberturu.haberturu.Model.IconBetterIdea;
import com.haberturu.haberturu.Model.KaynakWebSite;
import com.haberturu.haberturu.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class ListSourceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    ItemClickListener itemClickListener;

    TextView source_title;
    CircleImageView source_image;

    public ListSourceViewHolder(@NonNull View itemView) {
        super(itemView);

        source_image = itemView.findViewById(R.id.sourceImage);
        source_title = itemView.findViewById(R.id.source_name);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }
}

public class ListSourceAdapter extends RecyclerView.Adapter<ListSourceViewHolder> {

    private Context context;
    private KaynakWebSite kaynakWebSite;

    private IconBetterIdeaService mService;

    public ListSourceAdapter(Context context, KaynakWebSite kaynakWebSite) {
        this.context = context;
        this.kaynakWebSite = kaynakWebSite;

        mService = Common.getIconService();
    }

    @NonNull
    @Override
    public ListSourceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.source_layout, viewGroup, false);
        return new ListSourceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListSourceViewHolder listSourceViewHolder, final int i) {

        StringBuilder iconBetterAPI = new StringBuilder("https://i.olsh.me/allicons.json?url=");
        iconBetterAPI.append(kaynakWebSite.getSources().get(i).getUrl());

        mService.getIconUrl(iconBetterAPI.toString())
                .enqueue(new Callback<IconBetterIdea>() {
                    @Override
                    public void onResponse(Call<IconBetterIdea> call, Response<IconBetterIdea> response) {
                        if (response.body() != null && response.body().getIcons() != null
                                && response.body().getIcons().size() > 0
                                && !TextUtils.isEmpty(response.body().getIcons().get(0).getUrl())) {
                            Picasso.get()
                                    .load(response.body().getIcons().get(0).getUrl())
                                    .placeholder(R.drawable.ic_placeholder)
                                    .error(R.drawable.ic_placeholder)
                                    .into(listSourceViewHolder.source_image);
                        }
                    }

                    @Override
                    public void onFailure(Call<IconBetterIdea> call, Throwable t) {

                    }
                });

        listSourceViewHolder.source_title.setText(kaynakWebSite.getSources().get(i).getName());

        listSourceViewHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                Intent intent = new Intent(context, ListNews.class);
                intent.putExtra("source", kaynakWebSite.getSources().get(position).getId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return kaynakWebSite.getSources().size();
    }
}
