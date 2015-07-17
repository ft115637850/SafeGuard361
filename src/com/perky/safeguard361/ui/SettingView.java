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
		// ��|�ر�
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
		// ����Դ�ļ�ת����view������ʾ���Լ�����
		View.inflate(context, R.layout.ui_setting_view, this);
		cb_status = (CheckBox) findViewById(R.id.cb_status);
		tv_desc = (TextView) findViewById(R.id.tv_desc);
		tv_title = (TextView) findViewById(R.id.tv_title);
		this.setBackgroundResource(R.drawable.list_selector);
	}
	
	/**
	 * �����Զ���ؼ��ı���
	 * 
	 * @param text
	 */
	public void setTitle(String text) {
		tv_title.setText(text);
	}

	/**
	 * �����Զ���ؼ�������
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
	 * �ж���Ͽؼ��Ƿ�ѡ��
	 * 
	 * @return
	 */
	public boolean isChecked() {
		return cb_status.isChecked();
	}

	/**
	 * ������Ͽؼ���ѡ�з�ʽ
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
