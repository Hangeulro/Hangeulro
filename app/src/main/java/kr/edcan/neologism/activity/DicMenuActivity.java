package kr.edcan.neologism.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.claudiodegio.msv.SuggestionMaterialSearchView;
import com.github.nitrico.lastadapter.ItemType;
import com.github.nitrico.lastadapter.LastAdapter;
import com.github.nitrico.lastadapter.LayoutHandler;
import com.github.nitrico.lastadapter.Type;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kr.edcan.neologism.BR;
import kr.edcan.neologism.R;
import kr.edcan.neologism.databinding.ActivityDicMenuBinding;
import kr.edcan.neologism.databinding.DicExpandGridItemBinding;
import kr.edcan.neologism.model.CommonData;
import kr.edcan.neologism.model.DicDBData;

public class DicMenuActivity extends AppCompatActivity {

    RealmResults<DicDBData> realmResults;
    Menu menu = null;
    Toolbar toolbar;
    SuggestionMaterialSearchView searchView;
    ActivityDicMenuBinding binding;
    RecyclerView dicRecycler;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dic_menu);
        setAppbarLayout();
        setDefault();
    }

    private void setAppbarLayout() {
        toolbar = binding.toolbar;
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("신조어 사전 펼쳐보기");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setDefault() {
        dicRecycler = binding.dicGrid;
        dicRecycler.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        final ArrayList<CommonData> arrayList = new ArrayList<>();
        arrayList.add(new CommonData("#사랑", "", R.drawable.bg_dic_lovetag));
        arrayList.add(new CommonData("#우스운", "", R.drawable.bg_dic_funtag));
        arrayList.add(new CommonData("#서글픈", "", R.drawable.bg_dic_sadtag));
        arrayList.add(new CommonData("#화가 나는", "", R.drawable.bg_dic_angrytag));
        arrayList.add(new CommonData("#공감되는", "", R.drawable.bg_dic_satisfiedtag));
        arrayList.add(new CommonData("#일상생활", "", R.drawable.bg_dic_lifetag));
        LastAdapter.with(arrayList, BR.item)
                .map(CommonData.class, new Type<>(R.layout.dic_expand_grid_item)
                        .onBind(new Function1<Type.Params<? extends ViewDataBinding>, Unit>() {
                            @Override
                            public Unit invoke(Type.Params<? extends ViewDataBinding> params) {
                                ((DicExpandGridItemBinding)params.getBinding()).image.setImageResource(arrayList.get(params.getPosition()).getLogo());
                                return null;
                            }
                        })
                        .onClick(new Function1<Type.Params<? extends ViewDataBinding>, Unit>() {
                            @Override
                            public Unit invoke(Type.Params<? extends ViewDataBinding> params) {
                                startActivity(new Intent(getApplicationContext(), DicViewActivity.class)
                                        .putExtra("codeType", params.getPosition()));
                                return null;
                            }
                        }))
                .into(dicRecycler);
        realm = Realm.getDefaultInstance();
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
