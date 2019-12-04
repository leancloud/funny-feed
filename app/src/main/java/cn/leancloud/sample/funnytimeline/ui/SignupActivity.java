package cn.leancloud.sample.funnytimeline.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import cn.leancloud.AVUser;
import cn.leancloud.sample.funnytimeline.R;
import cn.leancloud.sample.funnytimeline.ui.login.LoginViewModel;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignupActivity extends AppCompatActivity {
  private EditText emailText;
  private EditText usernameText;
  private EditText passwordText;
  private Button signupButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_signup);

    this.emailText = findViewById(R.id.email);
    this.usernameText = findViewById(R.id.username);
    this.passwordText = findViewById(R.id.password);
    this.signupButton = findViewById(R.id.signup);

    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
      actionBar.setDisplayShowTitleEnabled(false);
    }

    this.signupButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String email = emailText.getText().toString();
        String username = usernameText.getText().toString();
        String password = passwordText.getText().toString();
        if (!LoginViewModel.isUserNameValid(email)) {
          displayErrorMessage("email is invalid");
        } else if (!LoginViewModel.isPasswordValid(password)) {
          displayErrorMessage("password is invalid");
        } else if (null == username || username.trim().length() < 1) {
          displayErrorMessage("username is invalid");
        } else {
          AVUser user = new AVUser();
          user.setEmail(email);
          user.setUsername(username);
          user.setPassword(password);
          user.signUpInBackground().subscribe(new Observer<AVUser>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(AVUser avUser) {
              finish();
            }

            @Override
            public void onError(Throwable e) {
              displayErrorMessage(e.getMessage());
            }

            @Override
            public void onComplete() {

            }
          });
        }
      }
    });
  }

  private void displayErrorMessage(String message) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      // Respond to the action bar's Up/Home button
      case android.R.id.home:
        finish();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    finish();
  }
}
