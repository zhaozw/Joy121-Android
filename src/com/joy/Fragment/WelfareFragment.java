package com.joy.Fragment;

import gejw.android.quickandroid.QFragment;
import gejw.android.quickandroid.widget.Toast;

import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.joy.R;
import com.joy.Utils.Constants;
import com.joy.Widget.WelfareAdapter;
import com.joy.json.JsonCommon;
import com.joy.json.JsonCommon.OnOperationListener;
import com.joy.json.model.CommoditySet;
import com.joy.json.model.WelfareEntity;
import com.joy.json.operation.OperationBuilder;
import com.joy.json.operation.impl.Fp_benefitOp;

/**
 * 公司福利
 * 
 * @author daiye
 * 
 */
public class WelfareFragment extends QFragment {

	private RelativeLayout layout_title;
	private TextView tv_title;
	private ListView mListView;
	private WelfareAdapter mAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_welfare, container, false);
		initView(v);
		
		getWelfareList();
		return v;
	}

	private void initView(View v) {
		layout_title = (RelativeLayout) v.findViewById(R.id.layout_title);
		uiAdapter.setMargin(layout_title, LayoutParams.MATCH_PARENT, 74, 0, 0, 0, 0);
		
		tv_title = (TextView) v.findViewById(R.id.tv_title);
		uiAdapter.setTextSize(tv_title, Constants.TitleSize);
		
		mListView = (ListView) v.findViewById(R.id.list_welfare);
		mAdapter = new WelfareAdapter(mActivity);
		
		mListView.setAdapter(mAdapter);

		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
			}
		});
	}
	
	/**
	 * 获取接口数据
	 */
	private void getWelfareList() {
//		String loginname = JoyApplication.getInstance().getUserinfo().getLoginName();
		String loginname = "steven";
		
		OperationBuilder builder = new OperationBuilder().append(
				new Fp_benefitOp(), loginname);
		OnOperationListener listener = new OnOperationListener() {
			@Override
			public void onOperationFinished(List<Object> resList) {
				if (mActivity.isFinishing()) {
					return;
				}
				if (resList == null) {
					Toast.show(mActivity, "连接超时");
					return;
				}
				CommoditySet commoditySet = (CommoditySet) resList.get(0);
				if (commoditySet == null) {
					Toast.show(mActivity, "无法取得数据！");
					return;
				} else {
					
				}
			}

			@Override
			public void onOperationError(Exception e) {
				e.printStackTrace();
			}
		};

		JsonCommon task = new JsonCommon(mActivity, builder, listener,
				JsonCommon.PROGRESSQUERY);
		task.execute();
	}
}
