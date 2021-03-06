package com.perky.safeguard361.ui;

import com.perky.safeguard361.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingView extends RelativeLayout {
	private CheckBox cb_status;
	private TextView tv_desc;
	private TextView tv_title;
	private String[] descs;

	public SettingView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public SettingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		String title = attrs.getAttributeValue(
				"http://schemas.android.com/apk/res/com.perky.safeguard361",
				"title");
		String desc = attrs.getAttributeValue(
				"http://schemas.android.com/apk/res/com.perky.safeguard361",
				"desc");
		// 打卡|关闭
		init(context);
		setTitle(title);
		descs = desc.split("#");
		setDesc(descs, false);
	}

	public SettingView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		// 把资源文件转化成view对象，显示在自己身上
		View.inflate(context, R.layout.ui_setting_view, this);
		cb_status = (CheckBox) findViewById(R.id.cb_status);
		tv_desc = (TextView) findViewById(R.id.tv_desc);
		tv_title = (TextView) findViewById(R.id.tv_title);
		this.setBackgroundResource(R.drawable.list_selector);
	}
	
	/**
	 * 设置自定义控件的标题
	 * 
	 * @param text
	 */
	public void setTitle(String text) {
		tv_title.setText(text);
	}

	/**
	 * 设置自定义控件的描述
	 * 
	 * @param text
	 */
	public void setDesc(String[] descs, boolean checked) {
		this.descs = descs;
		if (checked) {
			tv_desc.setText(descs[0]);
		} else {
			tv_desc.setText(descs[1]);
		}
	}

	/**
	 * 判断组合控件是否被选中
	 * 
	 * @return
	 */
	public boolean isChecked() {
		return cb_status.isChecked();
	}

	/**
	 * 设置组合控件的选中方式
	 * 
	 * @param checked
	 */
	public void setChecked(boolean checked) {
		cb_status.setChecked(checked);
		if (checked) {
			if (descs != null) {
				tv_desc.setText(descs[0]);
			}
		} else {
			if (descs != null) {
				tv_desc.setText(descs[1]);
			}
		}
	}
}
