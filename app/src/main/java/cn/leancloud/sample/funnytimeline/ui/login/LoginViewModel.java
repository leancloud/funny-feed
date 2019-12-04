package cn.leancloud.sample.funnytimeline.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;

import cn.leancloud.AVUser;
import cn.leancloud.sample.funnytimeline.data.LoginRepository;
import cn.leancloud.sample.funnytimeline.R;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class LoginViewModel extends ViewModel {

  private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
  private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
  private LoginRepository loginRepository;

  LoginViewModel(LoginRepository loginRepository) {
    this.loginRepository = loginRepository;
  }

  LiveData<LoginFormState> getLoginFormState() {
    return loginFormState;
  }

  LiveData<LoginResult> getLoginResult() {
    return loginResult;
  }

  public void login(String username, String password) {
    // can be launched in a separate asynchronous job
    System.out.println("login start....");
    Observable<AVUser> result = loginRepository.login(username, password);

    result.subscribe(new Observer<AVUser>() {
      @Override
      public void onSubscribe(Disposable d) {

      }

      @Override
      public void onNext(AVUser avUser) {
        loginResult.setValue(new LoginResult(new LoggedInUserView(avUser.getUsername())));
        System.out.println("succeed to login!");
      }

      @Override
      public void onError(Throwable e) {
        loginResult.setValue(new LoginResult(R.string.login_failed));
      }

      @Override
      public void onComplete() {

      }
    });
  }

  public void loginDataChanged(String username, String password) {
    if (!isUserNameValid(username)) {
      loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
    } else if (!isPasswordValid(password)) {
      loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
    } else {
      loginFormState.setValue(new LoginFormState(true));
    }
  }

  // A placeholder username validation check
  public static boolean isUserNameValid(String username) {
    if (username == null) {
      return false;
    }
    if (username.contains("@")) {
      return Patterns.EMAIL_ADDRESS.matcher(username).matches();
    } else {
      return !username.trim().isEmpty();
    }
  }

  // A placeholder password validation check
  public static boolean isPasswordValid(String password) {
    return password != null && password.trim().length() > 5;
  }
}
