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
import com.example.a4reddit.database.BookedPost;
import com.example.a4reddit.interfaces.SendTOMainActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by George Kimutai on 4/14/2019.
 */
public class FavoriteViewAdapter extends RecyclerView.Adapter<FavoriteViewAdapter.ViewHolder> {
    private final Context context;
    private List<BookedPost> post;
    private final SendTOMainActivity sendTOMainActivity;

    public FavoriteViewAdapter(Context context, SendTOMainActivity sendTOMainActivity) {
        this.sendTOMainActivity = sendTOMainActivity;
        this.context = context;
    }


    @NonNull
    @Override
    public FavoriteViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.favourite_list_item, parent, false);

        return new FavoriteViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewAdapter.ViewHolder holder, int position) {

        holder.title.setText(post.get(position).getTitle());
        holder.date.setText(post.get(position).getUpdated());
        holder.subtitle.setText(post.get(position).getText());
        holder.mGroup.setText(post.get(position).getGroup());
        String uri = post.get(position).getThumbnailURL();


        holder.imageView.setVisibility(View.VISIBLE);
        Picasso.get()
                .load(uri)
                .error(R.drawable.icon)
                .into(holder.imageView);


        holder.getPost(post.get(position));


    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void takePost(List<BookedPost> postsFromFrag) {
        if (postsFromFrag == post) return;
        if (postsFromFrag != null) {
            post = postsFromFrag;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        if (post == null) return 0;
        return post.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.mySubGroup_fav)
        TextView mGroup;
        @BindView(R.id.mySub_list_Date_Favoriete)
        TextView date;
        @BindView(R.id.mySub_list_Subtitle_Favoriete)
        TextView subtitle;
        @BindView(R.id.mySub_list_Title_Favoriete)
        TextView title;
        @BindView(R.id.mySub_list_imageView_Favoriete)
        ImageView imageView;
        private BookedPost post;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            sendTOMainActivity.mSendTOMainActivity(post);

        }

        void getPost(BookedPost post) {
            this.post = post;
        }
    }
}

