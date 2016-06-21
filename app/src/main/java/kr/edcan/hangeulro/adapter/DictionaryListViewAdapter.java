package kr.edcan.hangeulro.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
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
public class DictionaryListViewAdapter extends ArrayAdapter<CommonData> {
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
        ImageView background = (ImageView) view.findViewById(R.id.dictionarylist_listview_image);
        Drawable drawable = c.getResources().getDrawable(R.drawable.btn_bg);
        drawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        background.setImageDrawable(drawable);
        ImageView icon = (ImageView) view.findViewById(R.id.dictionarylist_listview_logo);
        TextView title = (TextView) view.findViewById(R.id.dictionarylist_listview_title);
        icon.setImageResource(data.getLogo());
        title.setText(data.getTitle());
        return view;
    }
}
