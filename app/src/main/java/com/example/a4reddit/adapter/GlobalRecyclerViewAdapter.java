package com.example.a4reddit.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a4reddit.R;
import com.example.a4reddit.data.model.Post;
import com.example.a4reddit.interfaces.GlobalAdapToFrag;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by George Kimutai on 3/29/2019.
 */
public class GlobalRecyclerViewAdapter extends RecyclerView.Adapter<GlobalRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "ViewAdapter";
    private final Context context;
    private List<Post> posts;
    private final GlobalAdapToFrag globalAdapToFrag;


    public GlobalRecyclerViewAdapter(Context context, GlobalAdapToFrag globalAdapToFrag) {
        this.globalAdapToFrag = globalAdapToFrag;
        this.context = context;

    }

    @NonNull
    @Override
    public GlobalRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.global_list_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final GlobalRecyclerViewAdapter.ViewHolder holder, int position) {

        holder.title.setText(posts.get(position).getTitle());
        holder.date.setText(posts.get(position).getUpdated());
        holder.subtitle.setText(posts.get(position).getAuthor());
        holder.Group.setText(posts.get(position).getGroup());

        String uri = posts.get(position).getThumbnailURL();


        if (uri != null) {
            holder.imageView.setVisibility(View.VISIBLE);
            Picasso.get().load(uri).into(holder.imageView);

        }
        holder.getData(posts.get(position));


    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(true);
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
    public int getItemCount() {
        if (posts == null) return 0;
        return posts.size();
    }

    public void takePost(List<Post> post) {
        if (post == this.posts) return;
        if (post != null) {
            this.posts = post;
        }
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.global_list_group)
        TextView Group;
        @BindView(R.id.global_list_Date)
        TextView date;
        @BindView(R.id.global_list_Subtitle)
        TextView subtitle;
        @BindView(R.id.global_list_Title)
        TextView title;
        @BindView(R.id.global_list_imageView)
        ImageView imageView;
        private Post posts;


        private ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            globalAdapToFrag.toFragment(posts);

        }

        void getData(Post posts) {
            this.posts = posts;
        }
    }
}

