package cn.leancloud.sample.funnytimeline;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import cn.leancloud.AVUser;
import cn.leancloud.sample.funnytimeline.ui.PublishActivity;
import cn.leancloud.sample.funnytimeline.ui.SettingsActivity;
import cn.leancloud.sample.funnytimeline.ui.login.LoginActivity;
import cn.leancloud.sample.funnytimeline.ui.main.SectionsPagerAdapter;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
    ViewPager viewPager = findViewById(R.id.view_pager);
    viewPager.setAdapter(sectionsPagerAdapter);
    TabLayout tabs = findViewById(R.id.tabs);
    tabs.setupWithViewPager(viewPager);

    FloatingActionButton fab = findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent publishIntent = new Intent(MainActivity.this, PublishActivity.class);
        MainActivity.this.startActivity(publishIntent);
//        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//            .setAction("Action", null).show();
      }
    });
    Button settingButton = findViewById(R.id.setting_button);
    settingButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent settingIntent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(settingIntent);
      }
    });
  }

  @Override
  protected void onStart() {
    super.onStart();
    AVUser currentUser = AVUser.currentUser();
    if (null == currentUser || !currentUser.isAuthenticated()) {
      Intent loginIntent = new Intent(this, LoginActivity.class);
      startActivity(loginIntent);
    }
  }
}