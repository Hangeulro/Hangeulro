package kr.edcan.neologism.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;

import kr.edcan.neologism.R;
import kr.edcan.neologism.activity.DicDetailViewActivity;
import kr.edcan.neologism.databinding.DicViewRecyclerContentBinding;
import kr.edcan.neologism.model.Board;
import kr.edcan.neologism.model.DicData;
import kr.edcan.neologism.model.MyDic;
import kr.edcan.neologism.utils.DataManager;
import kr.edcan.neologism.utils.NetworkHelper;
import kr.edcan.neologism.utils.NetworkInterface;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by MalangDesktop on 2016-06-04.
 */
public class DicRecyclerAdapter extends RecyclerView.Adapter<DicRecyclerAdapter.ViewHolder> {
    DicViewRecyclerContentBinding binding;
    Context context;
    ArrayList<DicData> arrayList;
    DicData data;
    int cardFooter[] = {R.drawable.bg_diclove_card, R.drawable.bg_dicfun_card, R.drawable.bg_dicsad_card, R.drawable.bg_dicangry_card, R.drawable.bg_dicsym_card, R.drawable.bg_diclife_card};
    int codeType;

    public DicRecyclerAdapter(Context context, ArrayList<DicData> items, int codeType) {
        this.context = context;
        this.arrayList = items;
        this.codeType = codeType;
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
        holder.dicRecyclerHeader.setBackgroundResource(cardFooter[codeType]);
        holder.dicBG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.c.startActivity(new Intent(holder.c, DicDetailViewActivity.class)
                        .putExtra("title", holder.title.getText())
                        .putExtra("meaning", holder.meaning.getText())
                        .putExtra("example", holder.example.getText()));
            }
        });
        holder.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSpinner(holder);
            }

        });
    }

    private void showSpinner(final ViewHolder holder) {

        final NetworkInterface service = NetworkHelper.getNetworkInstance();
        final DataManager manager = new DataManager(context);
        Call<ArrayList<MyDic>> getMyDictionary = service.getMyDictionary(manager.getActiveUser().second.getToken());
        LayoutInflater in = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = in.inflate(R.layout.spinnerdialog, null);

        final Spinner sp = (Spinner) view.findViewById(R.id.spinner);
        getMyDictionary.enqueue(new Callback<ArrayList<MyDic>>() {
            @Override
            public void onResponse(Call<ArrayList<MyDic>> call, Response<ArrayList<MyDic>> response) {
                switch (response.code()) {
                    case 200:
                        final ArrayList<String> result = new ArrayList<String>();
                        for(MyDic d : response.body()){
                            result.add(d.getDicname());
                        }
                        ArrayAdapter adapter = new ArrayAdapter(
                                holder.c,
                                R.layout.textview, // 레이아웃
                                result);
                        sp.setAdapter(adapter);
                        new MaterialDialog.Builder(holder.c)
                                .title("저장할 사전을 선택")
                                .customView(view, true)
                                .positiveText("저장")
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        Call<ResponseBody> addToDictionary = service.addToDictionary(manager.getActiveUser().second.getToken(),
                                                result.get(sp.getSelectedItemPosition()), data.getTitle());
                                        addToDictionary.enqueue(new Callback<ResponseBody>() {
                                            @Override
                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                Log.e("asdf", response.code()+"");
                                                switch (response.code()){
                                                    case 200:
                                                        Toast.makeText(context, "저장이 완료되었습니다!", Toast.LENGTH_SHORT).show();
                                                        break;
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                Log.e("asdf", t.getMessage());
                                            }
                                        });
                                    }
                                })
                                .show();
                        break;
                    default:
                        Log.e("asdf", response.code() + "");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<MyDic>> call, Throwable t) {

            }
        });
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
            save = binding.dicRecyclerSave;
            example = binding.dicRecyclerExample;
            viewCount = binding.dicRecyclerViewCount;
            dicRecyclerHeader = binding.dicRecyclerHeader;
            dicBG = binding.dicRecyclerBG;
        }
    }
}