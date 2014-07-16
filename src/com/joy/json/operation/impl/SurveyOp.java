package com.joy.json.operation.impl;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;

import com.joy.JoyApplication;
import com.joy.Utils.MD5;
import com.joy.Utils.SharedPreferencesUtils;
import com.joy.json.http.AbstractHttpApi;
import com.joy.json.http.HttpApi;
import com.joy.json.http.HttpApiWithBasicAuth;
import com.joy.json.model.SurveyEntity;
import com.joy.json.operation.ITaskOperation;
import com.joy.json.parse.SurveyParse;

/**
 * 调查列表
 * @author daiye
 *
 */
public class SurveyOp implements ITaskOperation {

	@Override
	public Object exec(Object in, Object res) throws Exception {
		SurveyEntity sur = (SurveyEntity)in;
		DefaultHttpClient httpClient = AbstractHttpApi.createHttpClient();
		httpClient.getParams().setParameter(
				HttpConnectionParams.CONNECTION_TIMEOUT, TIMEOUT);
		httpClient.getParams().setParameter(HttpConnectionParams.SO_TIMEOUT,
				TIMEOUT);
		HttpApi httpApi = new HttpApiWithBasicAuth(httpClient, "testRest");
		HttpGet get = httpApi.createHttpGet(
				IP,
				new BasicNameValuePair("action", "comp_survey"),
				new BasicNameValuePair("json", String.format(
						"{\"loginname\":\"%s\",\"company\":\"%s\",\"isexpired\": \"%s\"}", SharedPreferencesUtils
								.getLoginName(JoyApplication.getSelf()), SharedPreferencesUtils
								.getCompany(JoyApplication.getSelf()), sur.isexpired)),
								new BasicNameValuePair("token", new MD5()
								.getMD5ofStr(SharedPreferencesUtils
										.getLoginName(JoyApplication.getSelf())
										+ MD5.key)));
		return (SurveyEntity) httpApi.doHttpRequest(get, new SurveyParse());
	}
}
