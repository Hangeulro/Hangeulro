package kr.edcan.hangeulro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import kr.edcan.hangeulro.R;
import kr.edcan.hangeulro.model.CommonData;

/**
 * Created by KOHA_DESKTOP on 2016. 6. 19..
 */
public class DictionaryListViewAdapter extends ArrayAdapter<CommonData>{
    ArrayList<CommonData> arrayList;
    Context c;
    public DictionaryListViewAdapter(Context context, ArrayList<CommonData> arrayList) {
        super(context, 0, arrayList);
        this.arrayList = arrayList;
        this.c = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(c).inflate(R.layout.dictionary_listview_content, null);

        CommonData data = getItem(position);
        ImageView icon = (ImageView) view.findViewById(R.id.common_listview_image);
        TextView title = (TextView) view.findViewById(R.id.common_listview_title);
        ImageView arrow = (ImageView) view.findViewById(R.id.common_listview_arrow);
        icon.setImageResource(data.getLogo());
        title.setText(data.getTitle());
        return view;
    }
}
