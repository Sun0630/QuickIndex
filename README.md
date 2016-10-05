# QuickIndex
很常见的快速索引项目，能够通过右边的索引栏通过拼音的首字母快速定位
![image](https:/github.com/Sun0630/QuickIndex/raw/master/qucikindex.gif)

##快速索引

>绘制文字的原点，在文字的左下角

> 绘制26个字母，并均匀的竖直排列

- 获取文本的x坐标，就是文本的底部边框的一半

		float x = mWidth / 2;

- 获取文本的y坐标，y = 格子的高度 / 2 + 文本的高度 / 2 + index * 格子的高度

		float y = cellHeight / 2 + height / 2 + i * cellHeight;

- 获得文本的高度

		/**
	     * 获得文本的高度
	     *
	     * @param text
	     */
	    private int getTextHeight(String text) {
	        Rect bounds = new Rect();
	        paint.getTextBounds(text, 0, text.length(), bounds);
	        return bounds.height();
	    }


>当触摸到列表中的某个字母的时候就获得该字母的内容。

	 private int lastIndex = -1; //记录上一次的索引

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                //获取当前的字母的索引
                float y = event.getY();
                //得到索引
                int index = (int) (y / cellHeight);

                if (index != lastIndex) {

                    //安全性的检查
                    if (index >= 0 && index <= indexArr.length-1){
                        if (mOnTouchLetterListener != null){
                            mOnTouchLetterListener.onTouchLetter(indexArr[index]);
                        }
                    }
                }
                lastIndex = index;
                break;
            case MotionEvent.ACTION_UP:
                //重置索引
                lastIndex = -1;
                break;
        }
        return true;
    }

> 提供接口回调，把当前触摸的字母内容暴露给外界

	 private OnTouchLetterListener mOnTouchLetterListener;

    public void setOnTouchLetterListener(OnTouchLetterListener mOnTouchLetterListener){
        this.mOnTouchLetterListener = mOnTouchLetterListener;
    }

    /**
     * 当触摸字母的时候的回调
     */
    public interface OnTouchLetterListener{
        void onTouchLetter(String letter);
    }


> 点击某个字母或滑动的时候，让字体的颜色改变

- 首先在onTouchEvent方法的最后调用重绘

	 	//重绘，会马上去执行onDraw方法。改变点击触摸到的字母的颜色
        invalidate();
- 然后在onDraw方法绘制之前设置字体的颜色

		//改变触摸到的字母的颜色
	    paint.setColor(lastIndex == i ? Color.BLACK : Color.WHITE);



>###抽取adapter的内容到ViewHolder中

 	@Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            convertView = View.inflate(mContext,R.layout.adapter_view,null);
        }
        ViewHolder holder = ViewHolder.getHolder(convertView);

        Friend friend = datas.get(position);
        holder.name.setText(friend.name);
        holder.firstLetter.setText(friend.pinYin);


        return convertView;
    }



	//抽取adapter到ViewHolder
    static class ViewHolder {

        TextView name,firstLetter;

        public ViewHolder(View convertView){
            name = (TextView) convertView.findViewById(R.id.tv_name);
            firstLetter = (TextView) convertView.findViewById(R.id.tv_first_letter);
        }

        public static ViewHolder getHolder(View convertView){
            ViewHolder holder = (ViewHolder) convertView.getTag();
            if (holder == null){
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }
            return holder;
        }
    }

--- 

> ###只显示首字母

		//设置只显示首字母，因为charAt的返回值是char类型，
        // 会报出Resources$NotFoundException: String resource ID #0x41的异常，因为走了setText(int resId)
        //的构造，找不着资源id，只需要把其转成字符串即可
        holder.firstLetter.setText(friend.pinYin.charAt(0)+"");


> ###隐藏和上一个首字母相同的选项

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

>###根据触摸的当前字母，去ListView中找到item的首字母和letter一样的，然后放到屏幕顶端

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
            }
        });


>###在屏幕中间显示的首字母框框，其实是一个TextView

 	<TextView
        android:layout_centerInParent="true"
        android:id="@+id/tv_letter"
        android:text="A"
        android:textSize="50sp"
        android:gravity="center"
        android:background="@drawable/bg"
        android:textStyle="bold|italic"
        android:layout_width="110dp"
        android:layout_height="110dp"/>

	<shape xmlns:android="http://schemas.android.com/apk/res/android"
   		 android:shape="oval"
    >

    <solid android:color="@color/colorAccent"/>
	</shape>	

--- 
	//逻辑代码
	//使用handler完成过一段时间做某事，在1秒之后让框框消失
    private Handler mHandler = new Handler();

    private void showCurrentLetter(String letter) {
        //显示中间的框框
        mLetter.setVisibility(View.VISIBLE);
        mLetter.setText(letter);

        //开始之前先清除一下
        mHandler.removeCallbacksAndMessages(null);

        //设置框框在1秒之后消失
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //这里面是在主线程中运行的
                mLetter.setVisibility(View.GONE);
            }
        },1000);

    }


---
>###添加框框显示的动画效果，使用缩放的属性动画。
>用到nineoldandroid库

 		//使用缩放动画让框框消失
        ViewHelper.setScaleX(mLetter,0f);
        ViewHelper.setScaleY(mLetter,0f);


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
