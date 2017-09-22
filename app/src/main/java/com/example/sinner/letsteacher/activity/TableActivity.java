package com.example.sinner.letsteacher.activity;


import android.app.Activity;
import android.app.ActionBar;
import android.app.FragmentTransaction;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sinner.letsteacher.R;
import com.example.sinner.letsteacher.adapter.TestAdapter;

import java.util.ArrayList;
import java.util.List;

public class TableActivity extends Activity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v13.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private RVPAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        //mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());
        ArrayList<RecyclerView> data=new ArrayList<>();
        ArrayList<String>  strs=new ArrayList<>();
        for (int j = 0; j < 30; j++) {
            strs.add(" "+j);
        }

        for (int i = 0; i < 3; i++) {
            RecyclerView rv= (RecyclerView) getLayoutInflater().inflate(R.layout.layout_testrv,null);
            rv.setLayoutManager(new GridLayoutManager(this,3));
            rv.setAdapter(new TestAdapter(strs));
            //此处需要加入rv的touchhelp

            data.add(rv);
        }
        adapter=new RVPAdapter(this,data);
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(3);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_table, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_table, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }

    private class RVPAdapter extends PagerAdapter {

        List<RecyclerView> data;
        Context context;

        public RVPAdapter(Context context,List<RecyclerView> data){
            this.data=data;
            this.context=context;
        }
        @Override
        public int getCount() {
            if(data!=null)
            return data.size();
            return 0;
        }

        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            view.addView(data.get(position % data.size()));
            return data.get(position % data.size());
        }

        @Override
        public void destroyItem(ViewGroup view, int position, Object object) {
            // 从viewpager里移除childview
            view.removeView(data.get(position % data.size()));
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }
    }


    public class TestAdapter extends RecyclerView.Adapter<TestAdapter.ViewHolder> {

        private List<String> data;
        private TestAdapter.OnEventClick onEventClick;
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_test, parent, false), onEventClick);

        }

        public TestAdapter(List<String> data){
            this.data=data;
        }

        @Override
        public void onBindViewHolder (ViewHolder holder,int position){
            if(data!=null){
                holder.tv.append(data.get(position));
            }
        }

        @Override
        public int getItemCount () {
            if (data != null) return data.size();
            return 0;
        }
    //abstract int getItemLayout();


        public  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            private TestAdapter.OnEventClick onEventClick;
            private TextView tv;
            public ViewHolder(View itemView,TestAdapter.OnEventClick onEventClick) {
                super(itemView);
                this.onEventClick=onEventClick;
                tv= (TextView) itemView.findViewById(R.id.tv_testview);
                itemView.setOnClickListener(this);
            }


            @Override
            public void onClick(View view) {
                //getPosition 位置出现偏差，和adapterposition会因为你addview导致下标不正确
                onEventClick.onItemClick(view,getAdapterPosition());
            }
        }

        public abstract  class OnEventClick {
            public abstract void onItemClick(View view, int position);
        }
    }
}
