package my.example.onekeycleaner.adapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import com.example.onekeycleaner.R;

import my.example.onekeycleaner.imgcache.ImageFetcher;
import my.example.onekeycleaner.model.CacheInfo;
import my.example.onekeycleaner.widget.ActionMoreItemView;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class AppCacheClearAdapter extends ListBaseAdapter{

    private ArrayList<CacheInfo> mAppInstallCacheList;
    private HashMap<String, CacheInfo> mSelectedAppCacheList;
    
	public AppCacheClearAdapter(Context context, ListView listView,
			ImageFetcher imageFetcher, OnItemActionListener listener
			,OnItemSelectedListener selectedListener) {
		super(context, listView, imageFetcher, listener);
		// TODO Auto-generated constructor stub
        mOnSelectedListener = selectedListener;
        mAppInstallCacheList = new ArrayList<CacheInfo>();
        mSelectedAppCacheList = new HashMap<String, CacheInfo>();
	}
    public void setInstallCacheList(ArrayList<CacheInfo> appInstalls) {
    	mAppInstallCacheList = appInstalls;
    }
    public HashMap<String, CacheInfo> getSelectedAppList() {
        return mSelectedAppCacheList;
    }

	public void selectAll() {
		for (CacheInfo installcache : mAppInstallCacheList) {
			installcache.setChecked(true);
			mSelectedAppCacheList.put(installcache.packageName, installcache);
		}
	}
    
    public void clearSelection() {
        Collection<CacheInfo> values = mSelectedAppCacheList.values();
        for (CacheInfo installcache : values) {
        	installcache.setChecked(false);
        }
        mSelectedAppCacheList.clear();
    }
    
    @Override
    public int getCount() {
        return mAppInstallCacheList.size();
    }

    @Override
    public Object getItem(int position) {
        return mAppInstallCacheList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View itemView;
        ItemViewHolder holder;
        CacheInfo installcache;

        installcache = mAppInstallCacheList.get(position);

        itemView = convertView;
        Object tag = (itemView != null ? itemView.getTag() : null);

        if (tag != null) {
            holder = (ItemViewHolder) itemView.getTag();
        } else {
            itemView = mLayoutInflater.inflate(R.layout.listview_action_more_item_view, parent, false);
            ((ActionMoreItemView) itemView).inflateMainView(R.layout.listview_install_item);

            holder = new CacheItemViewHolder(itemView);
            itemView.setTag(holder);
        }

        // 刷新Item的各个View数据
        holder.setData(installcache, position);

        // 绑定数据到View
        itemView.setTag(ITEM_VIEW_TAG_KEY, installcache);

        if(position == 0) {
            itemView.setPadding(0, padding, 0, 0);
        } else {
            itemView.setPadding(0, 0, 0, 0);
        }

        return itemView;
	}
	
	
	
	 private class CacheItemViewHolder extends ItemViewHolder {
	        public CheckBox mAppCheckedHolder;
	        public ImageView mAppIconHolder;
	        public TextView mAppNameHolder;
	        public TextView mAppVersionHolder;
	        public TextView mAppSizeHolder;
	        public TextView mAppSizeText;
	        public TextView mAppSystemHolder;
	        public ImageView mIndicatorHolder;
	        public Button mActionButtonHolder;
	        public View mActionMoreHolder;

	        public CacheItemViewHolder(View view) {
	            super(view);

	            mAppCheckedHolder = (CheckBox) view.findViewById(R.id.app_checked);
	            mAppIconHolder = (ImageView) view.findViewById(R.id.app_icon);
	            mAppNameHolder = (TextView) view.findViewById(R.id.app_name);
	            mAppVersionHolder = (TextView) view.findViewById(R.id.app_version);
	            mAppSizeHolder = (TextView) view.findViewById(R.id.app_size);
	            mAppSizeText  =(TextView)view.findViewById(R.id.size_text);
	            mAppSystemHolder = (TextView) view.findViewById(R.id.app_is_system);
	            mIndicatorHolder = (ImageView) view.findViewById(R.id.action_more_indicator);
	            mActionButtonHolder = (Button) view.findViewById(R.id.action_button);
	            mActionMoreHolder = view.findViewById(R.id.action_more_layout);

	            ActionMoreItemView actionMoreItemView = (ActionMoreItemView) mItemView;
	            actionMoreItemView.setOneMoreAction(R.drawable.btn_delete_action);
	        }

	        /**
	         * 由于ItemView会被循环使用，需根据Item数据设置好View的状态
	         * 
	         * @param install
	         */
		@Override
		public void setData(final Object object, final int position) {
			final CacheInfo installcache = (CacheInfo) object;

			mAppCheckedHolder.setEnabled(true);
			mAppCheckedHolder.setOnCheckedChangeListener(null);
			mAppCheckedHolder.setChecked(installcache.isChecked());
			mAppCheckedHolder.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							installcache.setChecked(isChecked);
							if (isChecked) {
								mSelectedAppCacheList.put(installcache.packageName, installcache);
							} else {
								mSelectedAppCacheList.remove(installcache.packageName);
							}
							mOnSelectedListener.onItemSelected(installcache,mSelectedAppCacheList.size());
						}
					});
			mAppSystemHolder.setVisibility(View.GONE);

			if (!TextUtils.isEmpty(installcache.packageName)) {
				mImageFetcher.loadImageFromPackageManger(installcache.mAppKey,
						mAppIconHolder);
			}

			mAppNameHolder.setText(installcache.name);
			mAppVersionHolder.setText(installcache.cacheVersion);
			mAppSizeText.setText(R.string.app_cache_size_text);
			mAppSizeHolder.setText(installcache.cacheSize);
			mAppSizeHolder.setTextSize(TypedValue.COMPLEX_UNIT_DIP,12);
			mAppSizeHolder.setTextColor(Color.rgb(0, 0, 0));
			
            mActionMoreHolder.setVisibility(View.GONE);
            mIndicatorHolder.setVisibility(View.GONE);
	
			mActionButtonHolder.setText(R.string.tab_action_clearner);
            mActionButtonHolder.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int action = ITEM_ACTION_CACHE_CLEAR;
                    mOnActionListener.onItemAction(mItemView, position, action, installcache);
                }
            });

		}
	}

}
