package cn.leancloud.demo.todo;

import cn.leancloud.AVQuery;
import cn.leancloud.AVUser;
import cn.leancloud.EngineFunction;
import cn.leancloud.EngineFunctionParam;

import java.util.List;

public class Cloud {

  @EngineFunction("hello")
  public static String hello(@EngineFunctionParam("name") String name) {
    if (name == null) {
      return "What is your name?";
    }
    return String.format("Hello %s!", name);
  }

  @EngineFunction("getFeaturedUsers")
  public static List<AVUser> featuredUsers() {
    AVQuery<AVUser> userQuery = AVUser.getQuery();
    userQuery.addDescendingOrder("official");
    return userQuery.find();
  }
}
