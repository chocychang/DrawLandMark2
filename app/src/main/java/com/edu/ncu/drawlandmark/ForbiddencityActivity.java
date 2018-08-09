package com.edu.ncu.drawlandmark;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class ForbiddencityActivity extends Activity implements ViewPager.OnPageChangeListener{

    private ViewPager vp;
    private LinearLayout ll_point;
    private int[] imageResIds; //存放?片?源id的??
    private ArrayList<ImageView> imageViews; //存放?片的集合
    private String[] contentDescs; //?片?容描述
    private int lastPosition;
    private boolean isRunning = false; //viewpager是否在自???

    Button bt_guess2;
    Button bt_drawingFun_inK;
    Button bt_playmusic;
    Button bt_profolio;

    private MediaPlayer mp;
    int gainCoin = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forbiddencity);

        //使用M-V-C模型
        //V--view??
        initViews();
        //M--model?据
        initData();
        //C--control控制器(即适配器)
        initAdapter();
        //???片的自???
        new Thread() {
            @Override
            public void run() {
                isRunning = true;
                while (isRunning) {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() { //在子?程中??子?程
                            //往下翻一?（setCurrentItem方法用??置ViewPager的?前?）
                            vp.setCurrentItem(vp.getCurrentItem() + 1);
                        }
                    });
                }
            }
        }.start();
    }

    /*
        初始化??
     */
    private void initViews() {
        //初始化放小??的控件
        ll_point = (LinearLayout) findViewById(R.id.ll_point);
        //初始化ViewPager控件
        vp = (ViewPager) findViewById(R.id.vp);
        //?置ViewPager的???听
        vp.setOnPageChangeListener(this);
        //?示?片描述信息的控件

        bt_playmusic = (Button) findViewById(R.id.bt_playmusic);
        mp =MediaPlayer.create(this, R.raw.forbiddencity);

        bt_playmusic.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mp.start();
                getListenCoin();
            }
        });

        this.bt_guess2 = (Button) findViewById(R.id.bt_guess2);
        bt_guess2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                startActivity( new Intent(ForbiddencityActivity.this, QueMenuActivity.class) );
            }
        });

        this.bt_drawingFun_inK = (Button) findViewById(R.id.bt_drawingFun_inK);
        bt_drawingFun_inK.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                String localName = "forbiddencity";
                Intent intent = new Intent(ForbiddencityActivity.this, ChooseDraw.class);
                Bundle bundle = new Bundle();
                //儲存資料　第一個為參數key，第二個為Value
                bundle.putString("passLocalName",localName);
                intent.putExtras(bundle);
                startActivity( intent );
            }
        });

        this.bt_profolio = (Button) findViewById(R.id.bt_profolio);
        bt_profolio.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                new AlertDialog.Builder(ForbiddencityActivity.this)
                        .setTitle("近請期待")
                        .setMessage("目前暫未開放此功能,非常抱歉QQ")
                        .setPositiveButton("我知道了",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })

                        .show();
            }
        });
    }

    /*
      初始化?据
     */
    private void initData() {
        //初始化填充ViewPager的?片?源
        imageResIds = new int[]{R.drawable.forbiddencity_1, R.drawable.forbiddencity_2, R.drawable.forbiddencity_3, R.drawable.forbiddencity_4, R.drawable.forbiddencity_5};
        //?片的描述信息

        //保存?片?源的集合
        imageViews = new ArrayList<>();
        ImageView imageView;
        View pointView;
        //循?遍??片?源，然后保存到集合中
        for (int i = 0; i < imageResIds.length; i++) {
            //添加?片到集合中
            imageView = new ImageView(this);
            imageView.setBackgroundResource(imageResIds[i]);
            imageViews.add(imageView);

            //加小白?，指示器（?里的小??定?在了drawable下的??器中了，也可以用小?片代替）
            pointView = new View(this);
            pointView.setBackgroundResource(R.drawable.point_selector); //使用??器?置背景
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100, 20);
            if (i != 0) {
                //如果不是第一??，??置?的左?距
                layoutParams.leftMargin = 10;
            }
            pointView.setEnabled(false); //默?都是暗色的
            ll_point.addView(pointView, layoutParams);
        }
    }

    public void getListenCoin(){
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
                Intent intent = new Intent(ForbiddencityActivity.this, GainXPCoinDialog.class);
                intent.putExtra("gainCoin",gainCoin);
                startActivity(intent);

            }
        } );
    }

    /*
      初始化适配器
     */
    private void initAdapter() {
        ll_point.getChildAt(0).setEnabled(true); //初始化控件?，?置第一?小???亮色
        lastPosition = 0; //?置之前的位置?第一?
        vp.setAdapter(new MyPagerAdapter());
        //?置默??示中?的某?位置（??可以左右滑?），???只有在整?范??，可以?便?置
        vp.setCurrentItem(5000000); //?示5000000??位置的?片
    }

    //界面???，停止viewpager的??
    @Override
    protected void onDestroy() {
        super.onDestroy();
        isRunning = false;
        mp.release();
    }

    /*
      自定?适配器，?承自PagerAdapter
     */
    class MyPagerAdapter extends PagerAdapter {

        //返回?示?据的???，?了???限循?，把返回的值?置?最大整?
        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        //指定复用的判???，固定?法：view == object
        @Override
        public boolean isViewFromObject(View view, Object object) {
            //??建新的?目，又反回?，判?view是否可以被复用(即是否存在)
            return view == object;
        }

        //返回要?示的?目?容
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //container  容器  相?于用?存放imageView
            //?集合中?得?片
            int newPosition = position % 5; //??中?共有5??片，超????度?，取摸，防止下?越界
            ImageView imageView = imageViews.get(newPosition);
            //把?片添加到container中
            container.addView(imageView);
            //把?片返回?框架，用??存
            return imageView;
        }

        //???目
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //object:?才?建的?象，即要??的?象
            container.removeView((View) object);
        }
    }

    //--------------以下是?置ViewPager的???听所需??的方法--------
    //?面滑?
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    //新的?面被?中
    @Override
    public void onPageSelected(int position) {
        //?前的位置可能很大，?了防止下?越界，?要?示的?片的???行取余
        int newPosition = position % 5;

        //?置小???高亮或暗色
        ll_point.getChildAt(lastPosition).setEnabled(false);
        ll_point.getChildAt(newPosition).setEnabled(true);
        lastPosition = newPosition; //??之前的?
    }

    //?面滑????生改?
    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
