package kr.edcan.neologism.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import kr.edcan.neologism.R;
import kr.edcan.neologism.model.Board;
import kr.edcan.neologism.model.CommonData;
import kr.edcan.neologism.utils.ImageSingleTon;
import kr.edcan.neologism.views.RoundImageView;

/**
 * Created by KOHA_DESKTOP on 2016. 6. 19..
 */
public class NeoBoardCommentListViewAdapter extends ArrayAdapter<Board.Comments> {
    ArrayList<Board.Comments> arrayList;
    Context c;

    public NeoBoardCommentListViewAdapter(Context context, ArrayList<Board.Comments> arrayList) {
        super(context, 0, arrayList);
        this.arrayList = arrayList;
        this.c = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(c).inflate(R.layout.neoboard_comment_listview_content, null);

        Board.Comments data = getItem(position);
        RoundImageView icon = (RoundImageView) view.findViewById(R.id.neoboard_coment_listview_image);
        TextView title = (TextView) view.findViewById(R.id.neoboard_coment_listview_title);
        TextView subtitle = (TextView) view.findViewById(R.id.neoboard_coment_listview_subtitle);
        icon.setImageUrl(data.getProfile_image(), ImageSingleTon.getInstance(getContext()).getImageLoader());
        title.setText(data.getWriter());
        subtitle.setText(data.getSummary());
        return view;
    }
}
