package com.example.a4reddit.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a4reddit.R;
import com.example.a4reddit.data.model.Post;
import com.example.a4reddit.database.SubModel;
import com.example.a4reddit.interfaces.BackPress;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by George Kimutai on 3/29/2019.
 */
public class SearchRecyclerviewAdapter extends RecyclerView.Adapter<SearchRecyclerviewAdapter.ViewHolder> {
    private static final String TAG = "SearchAdapter";
    private final Context ct;
    private List<Post> post;
    private List<SubModel> model;
    private final BackPress mSendWord;


    public SearchRecyclerviewAdapter(Context ct, List<SubModel> model, BackPress backPress) {

        mSendWord = backPress;
        this.ct = ct;
        this.model = model;

    }

    @NonNull
    @Override
    public SearchRecyclerviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(ct).inflate(R.layout.search_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchRecyclerviewAdapter.ViewHolder holder, int position) {
        ArrayList<SubModel> subModels = (ArrayList<SubModel>) model;

        Log.i(TAG, "sizeOfModel: " + subModels.size());
        holder.title.setText(post.get(position).getTitle());
        holder.author.setText(post.get(position).getAuthor());
        String numn = post.get(position).getThumbnailURL();
        if (numn != null) {
            Picasso.get().load(numn).into(holder.imageView);
        }
        int pos = -1;
        if (model.size() != 0 && model != null) {
            for (int z = 0; z < model.size(); z++) {
                Log.i(TAG, "room: " + model.get(z).getNameGropu());
                SubModel subModel = model.get(z);
                String dataContain = post.get(position).getGroup();
                String roomContain = subModel.getNameGropu();

                if (roomContain.equals(dataContain)) {
                    holder.mButton.setTag(subModel);
                    holder.mButton.setSelected(true);
                }

            }
        }


//        holder.itemView.setOnClickListener(onClickListener);

        holder.takeData(post.get(position).getGroup(), pos);

    }


    @Override
    public int getItemCount() {
        if (post == null) return 0;
        return post.size();
    }

    public void takeList(List<Post> post) {
        if (this.post == post) return;

        if (post != null) {
            this.post = post;

        }
        notifyDataSetChanged();

    }

    public void addGropu(List<SubModel> models) {
        if (models == null) return;

        this.model = models;
        notifyDataSetChanged();

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.search_list_imageView)
        ImageView imageView;
        @BindView(R.id.search_list_Date)
        TextView author;
        @BindView(R.id.search_list_Title)
        TextView title;
        @BindView(R.id.imageButtonSearchList)
        Button mButton;
        private String name;
        private View view;
        private int pos;

        private ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mButton.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {

            if (mButton.isSelected()) {
                mSendWord.startNewActivity(name, view, false);
                mButton.setSelected(false);
            } else {
                mSendWord.startNewActivity(name, view, true);
                mButton.setSelected(true);

            }


        }

        private void takeData(String name, int pos) {
            this.pos = pos;
            this.name = name;


        }
    }
}

