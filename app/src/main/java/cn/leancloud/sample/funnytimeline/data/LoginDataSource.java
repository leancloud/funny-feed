package cn.leancloud.sample.funnytimeline.data;

import cn.leancloud.AVUser;
import io.reactivex.Observable;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

  public Observable<AVUser> login(String username, String password) {
    System.out.println("handle loggedInUser authentication");
    return (Observable<AVUser>)AVUser.logIn(username, password);
  }

  public void logout() {
    // TODO: revoke authentication
  }
}
