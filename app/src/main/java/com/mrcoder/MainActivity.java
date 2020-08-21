package com.mrcoder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.mrcoder.databinding.ActivityMainBinding;
import com.mrcoder.menu.CallsFragment;
import com.mrcoder.menu.ChatsFragment;
import com.mrcoder.menu.StatusFragment;
import com.mrcoder.view.contact.ContactsActivity;
import com.mrcoder.view.settings.SettingsActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setUpWithViewPager(binding.viewPager);
        binding.tabLayout.setupWithViewPager(binding.viewPager);
        setSupportActionBar(binding.toolBar);

        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                changeFabIcon(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    private void setUpWithViewPager(ViewPager viewPager) {
        MainActivity.SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new ChatsFragment(), "Chats");
        adapter.addFragment(new StatusFragment(), "Status");
        adapter.addFragment(new CallsFragment(), "Calls");

        viewPager.setAdapter(adapter);

    }

    private static class SectionsPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager manager) {
            super(manager);

        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_search:
                Toast.makeText(MainActivity.this, "Action Search", Toast.LENGTH_LONG).show();
                break;
            case R.id.menu_group:
                Toast.makeText(MainActivity.this, "Action More", Toast.LENGTH_LONG).show();
                break;
            case R.id.menu_newGroup:
                Toast.makeText(MainActivity.this, "Action New Group", Toast.LENGTH_LONG).show();
                break;
            case R.id.menu_newBroadcast:
                Toast.makeText(MainActivity.this, "Action New Broadcast", Toast.LENGTH_LONG).show();
                break;
            case R.id.menu_whatsappWeb:
                Toast.makeText(MainActivity.this, "Action WhatsApp Web", Toast.LENGTH_LONG).show();
                break;
            case R.id.menu_starredmesaages:
                Toast.makeText(MainActivity.this, "Action Starred Meassages", Toast.LENGTH_LONG).show();
                break;
            case R.id.menu_settings:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void changeFabIcon(final int index) {
        binding.floatingActionButton.hide();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (index) {
                    case 0:
                        binding.floatingActionButton.setImageDrawable(getDrawable(R.drawable.ic_chat));
                        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(MainActivity.this, ContactsActivity.class));
                            }
                        });
                        break;
                    case 1:
                        binding.floatingActionButton.setImageDrawable(getDrawable(R.drawable.ic_call));
                        break;
                    case 2:
                        binding.floatingActionButton.setImageDrawable(getDrawable(R.drawable.ic_camera));
                        break;
                }
                binding.floatingActionButton.show();
            }
        }, 400);
    }
}