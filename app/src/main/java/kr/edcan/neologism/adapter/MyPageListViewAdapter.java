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
 * Created by MalangDesktop on 2016-05-08.
 */
public class MyPageListViewAdapter extends ArrayAdapter<CommonData> {

    private LayoutInflater inflater;

    public MyPageListViewAdapter(Context c, ArrayList<CommonData> normalPreferenceListDatas){
        super(c,0,normalPreferenceListDatas);
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.mypage_listview_content, null);
        CommonData data = this.getItem(position);
        if(data != null){

            ImageView imageView = (ImageView) view.findViewById(R.id.mypage_listview_imageview);
            TextView title = (TextView) view.findViewById(R.id.mypage_listview_title);
            TextView content = (TextView) view.findViewById(R.id.mypage_listview_content);
            imageView.setImageResource(data.getLogo());
            title.setText(data.getTitle());
            content.setText(data.getSubtitle());
        }
        return view;
    }
}