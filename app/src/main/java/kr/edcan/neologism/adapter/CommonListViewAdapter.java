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

/**
 * Created by KOHA_DESKTOP on 2016. 6. 19..
 */
public class CommonListViewAdapter extends ArrayAdapter<CommonData> {
    ArrayList<CommonData> arrayList;
    Context c;

    public CommonListViewAdapter(Context context, ArrayList<CommonData> arrayList) {
        super(context, 0, arrayList);
        this.arrayList = arrayList;
        this.c = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(c).inflate(R.layout.common_listview_content, null);

        CommonData data = getItem(position);
        ImageView icon = (ImageView) view.findViewById(R.id.common_listview_image);
        TextView title = (TextView) view.findViewById(R.id.common_listview_title);
        TextView subtitle = (TextView) view.findViewById(R.id.common_listview_subtitle);
        ImageView arrow = (ImageView) view.findViewById(R.id.common_listview_arrow);
        icon.setImageResource(data.getLogo());
        title.setText(data.getTitle());
        subtitle.setText(data.getSubtitle());
        return view;
    }
}
