package cn.leancloud.sample.funnytimeline.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.leancloud.AVException;
import cn.leancloud.AVUser;
import cn.leancloud.callback.FollowersAndFolloweesCallback;
import cn.leancloud.sample.funnytimeline.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class UserListFragment extends Fragment {

  private RecyclerView recyclerView;
  private RecyclerView.Adapter recyclerAdapter;

  public static UserListFragment newInstance() {
    UserListFragment fragment = new UserListFragment();
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public void onResume() {
    super.onResume();
    ((UserListAdapter)recyclerAdapter).syncData();
  }

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.fragment_main, container, false);
    recyclerView = root.findViewById(R.id.list_view);

    // use this setting to improve performance if you know that changes
    // in content do not change the layout size of the RecyclerView
    recyclerView.setHasFixedSize(true);

    // use a linear layout manager
    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(inflater.getContext());
    recyclerView.setLayoutManager(layoutManager);

    recyclerAdapter = new UserListAdapter(inflater);
    recyclerView.setAdapter(recyclerAdapter);

    return root;
  }
}