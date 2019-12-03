package com.haberturu.haberturu.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.haberturu.haberturu.Common.ISO8601Parse;
import com.haberturu.haberturu.DetailsNewsActivity;
import com.haberturu.haberturu.Interface.ItemClickListener;
import com.haberturu.haberturu.Model.Article;
import com.haberturu.haberturu.R;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class ListNewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int SPAN_COUNT_ONE = 1;
    public static final int SPAN_COUNT_ONE_ALT = 1;
    public static final int SPAN_COUNT_TWO = 2;

    private static final int VIEW_TYPE_NORMAL = 1;
    private static final int VIEW_TYPE_SIDE_IMAGE = 2;
    private static final int VIEW_TYPE_GRID = 3;

    private List<Article> articleList;
    private GridLayoutManager mGridLayoutManager;
    private Context context;


    public ListNewsAdapter(List<Article> articleList, GridLayoutManager mGridLayoutManager, Context context) {
        this.articleList = articleList;
        this.mGridLayoutManager = mGridLayoutManager;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        int spanCount = mGridLayoutManager.getSpanCount();
        if (spanCount == SPAN_COUNT_ONE) {
            return VIEW_TYPE_NORMAL;
        } else if (spanCount == SPAN_COUNT_TWO){
            return VIEW_TYPE_GRID;
        } else {
            return VIEW_TYPE_SIDE_IMAGE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View itemView;
        if (viewType == VIEW_TYPE_NORMAL) {
            itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_news_card, viewGroup, false);
            return new ViewHolderNormal(itemView);
        } else if (viewType == VIEW_TYPE_SIDE_IMAGE) {
            itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_news_card_side_image, viewGroup, false);
            return new ViewHolderSideImage(itemView);
        } else {
            itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_news_grid_layout, viewGroup, false);
            return new ViewHolderGrid(itemView);
        }



    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {


        switch (holder.getItemViewType()){
            case VIEW_TYPE_NORMAL:
                loadNormalLayout((ViewHolderNormal)holder, i);
                break;
            case VIEW_TYPE_SIDE_IMAGE:
                loadSideImageLayout((ViewHolderSideImage)holder, i);
                break;
            case VIEW_TYPE_GRID:
                loadGridLayout((ViewHolderGrid)holder, i);
                break;
            default:
                break;
        }
    }

    private void loadNormalLayout(ViewHolderNormal holder, int i) {

        Picasso.get()
                .load(String.valueOf(!TextUtils.isEmpty(articleList.get(i).getUrlToImage()) ? articleList.get(i).getUrlToImage() : R.drawable.ic_news_placeholder))
                .centerCrop()
                .fit()
                .placeholder(R.drawable.ic_news_placeholder)
                .into(holder.imageView);


        if (articleList.get(i).getTitle() != null
                && articleList != null
                && articleList.get(i) != null
                && !TextUtils.isEmpty(articleList.get(i).getTitle())
                && articleList.get(i).getTitle().length() > 65) {
            holder.titleTV.setText(articleList.get(i).getTitle().substring(0, 65) + context.getString(R.string.uc_nokta));
        } else {
            holder.titleTV.setText(articleList.get(i).getTitle());
        }

        if (articleList.get(i).getAuthor() != null
                && articleList != null
                && articleList.get(i) != null
                && !TextUtils.isEmpty(articleList.get(i).getAuthor())
                && articleList.get(i).getAuthor().length() > 30) {
            holder.sourceTV.setText(articleList.get(i).getAuthor().substring(0, 30) + context.getString(R.string.uc_nokta));
        } else {
            holder.sourceTV.setText(articleList.get(i).getAuthor());
        }

        Date date = null;
        try {
            date = ISO8601Parse.parse(articleList.get(i).getPublishedAt());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.timeTV.setReferenceTime(date.getTime());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                Intent detail = new Intent(context, DetailsNewsActivity.class);
                detail.putExtra("webUrl", articleList.get(position).getUrl());
                detail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(detail);
            }
        });

    }

    private void loadSideImageLayout(ViewHolderSideImage holder, int i) {

        Picasso.get()
                .load(String.valueOf(!TextUtils.isEmpty(articleList.get(i).getUrlToImage()) ? articleList.get(i).getUrlToImage() : R.drawable.ic_news_placeholder))
                .centerCrop()
                .fit()
                .placeholder(R.drawable.ic_news_placeholder)
                .into(holder.imageView);


        if (articleList.get(i).getTitle() != null
                && articleList != null
                && articleList.get(i) != null
                && !TextUtils.isEmpty(articleList.get(i).getTitle())
                && articleList.get(i).getTitle().length() > 65) {
            holder.titleTV.setText(articleList.get(i).getTitle().substring(0, 65) + context.getString(R.string.uc_nokta));
        } else {
            holder.titleTV.setText(articleList.get(i).getTitle());
        }

        if (articleList.get(i).getAuthor() != null
                && articleList != null
                && articleList.get(i) != null
                && !TextUtils.isEmpty(articleList.get(i).getAuthor())
                && articleList.get(i).getAuthor().length() > 30) {
            holder.sourceTV.setText(articleList.get(i).getAuthor().substring(0, 30) + context.getString(R.string.uc_nokta));
        } else {
            holder.sourceTV.setText(articleList.get(i).getAuthor());
        }

        Date date = null;
        try {
            date = ISO8601Parse.parse(articleList.get(i).getPublishedAt());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.timeTV.setReferenceTime(date.getTime());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                Intent detail = new Intent(context, DetailsNewsActivity.class);
                detail.putExtra("webUrl", articleList.get(position).getUrl());
                detail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(detail);
            }
        });

    }

    private void loadGridLayout(ViewHolderGrid holder, int i) {

        Picasso.get()
                .load(String.valueOf(!TextUtils.isEmpty(articleList.get(i).getUrlToImage()) ? articleList.get(i).getUrlToImage() : R.drawable.ic_news_placeholder))
                .centerCrop()
                .fit()
                .placeholder(R.drawable.ic_news_placeholder)
                .into(holder.imageView);


        if (articleList.get(i).getTitle() != null
                && articleList != null
                && articleList.get(i) != null
                && !TextUtils.isEmpty(articleList.get(i).getTitle())
                && articleList.get(i).getTitle().length() > 40) {
            holder.titleTV.setText(articleList.get(i).getTitle().substring(0, 40) + context.getString(R.string.uc_nokta));
        } else {
            holder.titleTV.setText(articleList.get(i).getTitle());
        }

        if (articleList.get(i).getAuthor() != null
                && articleList != null
                && articleList.get(i) != null
                && !TextUtils.isEmpty(articleList.get(i).getAuthor())
                && articleList.get(i).getAuthor().length() > 10) {
            holder.sourceTV.setText(articleList.get(i).getAuthor().substring(0, 10) + context.getString(R.string.uc_nokta));
        } else {
            holder.sourceTV.setText(articleList.get(i).getAuthor());
        }

        Date date = null;
        try {
            date = ISO8601Parse.parse(articleList.get(i).getPublishedAt());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.timeTV.setReferenceTime(date.getTime());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                Intent detail = new Intent(context, DetailsNewsActivity.class);
                detail.putExtra("webUrl", articleList.get(position).getUrl());
                detail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(detail);
            }
        });

    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }


    static class ViewHolderNormal extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView titleTV, sourceTV;
        public RelativeTimeTextView timeTV;
        public ImageView imageView;

        ItemClickListener itemClickListener;

        public ViewHolderNormal(@NonNull View itemView) {
            super(itemView);

            titleTV = itemView.findViewById(R.id.list_news_title);
            timeTV = itemView.findViewById(R.id.timestamp);
            sourceTV = itemView.findViewById(R.id.list_news_source);
            imageView = itemView.findViewById(R.id.list_news_image);

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

    static class ViewHolderSideImage extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView titleTV, sourceTV;
        public RelativeTimeTextView timeTV;
        public ImageView imageView;

        ItemClickListener itemClickListener;

        public ViewHolderSideImage(@NonNull View itemView) {
            super(itemView);

            titleTV = itemView.findViewById(R.id.side_image_title);
            timeTV = itemView.findViewById(R.id.side_image_timestamp);
            sourceTV = itemView.findViewById(R.id.side_image_source);
            imageView = itemView.findViewById(R.id.side_image_imageView);

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

    static class ViewHolderGrid extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView titleTV, sourceTV;
        public RelativeTimeTextView timeTV;
        public ImageView imageView;

        ItemClickListener itemClickListener;

        public ViewHolderGrid(@NonNull View itemView) {
            super(itemView);

            titleTV = itemView.findViewById(R.id.grid_textView_title);
            timeTV = itemView.findViewById(R.id.grid_textView_time);
            sourceTV = itemView.findViewById(R.id.grid_textView_source);
            imageView = itemView.findViewById(R.id.grid_imageView_image);

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


}
