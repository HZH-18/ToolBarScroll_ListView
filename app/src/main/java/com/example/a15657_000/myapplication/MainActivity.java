package com.example.a15657_000.myapplication;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity{
    private static final String TAG = MainActivity.class.getSimpleName();

    private Toolbar mMainToolbar = null;
    private ListView mMainListView = null;
    private String[] mStr = new String[20];

    private float mStartY = 0, mLastY = 0, mLastDeltaY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainToolbar = (Toolbar)this.findViewById(R.id.main_bar);


        for (int i = 0; i < mStr.length; i++) {
            mStr[i] = "Item " + i;
        }
        mMainListView = (ListView)this.findViewById(R.id.main_list_view);
        final View header = LayoutInflater.from(this).inflate(R.layout.layout_header, null);
        mMainListView.addHeaderView(header);
        mMainListView.setAdapter(new ArrayAdapter<String>(
                MainActivity.this,
                android.R.layout.simple_expandable_list_item_1,
                mStr));


        mMainListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final float y = event.getY();
                float translationY = mMainToolbar.getTranslationY();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
//                        Log.v(TAG, "Down");
                        mStartY = y;
                        mLastY = mStartY;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float mDeltaY = y - mLastY;

                        float newTansY = translationY + mDeltaY;
                        if (newTansY <= 0 && newTansY >= -mMainToolbar.getHeight()) {
                            mMainToolbar.setTranslationY(newTansY);
                        }
                        mLastY = y;
                        mLastDeltaY = mDeltaY;
//                        Log.v(TAG, "Move");
                        break;
                    case MotionEvent.ACTION_UP:
                        ObjectAnimator animator = null;
                        Log.d(TAG, "mLastDeltaY=" + mLastDeltaY);
                        if (mLastDeltaY < 0 && mMainListView.getFirstVisiblePosition() > 1) {
                            Log.v(TAG, "listView.first=" + mMainListView.getFirstVisiblePosition());
                            animator = ObjectAnimator.ofFloat(mMainToolbar, "translationY", mMainToolbar.getTranslationY(), -mMainToolbar.getHeight());
                        } else {
                            animator = ObjectAnimator.ofFloat(mMainToolbar, "translationY", mMainToolbar.getTranslationY(), 0);
                        }
                        animator.setDuration(100);
                        animator.start();
                        animator.setInterpolator(AnimationUtils.loadInterpolator(MainActivity.this, android.R.interpolator.linear));
//                        Log.v(TAG, "Up");
                        break;
                }
                return false;
            }
        });
    }
}
