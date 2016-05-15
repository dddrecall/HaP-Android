package com.HaP.View;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.graphics.Bitmap.*;
import android.graphics.PorterDuff.*;
import android.graphics.drawable.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import com.HaP.Byml.*;

public class ExpandeAdapter extends BaseExpandableListAdapter
{

  private Context mContext;
  private LayoutInflater mInflater = null;
  //private String[] mGroupStrings = {"第一","第二","第三","4"};
  private List<List<Item>> mData = null;

  public ExpandeAdapter(Context ctx, List<List<Item>> list)
  {
		mContext = ctx;
		mInflater = (LayoutInflater) mContext
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//mGroupStrings = mContext.getResources().getStringArray(R.array.groups);
		mData = list;
  }

  public void setData(List<List<Item>> list)
  {
		mData = list;
  }

  @Override
  public int getGroupCount()
  {
		// TODO Auto-generated method stub
		return mData.size();
  }

  @Override
  public int getChildrenCount(int groupPosition)
  {
		// TODO Auto-generated method stub
		return mData.get(groupPosition).size();
  }

  @Override
  public List<Item> getGroup(int groupPosition)
  {
		// TODO Auto-generated method stub
		return mData.get(groupPosition);
  }

  @Override
  public Item getChild(int groupPosition, int childPosition)
  {
		// TODO Auto-generated method stub
		return mData.get(groupPosition).get(childPosition);
  }

  @Override
  public long getGroupId(int groupPosition)
  {
		// TODO Auto-generated method stub
		return groupPosition;
  }

  @Override
  public long getChildId(int groupPosition, int childPosition)
  {
		// TODO Auto-generated method stub
		return childPosition;
  }

  @Override
  public boolean hasStableIds()
  {
		// TODO Auto-generated method stub
		return false;
  }

  @Override
  public View getGroupView(int groupPosition, boolean isExpanded,
													 View convertView, ViewGroup parent)
  {
		// TODO Auto-generated method stub
		if (convertView == null)
		{
			convertView = mInflater.inflate(R.layout.group_item_layout, null);
		}
		GroupViewHolder holder = new GroupViewHolder();
		holder.mGroupName = (TextView) convertView
			.findViewById(R.id.group_name);
		holder.mGroupName.setText(mData.get(groupPosition).get(0).getTitel());
		holder.mGroupCount = (TextView) convertView
			.findViewById(R.id.group_count);
//holder.mGroupCount.setText("[" + mData.get(groupPosition).size() + "]");
		return convertView;
  }

  @Override
  public View getChildView(int groupPosition, int childPosition,
													 boolean isLastChild, View convertView, ViewGroup parent)
  {
		// TODO Auto-generated method stub
		if (convertView == null)
		{
			convertView = mInflater.inflate(R.layout.child_item_layout, null);
		}
		ChildViewHolder holder = new ChildViewHolder();
		holder.mIcon = (ImageView) convertView.findViewById(R.id.img);
		//holder.mIcon.setBackgroundDrawable(getRoundCornerDrawable(
		//								 getChild(groupPosition, childPosition).getImageId(), 0));
		holder.mIcon.setBackgroundResource(getChild(groupPosition, childPosition).getImageId());
		holder.mChildName = (TextView) convertView.findViewById(R.id.item_name);
		holder.mChildName.setText(getChild(groupPosition, childPosition)
															.getName());
		holder.mDetail = (TextView) convertView.findViewById(R.id.item_detail);
		holder.mDetail.setText(getChild(groupPosition, childPosition)
													 .getDetail());
		return convertView;
  }
  boolean isclick=false;
  @Override
  public boolean isChildSelectable(int groupPosition, int childPosition)
  {
		// TODO Auto-generated method stub
		/* 很重要：实现ChildView点击事件，必须返回true */
		return true;
  }


  private Drawable getRoundCornerDrawable(int resId, float roundPX //圆角的半径
																					)
  {
		Drawable drawable = mContext.getResources().getDrawable(resId);
		//int w = mContext.getResources().getDimensionPixelSize(R.dimen.image_width);
		int w=32;
		int h = w;

		Bitmap bitmap = Bitmap
			.createBitmap(w, h,
										drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
										: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, w, h);
		drawable.draw(canvas);

		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Bitmap retBmp = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas can = new Canvas(retBmp);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, width, height);
		final RectF rectF = new RectF(rect);

		paint.setColor(color);
		paint.setAntiAlias(true);
		can.drawARGB(0, 0, 0, 0);
		can.drawRoundRect(rectF, roundPX, roundPX, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		can.drawBitmap(bitmap, rect, rect, paint);
		return new BitmapDrawable(retBmp);
  }

  private class GroupViewHolder
  {
		TextView mGroupName;
		TextView mGroupCount;
  }

  private class ChildViewHolder
  {
		ImageView mIcon;
		TextView mChildName;
		TextView mDetail;
  }

}

