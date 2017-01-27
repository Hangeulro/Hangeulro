package kr.edcan.neologism.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import kr.edcan.neologism.R;
import kr.edcan.neologism.activity.DicDetailViewActivity;
import kr.edcan.neologism.databinding.DicViewRecyclerContentBinding;
import kr.edcan.neologism.model.DicData;
import kr.edcan.neologism.model.MyDic;
import kr.edcan.neologism.model.Word;

/**
 * Created by MalangDesktop on 2016-06-04.
 */
public class MyDicRecyclerAdapter extends RecyclerView.Adapter<MyDicRecyclerAdapter.ViewHolder> {
    DicViewRecyclerContentBinding binding;
    Context context;
    ArrayList<Word> arrayList;
    Word data;
    int cardFooter[] = {R.drawable.bg_diclove_card, R.drawable.bg_dicfun_card, R.drawable.bg_dicsad_card, R.drawable.bg_dicangry_card, R.drawable.bg_dicsym_card, R.drawable.bg_diclife_card};

    public MyDicRecyclerAdapter(Context context, ArrayList<Word> items) {
        this.context = context;
        this.arrayList = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dic_view_recycler_content, parent, false);
        return new ViewHolder(binding.getRoot());
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        data = arrayList.get(position);
        holder.title.setText(data.getWord());
        holder.meaning.setText(data.getMean());
        holder.viewCount.setText("조회수 " + data.getSee());
        holder.example.setText(data.getEx());
        holder.dicRecyclerHeader.setBackgroundResource(cardFooter[0]);
        holder.dicBG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.c.startActivity(new Intent(v.getContext(), DicDetailViewActivity.class)
                        .putExtra("title", holder.title.getText())
                        .putExtra("meaning", holder.meaning.getText())
                        .putExtra("example", holder.example.getText()));
            }
        });
        holder.save.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Context c;
        TextView title, meaning, example, viewCount, save;
        LinearLayout dicBG;
        RelativeLayout dicRecyclerHeader;

        public ViewHolder(View itemView) {
            super(itemView);
            c = itemView.getContext();
            title = binding.dicRecyclerTitle;
            meaning = binding.dicRecyclerMeaning;
            example = binding.dicRecyclerExample;
            viewCount = binding.dicRecyclerViewCount;
            dicRecyclerHeader = binding.dicRecyclerHeader;
            dicBG = binding.dicRecyclerBG;
            save = binding.dicRecyclerSave;
        }
    }
}