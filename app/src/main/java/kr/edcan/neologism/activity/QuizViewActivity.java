package kr.edcan.neologism.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import kr.edcan.neologism.R;
import kr.edcan.neologism.databinding.LayoutQuizBinding;
import kr.edcan.neologism.model.Quiz;
import kr.edcan.neologism.utils.NetworkHelper;
import kr.edcan.neologism.utils.NetworkInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizViewActivity extends AppCompatActivity {

    ArrayList<Quiz> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_view);
        arrayList = new ArrayList<>();
        final ViewPager pager = (ViewPager) findViewById(R.id.quizViewViewPager);
        pager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        NetworkInterface service = NetworkHelper.getNetworkInstance();
        Call<List<Quiz>> getQuizList = service.getQuizList();
        getQuizList.enqueue(new Callback<List<Quiz>>() {
            @Override
            public void onResponse(Call<List<Quiz>> call, Response<List<Quiz>> response) {
                switch (response.code()) {
                    case 200:
                        for (Quiz q : response.body()) {
                            arrayList.add(q);
                        }
                        pager.setAdapter(new PagerAdapterClass(getApplicationContext()));
                        break;

                }
            }

            @Override
            public void onFailure(Call<List<Quiz>> call, Throwable t) {

            }
        });
    }

    /**
     * PagerAdapter
     */
    private class PagerAdapterClass extends PagerAdapter {
        private LayoutInflater mInflater;
        int score = 0;

        public PagerAdapterClass(Context c) {
            super();
            mInflater = LayoutInflater.from(c);

        }


        @Override
        public int getCount() {
            return 10;
        }

        @Override
        public Object instantiateItem(final ViewGroup container, int position) {
            View v = null;
            Quiz q = arrayList.get(position);
            String mean1 = arrayList.get(new Random().nextInt(10)).getMean();
            String mean2 = arrayList.get(new Random().nextInt(10)).getMean();
            String mean3 = arrayList.get(new Random().nextInt(10)).getMean();
            LayoutQuizBinding binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.layout_quiz, container, false);
            binding.titletext.setText(q.getWord());
            binding.number1text.setText(mean2);
            binding.number2text.setText(mean3);
            binding.number3text.setText(mean1);
            binding.number4text.setText(q.getMean());
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "정답이 아닙니다!", Toast.LENGTH_SHORT).show();
                    if (((ViewPager) container).getCurrentItem() < 9)
                        ((ViewPager) container).setCurrentItem(((ViewPager) container).getCurrentItem() + 1);
                    else {
                        Toast.makeText(getApplicationContext(), "테스트가 완료되었습니다!!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), QuizResultActivity.class).putExtra("score", score));
                        finish();
                    }
                }
            };
            View.OnClickListener rightlistener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "정답입니다!", Toast.LENGTH_SHORT).show();
                    score = score + 100;
                    if (((ViewPager) container).getCurrentItem() < 9)
                        ((ViewPager) container).setCurrentItem(((ViewPager) container).getCurrentItem() + 1);
                    else {
                        Toast.makeText(getApplicationContext(), "테스트가 완료되었습니다!!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), QuizResultActivity.class).putExtra("score", score));
                        finish();
                    }
                }
            };
            binding.number1.setOnClickListener(listener);
            binding.number2.setOnClickListener(listener);
            binding.number3.setOnClickListener(listener);
            binding.number4.setOnClickListener(rightlistener);
            container.addView(binding.getRoot(), 0);
            return binding.getRoot();
        }

        @Override
        public void destroyItem(View pager, int position, Object view) {
            ((ViewPager) pager).removeView((View) view);
        }

        @Override
        public boolean isViewFromObject(View pager, Object obj) {
            return pager == obj;
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
        }

        @Override
        public void finishUpdate(View arg0) {
        }
    }

}
