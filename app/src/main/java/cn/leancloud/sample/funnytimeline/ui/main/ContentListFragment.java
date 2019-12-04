package cn.leancloud.sample.funnytimeline.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.leancloud.AVUser;
import cn.leancloud.sample.funnytimeline.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class ContentListFragment extends Fragment {

  public enum StatusType {
    Timeline, OwnedStatus, PrivateMessage
  }
  private RecyclerView recyclerView;
  private RecyclerView.Adapter recyclerAdapter;
  private StatusType type;
  private AVUser targetUser = null;

  public static ContentListFragment newInstance(StatusType type) {
    ContentListFragment fragment = new ContentListFragment();
    fragment.type = type;
    return fragment;
  }

  public void setTargetUser(AVUser targetUser) {
    this.targetUser = targetUser;
  }


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public void onResume() {
    super.onResume();
  }

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.fragment_main, container, false);

    recyclerView = root.findViewById(R.id.list_view);

    // use this setting to improve performance if you know that changes
    // in content do not change the layout size of the RecyclerView
    recyclerView.setHasFixedSize(false);

    // use a linear layout manager
    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(inflater.getContext());
    recyclerView.setLayoutManager(layoutManager);

    recyclerAdapter = new ContentListAdapter(inflater, this.type, targetUser);
    recyclerView.setAdapter(recyclerAdapter);

    return root;
  }
}