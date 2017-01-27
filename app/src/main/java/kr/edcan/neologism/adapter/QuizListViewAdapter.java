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
import kr.edcan.neologism.model.CommonData;
import kr.edcan.neologism.model.QuizData;

/**
 * Created by KOHA_DESKTOP on 2016. 6. 19..
 */
public class QuizListViewAdapter extends ArrayAdapter<QuizData> {
    ArrayList<QuizData> arrayList;
    Context c;

    public QuizListViewAdapter(Context context, ArrayList<QuizData> arrayList) {
        super(context, 0, arrayList);
        this.arrayList = arrayList;
        this.c = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(c).inflate(R.layout.quiz_listview_content, null);

        QuizData data = getItem(position);
        ImageView icon = (ImageView) view.findViewById(R.id.common_listview_image);
        TextView title = (TextView) view.findViewById(R.id.common_listview_title);
        TextView subtitle = (TextView) view.findViewById(R.id.common_listview_subtitle);
        TextView text = (TextView) view.findViewById(R.id.common_listview_text);
        icon.setImageResource(data.getLogo());
        title.setText(data.getTitle());
        subtitle.setText(data.getSubtitle());
        text.setText(data.getData());
        return view;
    }
}
