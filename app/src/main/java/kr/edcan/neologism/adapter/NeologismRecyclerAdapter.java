package kr.edcan.neologism.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import kr.edcan.neologism.R;
import kr.edcan.neologism.activity.DicDetailViewActivity;
import kr.edcan.neologism.activity.NeologismBoardViewActivity;
import kr.edcan.neologism.databinding.DicViewRecyclerContentBinding;
import kr.edcan.neologism.databinding.NeologismRecyclerContentBinding;
import kr.edcan.neologism.model.Board;
import kr.edcan.neologism.model.DicData;
import kr.edcan.neologism.utils.ImageSingleTon;
import kr.edcan.neologism.views.RoundImageView;

/**
 * Created by MalangDesktop on 2016-06-04.
 */
public class NeologismRecyclerAdapter extends RecyclerView.Adapter<NeologismRecyclerAdapter.ViewHolder> {
    NeologismRecyclerContentBinding binding;
    Context context;
    ArrayList<Board> arrayList;
    Board data;

    public NeologismRecyclerAdapter(Context context, ArrayList<Board> items) {
        this.context = context;
        this.arrayList = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.neologism_recycler_content, parent, false);
        return new ViewHolder(binding.getRoot());
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        data = arrayList.get(position);
        holder.title.setText(data.getTitle());
        holder.date.setText(data.getDate().toLocaleString());
        holder.commentOrInfo.setText("댓글 " + data.getComments().size() + "");
        holder.content.setText(data.getContents());
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("asdf", "like");

            }
        });
        holder.dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("asdf", "dislike");
            }
        });
        if (!data.getImageurl().equals("null")) {
            holder.imageView.setVisibility(View.VISIBLE);
            holder.imageView.setImageUrl(data.getImageurl(), ImageSingleTon.getInstance(context).getImageLoader());
        } else holder.imageView.setVisibility(View.GONE);

        holder.profile.setImageUrl(data.getWriter_profile(), ImageSingleTon.getInstance(context).getImageLoader());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("asdf", data.getBoardid() + " " + holder.getAdapterPosition());
                holder.c.startActivity(new Intent(holder.c, NeologismBoardViewActivity.class)
                        .putExtra("boardid", arrayList.get(holder.getAdapterPosition()).getBoardid()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Context c;
        RoundImageView profile;
        TextView title, date, commentOrInfo, content;
        ImageView like, dislike;
        NetworkImageView imageView;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            c = itemView.getContext();
            cardView = binding.neologismContentCardview;
            profile = binding.neologismContentProfile;
            imageView = binding.neologismContentImage;
            title = binding.neologismContentTitle;
            date = binding.neologismContentDate;
            commentOrInfo = binding.neologismContentCommentInfo;
            content = binding.neologismContentText;
            like = binding.neologismContentLike;
            dislike = binding.neologismContentUnlike;
        }
    }
}