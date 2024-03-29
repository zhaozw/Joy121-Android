package com.joy.Widget;

import gejw.android.quickandroid.ui.adapter.UIAdapter;
import gejw.android.quickandroid.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.joy.JoyApplication;
import com.joy.R;
import com.joy.Activity.SurveyActivity;
import com.joy.Utils.SharedPreferencesUtils;
import com.joy.json.JsonCommon;
import com.joy.json.JsonCommon.OnOperationListener;
import com.joy.json.model.CompAppSet;
import com.joy.json.model.SurveyAEntity;
import com.joy.json.model.SurveyDetailEntity;
import com.joy.json.model.SurveyDetailEntity.SurveyAns;
import com.joy.json.model.SurveyDetailEntity.SurveyRate;
import com.joy.json.operation.OperationBuilder;
import com.joy.json.operation.impl.SurveyAOp;

public class SurveyAdapter extends BaseAdapter {

	/**
	 * 上下文对象
	 */
	private Context mContext = null;
	private Activity mActivity = null;
	private ArrayList<SurveyDetailEntity> data = new ArrayList<SurveyDetailEntity>();
	private UIAdapter uiAdapter;
	private String type;
	CompAppSet appSet;
	int color;
	Handler mHandler;

	/**
	 * @param mainActivity
	 */
	public SurveyAdapter(Activity activity, Context ctx) {
		mActivity = activity;
		mContext = ctx;
		uiAdapter = UIAdapter.getInstance(ctx);
		color = Color.parseColor("#ffa800");
		appSet = JoyApplication.getInstance().getCompAppSet();
		mHandler = new Handler();
	}
	
	public void setType(String type){
		this.type = type;
	}

	public void addItem(SurveyDetailEntity entity) {
		data.add(entity);
	}

	public void addSeparatorItem(SurveyDetailEntity entity) {
		data.add(entity);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void removeAll()
	{
		data.clear();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final SurveyDetailEntity entity = data.get(position);

		final ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.survey_list_item, parent, false);
			holder = new ViewHolder();

			holder.iv_survey = (ImageView) convertView
					.findViewById(R.id.iv_survey);
			uiAdapter.setMargin(holder.iv_survey, 60,
					uiAdapter.CalcHeight(60, 133, 94), 0, 0, 0, 0);

			holder.tv_surveyname = (TextView) convertView
					.findViewById(R.id.tv_surveyname);
			uiAdapter.setTextSize(holder.tv_surveyname, 20);
			uiAdapter.setMargin(holder.tv_surveyname,
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 0, 0,
					0, 0);

			holder.tv_expiretime = (TextView) convertView
					.findViewById(R.id.tv_expiretime);
			uiAdapter.setTextSize(holder.tv_expiretime, 20);
			uiAdapter.setMargin(holder.tv_expiretime,
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 20,
					5, 0, 5);

			holder.layout_multichoice = (LinearLayout) convertView
					.findViewById(R.id.layout_multichoice);
			uiAdapter.setMargin(holder.layout_multichoice,
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 50,
					10, 0, 10);

			holder.btn_survey = (Button) convertView
					.findViewById(R.id.btn_survey);
			uiAdapter.setTextSize(holder.btn_survey, 20);
			uiAdapter.setMargin(holder.btn_survey, 120, 35, 0, 0, 10, 5);

			convertView.setTag(holder);
		} else {// 有直接获得ViewHolder
			holder = (ViewHolder) convertView.getTag();
		}
		
		
		String optionStr="";
		final int optionType = entity.getOptionType();
		String max = entity.getOptionMax();
		String min = entity.getOptionMin();
		if(0 == optionType){
			optionStr = "  单选";
		}else{
			optionStr = "  最少选"+min+"项"+" 最多选"+max+"项";
		}

