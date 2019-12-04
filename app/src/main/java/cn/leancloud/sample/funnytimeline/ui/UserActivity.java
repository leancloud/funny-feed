package cn.leancloud.sample.funnytimeline.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import cn.leancloud.AVObject;
import cn.leancloud.AVParcelableObject;
import cn.leancloud.AVUser;
import cn.leancloud.sample.funnytimeline.R;
import cn.leancloud.sample.funnytimeline.ui.main.ContentListFragment;

public class UserActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.user_activity);
    AVParcelableObject parcelableObject = getIntent().getParcelableExtra("user");
    AVObject userObject = null != parcelableObject? parcelableObject.object() : null;
    AVUser user = new AVUser();
    user.resetServerData(userObject.getServerData());
    if (savedInstanceState == null) {
      ContentListFragment fragment = ContentListFragment.newInstance(
          ContentListFragment.StatusType.OwnedStatus);
      fragment.setTargetUser(user);
      getSupportFragmentManager().beginTransaction()
          .replace(R.id.container, fragment)
          .commitNow();
      ActionBar actionBar = getSupportActionBar();
      if (actionBar != null) {
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(user.getUsername());
      }
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      // Respond to the action bar's Up/Home button
      case android.R.id.home:
        System.out.println("onHomePressed");
        finish();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onBackPressed() {
    System.out.println("onBackPressed");
    super.onBackPressed();
    finish();
  }
}
