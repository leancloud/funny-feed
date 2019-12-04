package cn.leancloud.sample.funnytimeline.data;

import java.util.List;

import cn.leancloud.AVStatus;
import cn.leancloud.AVStatusQuery;
import cn.leancloud.AVUser;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class StatusDataSource {
  public enum DataType {
    TIMELINE(0), OWNED(1), PRIVATE(2);
    DataType(int type) {
      this.type = type;
    }
    private int type;
    public int value() {
      return this.type;
    }
  }
  private DataType type;
  private int pageSize = 20;
  private AVStatusQuery query;
  private boolean firstQuery = true;

  public DataType getDataType() {
    return this.type;
  }

  public StatusDataSource(AVUser user, DataType type) {
    this.type = type;
    if (DataType.TIMELINE == this.type) {
      this.query = AVStatus.inboxQuery(user, AVStatus.INBOX_TYPE.TIMELINE.toString());
    } else if (DataType.OWNED == this.type) {
      try {
        this.query = AVStatus.statusQuery(user);
        this.query.include("source");
      } catch (Exception ex) {
        ex.printStackTrace();
        this.query = null;
      }
    } else {
      this.query = AVStatus.inboxQuery(user, AVStatus.INBOX_TYPE.PRIVATE.toString());
    }
    this.query.setPageSize(this.pageSize);
  }

  public Observable<List<AVStatus>> loadMore() {
    Observable<List<AVStatus>> result;
    if (firstQuery) {
      result = this.query.findInBackground().map(new Function<List<AVStatus>, List<AVStatus>>() {
        @Override
        public List<AVStatus> apply(List<AVStatus> avStatuses) throws Exception {
          firstQuery = false;
          return avStatuses;
        }
      });
    } else {
      result = this.query.nextInBackground();
    }
    return result;
  }
}
