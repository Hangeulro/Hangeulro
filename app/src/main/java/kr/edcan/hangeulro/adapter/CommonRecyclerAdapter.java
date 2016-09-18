package kr.edcan.hangeulro.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import kr.edcan.hangeulro.R;
import kr.edcan.hangeulro.model.CommonRecycleData;

/**
 * Created by MalangDesktop on 2016-06-04.
 */
public class CommonRecyclerAdapter extends RecyclerView.Adapter<CommonRecyclerAdapter.ViewHolder> {

    Context context;
    ArrayList<CommonRecycleData> arrayList;
    CommonRecycleData data;

    public CommonRecyclerAdapter(Context context, ArrayList<CommonRecycleData> items) {
        this.context = context;
        this.arrayList = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.common_cardview_content, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        data = arrayList.get(position);
        if (data.isHeader()) {
            holder.headerView.setVisibility(View.VISIBLE);
            holder.visibileView.setVisibility(View.GONE);
        } else {
            holder.headerView.setVisibility(View.GONE);
            holder.visibileView.setVisibility(View.VISIBLE);
            holder.title.setText(data.getTitle());
            holder.content.setText(data.getContent());
            holder.subContent.setText(data.getSubContent());
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView visibileView;
        RelativeLayout headerView;
        TextView title, content, subContent;

        public ViewHolder(View itemView) {
            super(itemView);
            visibileView = (CardView) itemView.findViewById(R.id.common_cardview_cardview);
            headerView = (RelativeLayout) itemView.findViewById(R.id.common_cardview_headerLayout);
            title = (TextView) itemView.findViewById(R.id.common_cardview_title);
            content = (TextView) itemView.findViewById(R.id.common_cardview_content);
            subContent = (TextView) itemView.findViewById(R.id.common_cardview_subContent);
        }
    }
}