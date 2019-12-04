package cn.leancloud.sample.funnytimeline.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import cn.leancloud.sample.funnytimeline.R;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

  @StringRes
  private static final int[] TAB_TITLES = new int[]{
      R.string.tab_text_timeline,
      R.string.tab_text_explorer,
      R.string.tab_text_message
  };
  private final Context mContext;

  public SectionsPagerAdapter(Context context, FragmentManager fm) {
    super(fm);
    mContext = context;
  }

  @Override
  public Fragment getItem(int position) {
    // getItem is called to instantiate the fragment for the given page.
    // Return a ContentListFragment (defined as a static inner class below).
    Fragment result;
    switch (position) {
      case 0:
        result = ContentListFragment.newInstance(ContentListFragment.StatusType.Timeline);
        break;
      case 1:
        result = UserListFragment.newInstance();
        break;
      default:
        result = ContentListFragment.newInstance(ContentListFragment.StatusType.PrivateMessage);
        break;
    }
    return result;
  }

  @Nullable
  @Override
  public CharSequence getPageTitle(int position) {
    return mContext.getResources().getString(TAB_TITLES[position]);
  }

  @Override
  public int getCount() {
    // Show 2 total pages.
    return 3;
  }
}