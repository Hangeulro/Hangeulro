package kr.edcan.hangeulro.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import kr.edcan.hangeulro.R;
import kr.edcan.hangeulro.databinding.DicViewRecyclerContentBinding;
import kr.edcan.hangeulro.model.CommonRecycleData;
import kr.edcan.hangeulro.model.DicData;

/**
 * Created by MalangDesktop on 2016-06-04.
 */
public class DicRecyclerAdapter extends RecyclerView.Adapter<DicRecyclerAdapter.ViewHolder> {
    DicViewRecyclerContentBinding binding;
    Context context;
    ArrayList<DicData> arrayList;
    DicData data;

    public DicRecyclerAdapter(Context context, ArrayList<DicData> items) {
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
        holder.title.setText(data.getTitle());
        holder.meaning.setText(data.getMeaning());
        holder.viewCount.setText("조회수 " + data.getSearchCount());
        holder.example.setText(data.getExample());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, meaning, example, viewCount;

        public ViewHolder(View itemView) {
            super(itemView);
            title = binding.dicRecyclerTitle;
            meaning = binding.dicRecyclerMeaning;
            example = binding.dicRecyclerExample;
            viewCount = binding.dicRecyclerViewCount;
        }
    }
}