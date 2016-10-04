package com.sunxin.quickindex;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.OvershootInterpolator;
import android.widget.ListView;
import android.widget.TextView;

import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";
    private QuickIndexBar mQuickIndexBar;
    private ListView mListView;
    private TextView mLetter;

    private ArrayList<Friend> friends = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mQuickIndexBar = (QuickIndexBar) findViewById(R.id.quickIndexBar);
        mListView = (ListView) findViewById(R.id.listview);
        mLetter = (TextView) findViewById(R.id.tv_letter);//框框
        //准备数据
        fillList();
        //对数据进行排序
        Collections.sort(friends);

        mListView.setAdapter(new MyAdapter(this, friends));

        mQuickIndexBar.setOnTouchLetterListener(new QuickIndexBar.OnTouchLetterListener() {
            @Override
            public void onTouchLetter(String letter) {
                //根据当前触摸的字母，去列表中找到那个item的首字母和letter相同的，然后放置屏幕顶端
                for (int i = 0; i < friends.size(); i++) {
                    //得到当前触摸的字母
                    String firstLetter = friends.get(i).pinYin.charAt(0) + "";
                    if (firstLetter.equals(letter)){
                        //说明首字母相同，那么就放置到屏幕的顶端
                        mListView.setSelection(i);
                        //只需要找到第一个就好了
                        break;
                    }
                }



                //显示当前字母在框框中
                showCurrentLetter(letter);


            }
        });


        //使用缩放动画让框框消失
        ViewHelper.setScaleX(mLetter,0f);
        ViewHelper.setScaleY(mLetter,0f);
    }

    //使用handler完成过一段时间做某事，在1秒之后让框框消失
    private Handler mHandler = new Handler();

    private void showCurrentLetter(String letter) {
        //显示中间的框框
//        mLetter.setVisibility(View.VISIBLE);
        //使用缩放动画显示框框
        ViewPropertyAnimator.animate(mLetter)
                .scaleX(1.0f)
                .setDuration(300)
                .setInterpolator(new OvershootInterpolator())//弹性差值器
                .start();
        ViewPropertyAnimator.animate(mLetter)
                .scaleY(1.0f)
                .setDuration(300)
                .setInterpolator(new OvershootInterpolator())//弹性差值器
                .start();

        mLetter.setText(letter);

        //开始之前先清除一下
        mHandler.removeCallbacksAndMessages(null);

        //设置框框在1秒之后消失
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //这里面是在主线程中运行的
//                mLetter.setVisibility(View.GONE);
                //隐藏也是用属性动画
                ViewPropertyAnimator.animate(mLetter)
                        .scaleX(0)
                        .setDuration(300)
                        .setInterpolator(new OvershootInterpolator())
                        .start();
                ViewPropertyAnimator.animate(mLetter)
                        .scaleY(0)
                        .setDuration(300)
                        .setInterpolator(new OvershootInterpolator())
                        .start();
            }
        },1000);

    }


    private void fillList() {
        // 虚拟数据
        friends.add(new Friend("伊尼戈-洛佩斯"));
        friends.add(new Friend("阿梅奥比"));
        friends.add(new Friend("伊布拉希莫维奇"));
        friends.add(new Friend("伊班"));
        friends.add(new Friend("梅西"));
        friends.add(new Friend("姆希塔扬"));
        friends.add(new Friend("伊沃"));
        friends.add(new Friend("阿圭罗"));
        friends.add(new Friend("奥古斯托"));
        friends.add(new Friend("鲁尼"));
        friends.add(new Friend("科斯切尔尼"));
        friends.add(new Friend("张伯伦"));
        friends.add(new Friend("苏亚雷斯"));
        friends.add(new Friend("亚亚-图雷"));
        friends.add(new Friend("周杰伦"));
        friends.add(new Friend("陈坤2"));
        friends.add(new Friend("王寿挺"));
        friends.add(new Friend("李刚"));
        friends.add(new Friend("哈维"));
        friends.add(new Friend("林俊杰"));
        friends.add(new Friend("阿里"));
        friends.add(new Friend("图兰"));
        friends.add(new Friend("赵四"));
        friends.add(new Friend("尼古拉斯-赵四"));
        friends.add(new Friend("赵子龙"));
        friends.add(new Friend("格列兹曼"));
        friends.add(new Friend("诺伊尔"));
        friends.add(new Friend("罗老汉"));
        friends.add(new Friend("莱万多夫斯基"));
        friends.add(new Friend("格策"));
        friends.add(new Friend("二娃"));
        friends.add(new Friend("汤姆金斯"));
        friends.add(new Friend("罗伊斯"));
        friends.add(new Friend("桑德罗"));
        friends.add(new Friend("马斯切拉诺"));
        friends.add(new Friend("普约尔"));
        friends.add(new Friend("德科"));
        friends.add(new Friend("托雷斯"));
        friends.add(new Friend("戈丁"));
        friends.add(new Friend("JayChou"));
        friends.add(new Friend("皮卡丘"));
        friends.add(new Friend("皮克"));
        friends.add(new Friend("拉基蒂奇"));
        friends.add(new Friend("麦克格文"));
        friends.add(new Friend("哈维尔"));
        friends.add(new Friend("奥特内切"));
        friends.add(new Friend("奥斯皮纳"));
        friends.add(new Friend("拉姆塞"));
        friends.add(new Friend("温格"));
        friends.add(new Friend("瓜迪奥拉"));
        friends.add(new Friend("穆里尼奥"));
        friends.add(new Friend("恩里克"));
        friends.add(new Friend("外力"));
        friends.add(new Friend("罗比尼奥"));
        friends.add(new Friend("罗贝托"));
        friends.add(new Friend("比达尔"));
        friends.add(new Friend("吉鲁"));
        friends.add(new Friend("老司机"));
        friends.add(new Friend("罗斯基"));
    }

}
