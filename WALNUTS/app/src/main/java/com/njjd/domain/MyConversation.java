package com.njjd.domain;

import com.hyphenate.chat.EMConversation;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author wmm
 * @description
 * @date 2018-8-24 下午1:34:00
 */
public class MyConversation {
	EMConversation conversation;
	String name="";
	String avatar="";
	String openId="";

	public MyConversation(EMConversation conversation) {
		this.conversation = conversation;
	}

	public void setJson(JSONObject json) {
		try {
			name = json.isNull("name") ? "" : json.getString("name");
			avatar = json.isNull("avatar") ? "" : json.getString("avatar");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public EMConversation getConversation() {
		return conversation;
	}

	public void setConversation(EMConversation conversation) {
		this.conversation = conversation;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

}
