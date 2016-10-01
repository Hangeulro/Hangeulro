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
import kr.edcan.neologism.model.MyDic;

/**
 * Created by KOHA_DESKTOP on 2016. 6. 19..
 */
public class MyDicListViewAdapter extends ArrayAdapter<MyDic> {
    ArrayList<MyDic> arrayList;
    Context c;

    public MyDicListViewAdapter(Context context, ArrayList<MyDic> arrayList) {
        super(context, 0, arrayList);
        this.arrayList = arrayList;
        this.c = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(c).inflate(R.layout.dictionary_listview_content, null);

        MyDic data = getItem(position);
        ImageView icon = (ImageView) view.findViewById(R.id.common_listview_image);
        TextView title = (TextView) view.findViewById(R.id.common_listview_title);
        TextView subtitle = (TextView) view.findViewById(R.id.common_listview_subtitle);
        icon.setImageResource(R.drawable.ic_mydic_gold);
        title.setText(data.getDicname());
        subtitle.setText(data.getSub());
        return view;
    }
}
