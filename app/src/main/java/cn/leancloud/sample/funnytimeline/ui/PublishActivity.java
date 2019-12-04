package cn.leancloud.sample.funnytimeline.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import cn.leancloud.AVStatus;
import cn.leancloud.sample.funnytimeline.R;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;

public class PublishActivity extends AppCompatActivity {
  private EditText editText;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_publish);

    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
      actionBar.setDisplayShowTitleEnabled(false);
    }
    this.editText = findViewById(R.id.status_text);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.publish_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      // Respond to the action bar's Up/Home button
      case android.R.id.home:
        System.out.println("onHomePressed");
        finish();
        return true;
      case R.id.action_publish:
        String content = this.editText.getText().toString();
        System.out.println("onPublishPressed: " + content);
        if (null != content && content.trim().length() > 0) {
          AVStatus status = AVStatus.createStatus("", content.trim());
          status.sendToFollowersInBackground().subscribe(new Observer<AVStatus>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(AVStatus avStatus) {
              finish();
            }

            @Override
            public void onError(Throwable e) {
              Snackbar.make(editText, "failed to publish. cause: " + e.getMessage(), Snackbar.LENGTH_SHORT)
                  .setAction("Action", null).show();
            }

            @Override
            public void onComplete() {

            }
          });
        } else {
          finish();
        }
        return true;
    }
    return super.onOptionsItemSelected(item);
  }
}
