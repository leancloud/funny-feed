package cn.leancloud.demo.todo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.leancloud.*;
import cn.leancloud.utils.StringUtil;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

@WebServlet(name = "AppServlet", urlPatterns = { "/status" })
public class StatusServlet extends HttpServlet {

  private static final long serialVersionUID = -225836733891271748L;

  private void listStatuses(final HttpServletRequest req, final HttpServletResponse resp,
                            int offset, final List<String> usernameList) throws ServletException, IOException {
    AVQuery<AVStatus> query = AVObject.getQuery(AVStatus.class);
    query.orderByDescending("createdAt");
    query.skip(offset);

    query.findInBackground().subscribe(new Observer<List<AVStatus>>() {
      @Override
      public void onSubscribe(Disposable disposable) {

      }

      @Override
      public void onNext(List<AVStatus> statuses) {
        System.out.println("got statuses result. size: " + statuses.size());
        req.setAttribute("users", usernameList);
        req.setAttribute("statuses", statuses);
        try {
          req.getRequestDispatcher("/status.jsp").forward(req, resp);
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }

      @Override
      public void onError(Throwable throwable) {
        System.out.println("failed to query Status class. cause: " + throwable.getMessage());

        if (throwable instanceof AVException) {
          if (((AVException) throwable).getCode() == 101) {
            // 该错误的信息为：{ code: 101, message: 'Class or object doesn\'t exists.' }，说明 Todo
            // 数据表还未创建，所以返回空的
            // Todo 列表。
            // 具体的错误代码详见：https://leancloud.cn/docs/error_code.html
            req.setAttribute("statuses", new ArrayList<>());
          }
          req.setAttribute("users", usernameList);
          req.setAttribute("statuses", new ArrayList<>());
          try {
            req.getRequestDispatcher("/status.jsp").forward(req, resp);
          } catch (Exception ex) {
            ex.printStackTrace();
          }
//          throw new RuntimeException(throwable);
        }
      }

      @Override
      public void onComplete() {

      }
    });
  }
  @Override
  protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
    String offsetParam = req.getParameter("offset");
    final int offset;
    if (!StringUtil.isEmpty(offsetParam)) {
      offset = Integer.parseInt(offsetParam);
    } else {
      offset = 0;
    }

    AVQuery<AVUser> userQuery = AVUser.getQuery();
    userQuery.whereEqualTo("official", true);
    userQuery.findInBackground().subscribe(new Observer<List<AVUser>>() {
      @Override
      public void onSubscribe(Disposable disposable) {

      }

      @Override
      public void onNext(List<AVUser> avUsers) {
        List<String> usernameList = new ArrayList<>();
        for (AVUser usr : avUsers) {
          usernameList.add(usr.getUsername());
        }
        try {
          listStatuses(req, resp, offset, usernameList);
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }

      @Override
      public void onError(Throwable throwable) {
        System.out.println("failed to query user. cause: " + throwable.getMessage());
        try {
          listStatuses(req, resp, offset, new ArrayList<>());
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }

      @Override
      public void onComplete() {

      }
    });


  }

  @Override
  protected void doPost(HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
    final String content = req.getParameter("content");
    final String username = req.getParameter("user");

    AVUser.logIn(username, "123456").subscribe(new Observer<AVUser>() {
      @Override
      public void onSubscribe(Disposable disposable) {

      }

      @Override
      public void onNext(AVUser avUser) {
        System.out.println("current user:" + avUser);
        AVStatus status = AVStatus.createStatus("", content);
        status.sendToFollowersInBackground().subscribe(new Observer<AVStatus>() {
          @Override
          public void onSubscribe(Disposable disposable) {

          }

          @Override
          public void onNext(AVStatus avStatus) {
            System.out.println("publish status:" + avStatus);
            try {
              resp.sendRedirect("/status");
            } catch (IOException ex) {
              ex.printStackTrace();
            }
          }

          @Override
          public void onError(Throwable throwable) {
            System.out.println("failed to publish status: " + content + ", cause: " + throwable.getMessage());
            try {
              resp.sendRedirect("/status");
            } catch (IOException ex) {
              ex.printStackTrace();
            }
          }

          @Override
          public void onComplete() {

          }
        });
      }

      @Override
      public void onError(Throwable throwable) {
        System.out.println("failed to login with username: " + username + ", cause: " + throwable.getMessage());
        try {
          resp.sendRedirect("/status");
        } catch (IOException ex) {
          ex.printStackTrace();
        }
      }

      @Override
      public void onComplete() {

      }
    });
  }
}
