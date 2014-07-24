package com.joy.Activity;

import gejw.android.quickandroid.QActivity;
import gejw.android.quickandroid.widget.Toast;

import java.util.List;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.joy.R;
import com.joy.Utils.Constants;
import com.joy.Widget.PostAdapter;
import com.joy.json.JsonCommon;
import com.joy.json.JsonCommon.OnOperationListener;
import com.joy.json.model.PostDetailEntity;
import com.joy.json.model.PostEntity;
import com.joy.json.operation.OperationBuilder;
import com.joy.json.operation.impl.PostOp;
import com.umeng.analytics.MobclickAgent;

/**
 * 公告列表
 * @author daiye
 *
 */
public class PostActivity extends QActivity implements OnClickListener {
	
	private RelativeLayout layout_title;
	private TextView tv_ret;
	private TextView tv_title;
	private ListView list_post;
	private PostAdapter adapter;
	
	private LinearLayout layout_menu;
	private LinearLayout layout_useful;
	private TextView tv_useful;
	private LinearLayout layout_expired;
	private TextView tv_expired;
	private Resources resources;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post);
		resources = this.getResources();
		initView();
		initData("1");
	}

	private void initView() {
		layout_title = (RelativeLayout) findViewById(R.id.layout_title);
		uiAdapter.setMargin(layout_title, LayoutParams.MATCH_PARENT, Constants.TitleHeight, 0, 0,
				0, 0);

		tv_ret = (TextView) findViewById(R.id.tv_ret);
		tv_ret.setOnClickListener(this);
		uiAdapter.setTextSize(tv_ret, Constants.TitleRetSize);
		uiAdapter.setMargin(tv_ret, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, 20, 0, 0, 0);

		tv_title = (TextView) findViewById(R.id.tv_title);
		uiAdapter.setTextSize(tv_title, Constants.TitleSize);
		
		layout_menu = (LinearLayout) findViewById(R.id.layout_menu);
		
		layout_useful = (LinearLayout) findViewById(R.id.layout_useful);
		layout_useful.setOnClickListener(this);
		
		tv_useful = (TextView) findViewById(R.id.tv_useful);
		
		layout_expired = (LinearLayout) findViewById(R.id.layout_expired);
		layout_expired.setOnClickListener(this);
		
		tv_expired = (TextView) findViewById(R.id.tv_expired);
		list_post = (ListView) findViewById(R.id.list_post);
		uiAdapter.setMargin(list_post, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT, 0, 0, 0, 0);
		adapter = new PostAdapter(self);
		list_post.setAdapter(adapter);	
	}

	private void initData(String isexpired) {
		PostEntity post = new PostEntity();
		post.isexpired = isexpired;
		OperationBuilder builder = new OperationBuilder().append(new PostOp(),
				post);
		OnOperationListener listener = new OnOperationListener() {
			@Override
			public void onOperationFinished(List<Object> resList) {
				if (self.isFinishing()) {
					return;
				}
				if (resList == null) {
					Toast.show(self, "连接超时");
					return;
				}
				PostEntity entity = (PostEntity) resList.get(0);
				List<PostDetailEntity> postlist = entity.getRetobj();
				if (postlist == null) {
					Toast.show(self, "没有公告信息！");
					finish();
					return;
				}
				for (PostDetailEntity entity1 : postlist) {
					adapter.addItem(entity1);
				}
				adapter.notifyDataSetChanged();
			}

			@Override
			public void onOperationError(Exception e) {
				e.printStackTrace();
			}
		};

		JsonCommon task = new JsonCommon(self, builder, listener,
				JsonCommon.PROGRESSQUERY);
		task.execute();
	}

	private void showMenu(int layout) {
		switch (layout) {
		case R.id.layout_useful:
			tv_useful.setTextColor(resources.getColor(R.color.title_bg));
			tv_expired.setTextColor(resources.getColor(R.color.BLACK));
			adapter.removeAll();
			initData("1");
			break;
		case R.id.layout_expired:
			tv_expired.setTextColor(resources.getColor(R.color.title_bg));
			tv_useful.setTextColor(resources.getColor(R.color.BLACK));
			adapter.removeAll();
			initData("2");
			break;
		default:
			break;
		}
	}	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_useful:
		case R.id.layout_expired:
			showMenu(v.getId());
			break;
		case R.id.tv_ret:
			finish();
			break;
		default:
			break;
		}
	}
	
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}