		if("2".equals(type)){
			holder.iv_survey.setImageResource(R.drawable.survey_pass);
		}else{
			holder.iv_survey.setImageResource(R.drawable.survey);
		}
		holder.tv_surveyname.setText(entity.getTitle());
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		holder.tv_expiretime.setText("截止日期："
				+ sdf.format(new Date(Long.parseLong(entity.getExpireTime()
						.substring(6, 19))))+ optionStr);
		
		String[] questionlist = entity.getQuestions().split("\\^");
		// 清空checkbox
		holder.layout_multichoice.removeAllViews();
		SurveyAns surveyAns = entity.getSurveyAnswer();
		String[] answer = null;
		if (surveyAns == null) {
			if (entity.getAnswer() == null) {
				answer = new String[questionlist.length];
				for (int i = 0; i < answer.length; i++) {
					answer[i] = "0";
				}
				entity.setAnswer(answer);
			} else {
				answer = entity.getAnswer();
			}
		} else {
			answer = surveyAns.getAnswers().split("\\^");
		}

		List<SurveyRate> surveyratelist = entity.getSurveyRates();
		for (int j = 0; j < questionlist.length; j++) {
			final int k = j;
			int l = -1;
			String question = questionlist[j];
			CheckBox checkbox = new CheckBox(mContext);
			if(optionType == 0){
				checkbox.setButtonDrawable(R.drawable.checkbox_single_style);
			}else{
				checkbox.setButtonDrawable(R.drawable.checkbox_multiple_style);
			}
			if (surveyratelist == null || entity.getSurveyAnswer() == null) {
				checkbox.setText(question);
			} else {
				for (SurveyRate surveyrate : surveyratelist) {
					int index = surveyrate.getQuestionIndex();
					if (index == j) {
						l = j;
						break;
					}
				}
				if (l == -1 || l >= surveyratelist.size()) {
					checkbox.setText(question);
				} else {
					checkbox.setText(Html.fromHtml(question
							+ "<font color=\"#ff781f\">("
							+ surveyratelist.get(l).getRate() + "票)</font>"));
				}
			}

			checkbox.setChecked(answer[j].equals("0") ? false : true);
			if("2".equals(type)){
				checkbox.setClickable(false);
				checkbox.setOnCheckedChangeListener(null);
			}else{
				if (entity.getSurveyAnswer() == null) {
					checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
							if(optionType == 0){
								//单选
								int childCount = holder.layout_multichoice.getChildCount();
								for(int x = 0;x<childCount;x++){
									CheckBox ck = (CheckBox) holder.layout_multichoice.getChildAt(x);
									ck.setChecked(false);
								}
								if(isChecked){
									CheckBox curCk = (CheckBox) holder.layout_multichoice.getChildAt(k);
									curCk.setChecked(true);
								}
								String[] answer = entity.getAnswer();
								answer[k] = (isChecked ? "1" : "0");
								entity.setAnswer(answer);
							}else{
								//多选
								String[] answer = entity.getAnswer();
								answer[k] = (isChecked ? "1" : "0");
								entity.setAnswer(answer);
							}
						}
					});
				} else {
					checkbox.setClickable(false);
					checkbox.setOnCheckedChangeListener(null);
				}
			}
			holder.layout_multichoice.addView(checkbox);
		}
		
		if("2".equals(type)){
			//过期
			holder.btn_survey.setClickable(false);
			holder.btn_survey.setBackgroundColor(mActivity.getResources()
					.getColor(R.color.btn_disable));
			if (entity.getSurveyAnswer() == null) {
				holder.btn_survey.setText("投票");
			}else{
				holder.btn_survey.setText("已投票");
			}
		}else{
			if (entity.getSurveyAnswer() == null) {
				holder.btn_survey.setText("投票");
				if (appSet != null) {
					try {
						color = Color.parseColor(appSet.getColor2());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (color != 0) {
					// 设置颜色
					holder.btn_survey.setBackgroundColor(color);
				}
				holder.btn_survey.setClickable(true);
				holder.btn_survey.setTag(entity);
				holder.btn_survey.setOnClickListener(clicklistener);
			} else {
				holder.btn_survey.setText("已投票");
				holder.btn_survey.setClickable(false);
				holder.btn_survey.setBackgroundColor(mActivity.getResources()
						.getColor(R.color.btn_disable));
			}
		}

		return convertView;
	}

	OnClickListener clicklistener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			final SurveyDetailEntity surveydetailentity = (SurveyDetailEntity) v
					.getTag();
			final Button btn = (Button) v;
			final String[] answer = surveydetailentity.getAnswer();
			int count = 0;
			for (int i = 0; i < answer.length; i++) {
				if (answer[i].equals("1")) {
					count++;
				}
			}
			int iMax =0;
			int optionType = surveydetailentity.getOptionType();
			if(!TextUtils.isEmpty(surveydetailentity.getOptionMax())){
				iMax = Integer.parseInt(surveydetailentity.getOptionMax());
			}
			int iMin = 0;
			if(!TextUtils.isEmpty(surveydetailentity.getOptionMin())){
				iMin= Integer.parseInt(surveydetailentity.getOptionMin());
			}
			if(optionType == 0){
				if(count == 0){
					Toast.show(mContext, "请选择选项！");
					return;
				}
			}else{
				if(count == 0){
					Toast.show(mContext, "请选择选项！");
					return;
				}else{
					if(count < iMin){
						Toast.show(mContext, "最少选择"+iMin+"项");
						return;
					}else if(count>iMax){
						Toast.show(mContext, "最多选择"+iMax+"项");
						return;
					}
				}
			}

			String answers = "";
			for (String a : answer) {
				answers += a + "^";
			}
			answers = answers.substring(0, answers.length() - 1);
			final String finalanswer = answers;
			
			OperationBuilder builder = new OperationBuilder().append(
					new SurveyAOp(), surveydetailentity);
			OnOperationListener listener = new OnOperationListener() {
				@Override
				public void onOperationFinished(List<Object> resList) {
					if (mActivity.isFinishing()) {
						return;
					}
					if (resList == null) {
						Toast.show(mContext, "连接超时");
						return;
					}
					SurveyAEntity surveyaentity = (SurveyAEntity) resList
							.get(0);
					List<SurveyDetailEntity.SurveyRate> retobj = surveyaentity
							.getRetobj();
					int flag = surveyaentity.getFlag();
					if (flag != 1 && (retobj == null || retobj.size() == 0)) {
						Toast.show(mContext, "投票失败！");
						return;
					} else {
						Toast.show(mContext, "投票成功！");
						//btn.setText("已投票");
						//btn.setClickable(false);
						//btn.setBackgroundColor(mActivity.getResources().getColor(R.color.btn_disable));
						
						//SurveyAns sruveyans = new SurveyAns();
						//sruveyans.setAnswers(finalanswer);
						//surveydetailentity.setSurveyAnswer(sruveyans);
						//notifyDataSetChanged();
						
						
						
						/*int index = data.indexOf(surveyaentity);
						if (index != -1) {
							((SurveyDetailEntity) data.get(index))
									.setLoginName(SharedPreferencesUtils
											.getLoginName(JoyApplication
													.getSelf()));
							SurveyAns sruveyans = new SurveyAns();
							sruveyans.setAnswers(finalanswer);
							((SurveyDetailEntity) data.get(index))
									.setSurveyAnswer(sruveyans);

							((SurveyDetailEntity) data.get(index))
									.setSurveyRates(retobj);
						}*/
						
						//重调接口获取数据
						((SurveyActivity)mContext).reLoad();
					}
				}

				@Override
				public void onOperationError(Exception e) {
					e.printStackTrace();
				}
			};

			JsonCommon task = new JsonCommon(mContext, builder, listener,
					JsonCommon.PROGRESSCOMMIT);
			task.execute();
		}
	};

	public class ViewHolder {
		ImageView iv_survey;
		TextView tv_surveyname;
		TextView tv_expiretime;
		LinearLayout layout_multichoice;
		Button btn_survey;
	}
}
