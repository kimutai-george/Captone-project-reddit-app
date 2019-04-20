package com.example.a4reddit.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.a4reddit.R;
import com.example.a4reddit.data.model.Post;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by George Kimutai on 4/02/2019.
 */
public class CommentsViewAdapter extends RecyclerView.Adapter<CommentsViewAdapter.ViewHolder> {
    private List<Post> posts;
    private final Context context;

    public CommentsViewAdapter(Context context) {
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comments_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mAuthor.setText(posts.get(position).getAuthor());
        holder.mData.setText(posts.get(position).getUpdated());
        holder.mText.setText(posts.get(position).getText());


    }

    @Override
    public int getItemCount() {
        if (posts == null) return 0;
        return posts.size();
    }

    public void getPost(List<Post> post) {
        if (post == null) return;
        if (posts != post) {
            posts = post;
            notifyDataSetChanged();
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(true);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.comments_Author)
        TextView mAuthor;
        @BindView(R.id.comments_Text)
        TextView mText;
        @BindView(R.id.comments_data)
        TextView mData;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

