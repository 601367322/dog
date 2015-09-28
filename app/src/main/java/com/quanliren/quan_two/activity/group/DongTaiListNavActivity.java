package com.quanliren.quan_two.activity.group;

import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.base.BaseActivity;
import com.quanliren.quan_two.activity.redline.RedLineFragment;
import com.quanliren.quan_two.activity.redline.RedLineNavFragment_;

import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_only_fragment)
public class DongTaiListNavActivity extends BaseActivity {

    @Override
    public void init() {
        super.init();
//        getSupportActionBar().hide();
        getSupportFragmentManager().beginTransaction().replace(R.id.content, RedLineNavFragment_.builder().leftMode(RedLineFragment.RedLineMode.HOT).rightMode(RedLineFragment.RedLineMode.NEW).leftTabText(getString(R.string.month_hot)).rightTabText(getString(R.string.newset_publish)).build()).commitAllowingStateLoss();
    }

    /*public static final int QuanActivityPublishResponse = 1;

    @ViewById
    TabLinearLayout bottom_tab;
    @ViewById
    ViewPager viewpager;
    List<Fragment> views = new ArrayList<Fragment>();
    FragmentPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void initView() {
        final List<TabLinearLayout.TabBean> list = new ArrayList<TabLinearLayout.TabBean>();
        list.add(new TabLinearLayout.TabBean(R.drawable.ic_date_tab2, "附近动态"));
        list.add(new TabLinearLayout.TabBean(R.drawable.ic_dongtai_tab1, "我关注的"));
//        list.add(new TabLinearLayout.TabBean(R.drawable.ic_dongtai_tab2, "评价我的"));

        bottom_tab.setDate(list);
        bottom_tab.setListener(this);

        views.add(QuanListFragment_.builder().listType(QuanListFragment.LISTTYPE.ALL).build());
        views.add(QuanListFragment_.builder().listType(QuanListFragment.LISTTYPE.MYCARE).build());

//        QuanAboutMeFragment_ pull2 = new QuanAboutMeFragment_();
//        views.add(pull2);
        viewpager.setOnPageChangeListener(new OnPageChangeListener() {
            public void onPageSelected(int arg0) {
                bottom_tab.setCurrentIndex(arg0);
                getSupportActionBar().setTitle(list.get(arg0).getText());
            }

            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            public void onPageScrollStateChanged(int arg0) {
            }
        });
        viewpager.setAdapter(adapter = new mPagerAdapter(getSupportFragmentManager()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onTabClick(int position) {
        if (position > 0) {
            if (getHelper().getUser() == null) {
                startLogin();
                return;
            }
        }
        viewpager.setCurrentItem(position);
    }

    class mPagerAdapter extends FragmentPagerAdapter {

        public mPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int arg0) {
            return views.get(arg0);
        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position,
                                   Object object) {
            ((LoaderImpl) views.get(position)).refresh();
            super.setPrimaryItem(container, position, object);
        }
    }

    @Click
    public void dongtai_publish() {
        if (Util.isFastDoubleClick()) {
            return;
        }
        if (getHelper().getUser() == null) {
            startLogin();
            return;
        }
        PublishActivity_.intent(this).startForResult(QuanActivityPublishResponse);
    }

    @OnActivityResult(QuanActivityPublishResponse)
    void onPublishResult(int result, Intent data) {
        switch (result) {
            case RESULT_OK:
                if(viewpager.getCurrentItem()==0){
                    ((BaseListFragment)(views.get(0))).swipeRefresh();
                }
                break;
        }
    }*/
}
