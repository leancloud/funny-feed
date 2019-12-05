package cn.leancloud.sample.funnytimeline;

import android.app.Application;

import cn.leancloud.AVLogger;
import cn.leancloud.AVOSCloud;

public class FunnyApplication extends Application {
  private final String LC_APP_ID = "wSo2m4G48tMqVBdoQzbWXbLf-gzGzoHsz";

  @Override
  public void onCreate() {
    super.onCreate();

    // 提供 this、App ID、App Key、Server Host 作为参数
    AVOSCloud.initializeSecurely(this, LC_APP_ID, "https://wso2m4g4.lc-cn-n1-shared.com");
    AVOSCloud.setLogLevel(AVLogger.Level.DEBUG);
  }
}
