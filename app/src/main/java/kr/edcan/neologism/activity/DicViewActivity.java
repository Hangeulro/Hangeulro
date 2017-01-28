package kr.edcan.neologism.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.github.nitrico.lastadapter.LastAdapter;
import com.github.nitrico.lastadapter.Type;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kr.edcan.neologism.R;
import kr.edcan.neologism.databinding.ActivityDicViewBinding;
import kr.edcan.neologism.databinding.DictionaryListviewContentBinding;
import kr.edcan.neologism.model.DicDBData;
import kr.edcan.neologism.model.DicData;
import kr.edcan.neologism.utils.NetworkHelper;
import kr.edcan.neologism.utils.NetworkInterface;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class DicViewActivity extends AppCompatActivity {
    Realm realm;
    ArrayList<DicData> arrayList;
    int codeType;
    Toolbar toolbar;
    RecyclerView recyclerView;
    ActivityDicViewBinding binding;
    Intent intent;
    String[] titleTextArr = {
            "#사랑",
            "#우스운",
            "#서글픈",
            "#화가 난",
            "#공감되는",
            "#일상생활"
    };
    String[] tagArr = {
            "사", "우", "서", "화", "공", "일"
    };
    String titleTextEngArr[] = {
            "Love",
            "Funny",
            "Sad",
            "Angry",
            "Sympathy",
            "Lifestyle"
    };
    Call<ResponseBody> getWordByType;
    NetworkInterface service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dic_view);
        setDefault();
        getDataFromDatabase();
    }


    private void setDefault() {
        intent = getIntent();
        realm = Realm.getDefaultInstance();
        codeType = intent.getIntExtra("codeType", -1);
        service = NetworkHelper.getNetworkInstance();
        toolbar = binding.toolbar;
        toolbar.setContentInsetsAbsolute(0, 0);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.dicViewHeadeBG.setImageResource(new int[]{R.drawable.bg_dic_lovetag, R.drawable.bg_dic_funtag, R.drawable.bg_dic_sadtag, R.drawable.bg_dic_angrytag, R.drawable.bg_dic_satisfiedtag, R.drawable.bg_dic_lifetag}[codeType]);
        binding.collapsingToolbar.setTitle(titleTextArr[codeType]);
        binding.collapsingToolbar.setContentScrimColor(getResources().getColor(R.color.colorPrimary));
        binding.collapsingToolbar.setExpandedTitleTextAppearance(R.style.expandedTitleStyle);
        binding.collapsingToolbar.setCollapsedTitleTextAppearance(R.style.collapsedTitleStyle);
        recyclerView = binding.dicViewRecyclerView;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
    }


    private void getDataFromDatabase() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(false);
        realm.beginTransaction();
        RealmResults<DicDBData> results = realm.where(DicDBData.class)
                .contains("cata", tagArr[codeType])
                .findAll();
        arrayList = new ArrayList<>();
        for (DicDBData data : results) {
            arrayList.add(new DicData(data));
        }
        realm.commitTransaction();
        LastAdapter.with(arrayList, 0)
                .map(DicData.class, new Type<>(R.layout.dictionary_listview_content)
                        .onBind(new Function1<Type.Params<? extends ViewDataBinding>, Unit>() {
                            @Override
                            public Unit invoke(Type.Params<? extends ViewDataBinding> params) {
                                ((DictionaryListviewContentBinding) params.getBinding()).dicMenuTitle.setText(arrayList.get(params.getPosition()).getWord());
                                ((DictionaryListviewContentBinding) params.getBinding()).dicMenuContent.setText(arrayList.get(params.getPosition()).getMean());
                                return null;
                            }
                        })
                        .onClick(new Function1<Type.Params<? extends ViewDataBinding>, Unit>() {
                            @Override
                            public Unit invoke(Type.Params<? extends ViewDataBinding> params) {
                                return null;
                            }
                        })
                        .onLongClick(new Function1<Type.Params<? extends ViewDataBinding>, Unit>() {
                            @Override
                            public Unit invoke(Type.Params<? extends ViewDataBinding> params) {
                                return null;
                            }
                        }))
                .into(binding.dicViewRecyclerView);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
