package com.gautam.newsapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.gautam.newsapp.R;
import com.gautam.newsapp.data.model.Article;

import java.util.ArrayList;
import java.util.List;

public class ArticleListAdapter extends RecyclerView.Adapter<ArticleListAdapter.ArticleViewHolder> {

    private List<Article> mList;

    private ArticleListAdapter.OnItemInteractionListener mListener;
    private Context mContext;

    public ArticleListAdapter() {
        mList = new ArrayList<>();
    }

    public ArticleListAdapter(Context context, List<Article> list, ArticleListAdapter.OnItemInteractionListener listener) {
        mContext = context;
        this.mList = list;
        mListener = listener;
    }


    public List<Article> getItemList() {
        return mList;
    }

    public void setBookmark(int position, Boolean bookmarked) {
        mList.get(position).setBookmarked(bookmarked);
        notifyDataSetChanged();
    }

    public class ArticleViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvSubtitle;
        TextView tvUrl;
        ImageView ivBookmark;
        ImageView ivShare;

        public ArticleViewHolder(View view) {
            super(view);
            tvTitle = view.findViewById(R.id.text_title);
            tvSubtitle = view.findViewById(R.id.text_subtitle);
            tvUrl = view.findViewById(R.id.text_url);
            ivBookmark = view.findViewById(R.id.image_bookmark);
            ivShare = view.findViewById(R.id.image_share);
        }
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_article_row, parent, false);
        return new ArticleViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(ArticleViewHolder holder, final int position) {

        holder.tvTitle.setText(mList.get(position).getTitle().trim());
        holder.tvSubtitle.setText(mList.get(position).getDescription().trim());
        if(mList.get(position).isBookmarked()!=null) {
            holder.ivBookmark.setImageResource(mList.get(position).isBookmarked() ?
                    R.drawable.ic_bookmark_24dp : R.drawable.ic_bookmark_border_24dp);
        }else {
            holder.ivBookmark.setImageResource(R.drawable.ic_bookmark_border_24dp);
        }

        holder.ivBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener!=null){
                    mListener.onBookmarkClicked(mList.get(position).getTitle(),position);
                }
            }
        });

//        holder.relativeUserProfile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mListener.onEditClicked(position);
//            }
//        });
//        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mListener.onDeleteClicked(position);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public void add(List<Article> p) {
        int previousDataSize = this.mList.size();
        this.mList.addAll(p);
        notifyItemRangeInserted(previousDataSize, p.size());
    }

    public void setItems(List<Article> p) {
        mList = p;
        notifyDataSetChanged();
    }

    public interface OnItemInteractionListener {
        void onBookmarkClicked(String title, int position);
//
//        void onDeleteClicked(int position);
    }
}
