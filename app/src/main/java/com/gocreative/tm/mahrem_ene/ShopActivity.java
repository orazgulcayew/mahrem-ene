package com.gocreative.tm.mahrem_ene;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.gocreative.tm.mahrem_ene.Adapters.ShopFragmentAdapter;
import com.google.android.material.tabs.TabLayout;

public class ShopActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager2 viewPager;
    ShopFragmentAdapter fragmentAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        tabLayout = findViewById(R.id.login_tab_layout);
        viewPager = findViewById(R.id.login_view_pager);

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentAdapter = new ShopFragmentAdapter(fragmentManager, getLifecycle());
        viewPager.setAdapter(fragmentAdapter);

        tabLayout.addTab(tabLayout.newTab().setText("Çaga oýunjaklary"));
        tabLayout.addTab(tabLayout.newTab().setText("Çaga eşikleri"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });


    }
}