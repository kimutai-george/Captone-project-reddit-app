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
import com.example.a4reddit.interfaces.Mine_SubToDetail;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by George Kimutai on 3/28/2019.
 */
public class SubCreditAdapter extends RecyclerView.Adapter<SubCreditAdapter.ViewHolder> {
    private final Context context;
    private List<Post> post;
    private final Mine_SubToDetail mySubToDetail;

    public SubCreditAdapter(Context context, Mine_SubToDetail mySubToDetail) {
        this.mySubToDetail = mySubToDetail;
        this.context = context;
    }


    @NonNull
    @Override
    public SubCreditAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.my_sub_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubCreditAdapter.ViewHolder holder, int position) {

        holder.title.setText(post.get(position).getTitle());
        holder.date.setText(post.get(position).getUpdated());
        holder.subtitle.setText(post.get(position).getText());
        holder.mGroup.setText(post.get(position).getGroup());
        String uri = post.get(position).getThumbnailURL();


        holder.imageView.setVisibility(View.VISIBLE);
        Picasso.get().load(uri).error(R.drawable.icon).into(holder.imageView);


        holder.getPost(post.get(position));


    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void takePost(List<Post> postsFromFrag) {
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
        @BindView(R.id.mySub_group)
        TextView mGroup;
        @BindView(R.id.mySub_list_Date)
        TextView date;
        @BindView(R.id.mySub_list_Subtitle)
        TextView subtitle;
        @BindView(R.id.mySub_list_Title)
        TextView title;
        @BindView(R.id.mySub_list_imageView)
        ImageView imageView;
        private Post post;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mySubToDetail.mMySubToDetail(post);

        }

        void getPost(Post post) {
            this.post = post;
        }
    }
}

