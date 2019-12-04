package cn.leancloud.sample.funnytimeline.ui.main;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import cn.leancloud.AVParcelableObject;
import cn.leancloud.AVStatus;
import cn.leancloud.AVUser;
import cn.leancloud.sample.funnytimeline.R;
import cn.leancloud.sample.funnytimeline.data.StatusDataSource;
import cn.leancloud.sample.funnytimeline.ui.UserActivity;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class ContentListAdapter extends RecyclerView.Adapter<ContentListAdapter.ViewHolder> {
  public static class ViewHolder extends RecyclerView.ViewHolder {
    private TextView authorView;
    private TextView publishTimeView;
    private Button deleteButton;
    private TextView contentView;

    public ViewHolder(View v) {
      super(v);
      this.authorView = v.findViewById(R.id.author_name);
      this.publishTimeView = v.findViewById(R.id.publish_time);
      this.deleteButton = v.findViewById(R.id.delete_button);
      this.contentView = v.findViewById(R.id.content);
    }

    public void bindData(AVStatus status) {
      if (null != status) {
        this.authorView.setText(status.getSource().getUsername());
        this.publishTimeView.setText(status.getCreatedAtString());
        this.contentView.setText(status.getMessage());
      } else {
        this.authorView.setText("");
        this.publishTimeView.setText("");
        this.contentView.setText("");
      }
      this.authorView.setOnClickListener(null);
    }

    public void responseAuthorClick(View.OnClickListener listener) {
      this.authorView.setOnClickListener(listener);
    }

    public void hideActionButton(boolean hide) {
      this.deleteButton.setVisibility(hide ? View.INVISIBLE : View.VISIBLE);
    }
  }

  private LayoutInflater layoutInflater;
  private StatusDataSource dataSource;
  private List<AVStatus> statusList = new ArrayList<>();

  public ContentListAdapter(LayoutInflater inflater, ContentListFragment.StatusType type, AVUser targetUser) {
    this.layoutInflater = inflater;
    if (ContentListFragment.StatusType.Timeline == type) {
      dataSource = new StatusDataSource(AVUser.currentUser(), StatusDataSource.DataType.TIMELINE);
    } else if (ContentListFragment.StatusType.PrivateMessage == type){
      dataSource = new StatusDataSource(AVUser.currentUser(), StatusDataSource.DataType.PRIVATE);
    } else {
      dataSource = new StatusDataSource(targetUser, StatusDataSource.DataType.OWNED);
    }
    dataSource.loadMore().subscribe(new Observer<List<AVStatus>>() {
      @Override
      public void onSubscribe(Disposable d) {

      }

      @Override
      public void onNext(List<AVStatus> avStatuses) {
        statusList.addAll(avStatuses);
        notifyDataSetChanged();
      }

      @Override
      public void onError(Throwable e) {

      }

      @Override
      public void onComplete() {

      }
    });
  }

  // Create new views (invoked by the layout manager)
  @Override
  public ContentListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View rootView = layoutInflater.inflate(R.layout.singlestatus, parent, false);
    return new ViewHolder(rootView);
  }

  // Replace the contents of a view (invoked by the layout manager)
  @Override
  public void onBindViewHolder(ContentListAdapter.ViewHolder holder, int position) {
    if (position < 0 || position >= statusList.size()) {
      return;
    } else {
      final AVStatus status = statusList.get(position);
      holder.bindData(status);
      holder.responseAuthorClick(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          final AVUser source = status.getSource();
          AVParcelableObject parcelableObject = new AVParcelableObject(source);
          Intent intent = new Intent(layoutInflater.getContext(), UserActivity.class);
          intent.putExtra("user", parcelableObject);
          layoutInflater.getContext().startActivity(intent);
        }
      });
      holder.hideActionButton(this.dataSource.getDataType() == StatusDataSource.DataType.OWNED);
      return;
    }
  }

  // Return the size of your dataset (invoked by the layout manager)
  @Override
  public int getItemCount() {
    return statusList.size();
  }
}
