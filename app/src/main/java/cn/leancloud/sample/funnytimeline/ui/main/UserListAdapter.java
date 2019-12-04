package cn.leancloud.sample.funnytimeline.ui.main;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.RecyclerView;
import cn.leancloud.AVCloud;
import cn.leancloud.AVException;
import cn.leancloud.AVOSCloud;
import cn.leancloud.AVObject;
import cn.leancloud.AVParcelableObject;
import cn.leancloud.AVQuery;
import cn.leancloud.AVStatus;
import cn.leancloud.AVUser;
import cn.leancloud.callback.FollowersAndFolloweesCallback;
import cn.leancloud.sample.funnytimeline.R;
import cn.leancloud.sample.funnytimeline.data.StatusDataSource;
import cn.leancloud.sample.funnytimeline.ui.UserActivity;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {
  private static int ACTION_FOLLOW = 0;
  private static int ACTION_UNFOLLOW = 1;

  public interface FollowActionCallback{
    void done(int action, AVUser targetUser, Throwable ex);
  }
  public static class ViewHolder extends RecyclerView.ViewHolder {
    private TextView usernameView;
    private TextView descriptionView;
    private Button followButton;

    public ViewHolder(View v) {
      super(v);
      this.usernameView = v.findViewById(R.id.username);
      this.descriptionView = v.findViewById(R.id.user_description);
      this.followButton = v.findViewById(R.id.followButton);
    }

    public void responseUserClick(View.OnClickListener listener) {
      this.usernameView.setOnClickListener(listener);
    }

    public void bindData(final AVUser user, boolean isFollowee, final FollowActionCallback callback) {
      if (null != user) {
        this.usernameView.setText(user.getUsername());
        this.descriptionView.setText(user.getString("description"));
        if (isFollowee) {
          this.followButton.setText(R.string.action_unfollow);
          this.followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              AVUser.currentUser().unfollowInBackground(user.getObjectId()).subscribe(new Observer<JSONObject>() {
                @Override
                public void onSubscribe(Disposable d) {
                }

                @Override
                public void onNext(JSONObject jsonObject) {
                  System.out.println("succeed to follow user " + user.getUsername());
                  if (null != callback) {
                    callback.done(ACTION_UNFOLLOW, user, null);
                  }
                }

                @Override
                public void onError(Throwable e) {
                  System.out.println("failed to follow user, cause: " + e.getMessage());
                  if (null != callback) {
                    callback.done(ACTION_UNFOLLOW, user, e);
                  }
                }

                @Override
                public void onComplete() {

                }
              });
            }
          });
        } else {
          this.followButton.setText(R.string.action_follow);
          this.followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              AVUser.currentUser().followInBackground(user.getObjectId()).subscribe(new Observer<JSONObject>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(JSONObject jsonObject) {
                  System.out.println("succeed to follow user " + user.getUsername());
                  if (null != callback) {
                    callback.done(ACTION_FOLLOW, user, null);
                  }
                }

                @Override
                public void onError(Throwable e) {
                  System.out.println("failed to follow user, cause: " + e.getMessage());
                  if (null != callback) {
                    callback.done(ACTION_FOLLOW, user, e);
                  }
                }

                @Override
                public void onComplete() {

                }
              });
            }
          });
        }
      }
    }
  }

  private LayoutInflater layoutInflater;
  private List<AVUser> userList = new ArrayList<>();
  private List<AVUser> followers = new ArrayList<>();
  private List<AVUser> followees = new ArrayList<>();

  public UserListAdapter(LayoutInflater inflater) {
    this.layoutInflater = inflater;
  }

  public void syncData() {
    if (null == AVUser.currentUser()) {
      return;
    }
    AVUser.currentUser().getFollowersAndFolloweesInBackground(new FollowersAndFolloweesCallback() {
      @Override
      public void done(Map avObjects, AVException avException) {
        if (null == avObjects || null != avException) {
          return;
        }
        try {
          followers.clear();
          followees.clear();

          List<AVUser> followerArray = (List<AVUser>)avObjects.get("follower");
          List<AVUser> followeeArray = (List<AVUser>)avObjects.get("followee");
          if (null != followerArray) {
            followers.addAll(followerArray);
          }
          if (null != followeeArray) {
            followees.addAll(followeeArray);
          }
          System.out.println("followers=" + followers);
          System.out.println("followees=" + followees);
        } catch (Exception ex) {
          ex.printStackTrace();
        }
        getFeaturedUsers();
      }

      @Override
      protected void internalDone0(Object o, AVException avException) {

      }
    });
  }

  private void getFeaturedUsers() {
    Observable<List<Map<String, Object>>> users =
        AVCloud.callFunctionInBackground("getFeaturedUsers", new HashMap<String, Object>());
    users.subscribe(new Observer<List<Map<String, Object>>>() {
      @Override
      public void onSubscribe(Disposable d) {

      }

      @Override
      public void onNext(List<Map<String, Object>> results) {
        if (null == results) {
          return;
        }
        try {
          if (null != results && results.size() > 0) {
            Map<String, Object> tmp;
            for (int i = 0; i < results.size(); i++) {
              tmp = results.get(i);
              AVUser tmpUser = new AVUser();
              tmpUser.resetServerData(tmp);
              userList.add(tmpUser);
            }
          }
        } catch (Exception ex) {
          ex.printStackTrace();
        }
        notifyDataSetChanged();
      }

      @Override
      public void onError(Throwable e) {
        System.out.println("failed to query user. cause: " + e.getMessage());
      }

      @Override
      public void onComplete() {

      }
    });
  }

  // Create new views (invoked by the layout manager)
  @Override
  public UserListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View rootView = layoutInflater.inflate(R.layout.singleuser, parent, false);
    return new ViewHolder(rootView);
  }

  private boolean exist(List<AVUser> userList, AVUser target) {
    if (null == userList || userList.size() < 1) {
      return false;
    }
    if (null == target) {
      return false;
    }
    for (AVUser user: userList) {
      if (user.getObjectId().equals(target.getObjectId())) {
        return true;
      }
    }
    return false;
  }

  // Replace the contents of a view (invoked by the layout manager)
  @Override
  public void onBindViewHolder(final UserListAdapter.ViewHolder holder, int position) {
    if (position < 0 || position >= userList.size()) {
      return;
    } else {
      final AVUser user = userList.get(position);
      boolean isFollowee = exist(this.followees, user);
      holder.bindData(user, isFollowee, new FollowActionCallback() {
        @Override
        public void done(int action, AVUser targetUser, Throwable ex) {
          String message;
          if (null == ex) {
            if (ACTION_FOLLOW == action) {
              message = "Succeed to follow user:" + targetUser.getUsername();
              followees.add(targetUser);
            } else {
              message = "Succeed to unfollow user:" + targetUser.getUsername();
              int targetIndex = -1;
              for (int i = 0; i < followees.size(); i++) {
                AVUser tmp = followees.get(i);
                if (tmp.getObjectId().equals(targetUser.getObjectId())) {
                  targetIndex = i;
                  break;
                }
              }
              if (-1 < targetIndex) {
                followees.remove(targetIndex);
              } else {
                System.out.println("not found followee with objectId: " + targetUser.getObjectId());
              }
            }
            notifyDataSetChanged();
          } else {
            if (ACTION_FOLLOW == action) {
              message = "failed to follow user:" + targetUser.getUsername() + ", cause: " + ex.getMessage();
            } else {
              message = "failed to unfollow user:" + targetUser.getUsername() + ", cause: " + ex.getMessage();
            }
          }
          Snackbar.make(holder.itemView, message, Snackbar.LENGTH_SHORT)
              .setAction("Action", null).show();
        }
      });
      holder.responseUserClick(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          AVParcelableObject parcelableObject = new AVParcelableObject(user);
          Intent intent = new Intent(layoutInflater.getContext(), UserActivity.class);
          intent.putExtra("user", parcelableObject);
          layoutInflater.getContext().startActivity(intent);
        }
      });
      return;
    }
  }

  // Return the size of your dataset (invoked by the layout manager)
  @Override
  public int getItemCount() {
    return userList.size();
  }
}
