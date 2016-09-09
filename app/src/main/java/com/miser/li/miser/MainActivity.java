package com.miser.li.miser;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/*public class MainActivity extends FragmentActivity implements View.OnClickListener,
        OnPageChangeListener{*/

public class MainActivity extends FragmentActivity implements OnClickListener,
        OnPageChangeListener
{

    private ViewPager mViewPager;
    private List<Fragment> mTables = new ArrayList<>();
    private String[] mTitles = new String[]{
            "allmaster","conversation","alarms","setting"};
    private FragmentPagerAdapter mAdapter;
    private List<ChangeColorIconWithText> mTabIndicators = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mAlarmsListView = (ListView) findViewById(R.id.alarms_main_lv);//创建列表实例
        initView();
        initDatas();
        mViewPager.setAdapter(mAdapter);
        initEvent();

    }
    private void initEvent()
    {

        mViewPager.setOnPageChangeListener(this);

    }

    private void initDatas()
    {

        MasterFragment masterFragment = new MasterFragment();
        Bundle bundle = new Bundle();
        bundle.putString(masterFragment.TITLE, mTitles[0]);
        masterFragment.setArguments(bundle);
        mTables.add(masterFragment);

        ConversationFragment conversationFragment = new ConversationFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putString(conversationFragment.TITLE, mTitles[1]);
        conversationFragment.setArguments(bundle1);
        mTables.add(conversationFragment);

        AlarmsFragment alarmsFragment = new AlarmsFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putString(alarmsFragment.TITLE, mTitles[2]);
        alarmsFragment.setArguments(bundle2);
        mTables.add(alarmsFragment);

        SettingFragment settingFragment = new SettingFragment();
        Bundle bundle3 = new Bundle();
        bundle3.putString(settingFragment.TITLE, mTitles[3]);
        settingFragment.setArguments(bundle3);
        mTables.add(settingFragment);
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager())
        {

            @Override
            public int getCount()
            {
                return mTables.size();
            }

            @Override
            public Fragment getItem(int position)
            {
                return mTables.get(position);
            }
        };
    }

    private void initView()
    {
        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);

        ChangeColorIconWithText one = (ChangeColorIconWithText) findViewById(R.id.id_indicator_one);
        mTabIndicators.add(one);
        ChangeColorIconWithText two = (ChangeColorIconWithText) findViewById(R.id.id_indicator_two);
        mTabIndicators.add(two);
        ChangeColorIconWithText three = (ChangeColorIconWithText) findViewById(R.id.id_indicator_three);
        mTabIndicators.add(three);
        ChangeColorIconWithText four = (ChangeColorIconWithText) findViewById(R.id.id_indicator_four);
        mTabIndicators.add(four);

        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);

        one.setIconAlpha(1.0f);

    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void setOverflowButtonAlways()
    {
        try
        {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKey = ViewConfiguration.class
                    .getDeclaredField("sHasPermanentMenuKey");
            menuKey.setAccessible(true);
            menuKey.setBoolean(config, false);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
*/
    /**
     * 设置menu显示icon
     */
   /* @Override
    public boolean onMenuOpened(int featureId, Menu menu)
    {

        if (featureId == Window.FEATURE_ACTION_BAR && menu != null)
        {
            if (menu.getClass().getSimpleName().equals("MenuBuilder"))
            {
                try
                {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }

        return super.onMenuOpened(featureId, menu);
    }
*/
    @Override
    public void onClick(View v)
    {
        clickTab(v);

    }

    /**
     * 点击Tab按钮
     *
     * @param v
     */
    private void clickTab(View v)
    {
        resetOtherTabs();

        switch (v.getId())
        {
            case R.id.id_indicator_one:
                mTabIndicators.get(0).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(0, false);
                break;
            case R.id.id_indicator_two:
                mTabIndicators.get(1).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(1, false);
                break;
            case R.id.id_indicator_three:
                mTabIndicators.get(2).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(2, false);
                break;
            case R.id.id_indicator_four:
                mTabIndicators.get(3).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(3, false);
                break;
        }
    }

    /**
     * 重置其他的TabIndicator的颜色
     */
    private void resetOtherTabs()
    {
        for (int i = 0; i < mTabIndicators.size(); i++)
        {
            mTabIndicators.get(i).setIconAlpha(0);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset,
                               int positionOffsetPixels)
    {
        // Log.e("TAG", "position = " + position + " ,positionOffset =  "
        // + positionOffset);
        if (positionOffset > 0)
        {
            ChangeColorIconWithText left = mTabIndicators.get(position);
            ChangeColorIconWithText right = mTabIndicators.get(position + 1);
            left.setIconAlpha(1 - positionOffset);
            right.setIconAlpha(positionOffset);
        }

    }

    @Override
    public void onPageSelected(int position)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageScrollStateChanged(int state)
    {
        // TODO Auto-generated method stub

    }



}
