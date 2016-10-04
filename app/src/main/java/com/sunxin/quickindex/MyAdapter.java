package com.sunxin.quickindex;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by sunxin on 2016/10/3.
 */
public class MyAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Friend> datas = new ArrayList<>();

    public MyAdapter(Context context, ArrayList<Friend> datas) {
        this.mContext = context;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.adapter_view, null);
        }
        ViewHolder holder = ViewHolder.getHolder(convertView);

        Friend friend = datas.get(position);
        holder.name.setText(friend.name);

        //获得当前的字母
        String currentWord = friend.pinYin.charAt(0) + "";

        if (position > 0) {
            //获得上一个字母
            String lastWord = datas.get(position-1).pinYin.charAt(0)+"";
            if (currentWord.equals(lastWord)) {
                //当前的字母和上一个字母相同，那么就隐藏firstLetter
                holder.firstLetter.setVisibility(View.GONE);
            }else {
                holder.firstLetter.setVisibility(View.VISIBLE);
                holder.firstLetter.setText(currentWord);
            }
        }else{
            //显示当前
            holder.firstLetter.setVisibility(View.VISIBLE);
            holder.firstLetter.setText(currentWord);
        }

        //设置只显示首字母，因为charAt的返回值是char类型，
        // 会报出Resources$NotFoundException: String resource ID #0x41的异常，因为走了setText(int resId)
        //的构造，找不着资源id，只需要把其转成字符串即可


        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    //抽取adapter到ViewHolder
    static class ViewHolder {

        TextView name, firstLetter;

        public ViewHolder(View convertView) {
            name = (TextView) convertView.findViewById(R.id.tv_name);
            firstLetter = (TextView) convertView.findViewById(R.id.tv_first_letter);
        }

        public static ViewHolder getHolder(View convertView) {
            ViewHolder holder = (ViewHolder) convertView.getTag();
            if (holder == null) {
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }
            return holder;
        }
    }

}
