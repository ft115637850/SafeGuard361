package com.perky.safeguard361.activities;

import java.util.ArrayList;
import java.util.List;

import com.perky.safeguard361.R;
import com.perky.safeguard361.domain.TaskInfo;
import com.perky.safeguard361.engine.TaskInfoParser;
import com.perky.safeguard361.utils.SystemInfoUtils;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

public class TaskManagerActivity extends Activity {
	private TextView tv_tasksCount;
	private TextView tv_memorInfo;
	private ListView lv_task_mgr;
	private TaskMgrAdapter taskMgrAdapter;
	private List<TaskInfo> taskInfos;
	private List<TaskInfo> usrTaskInfos;
	private List<TaskInfo> sysTaskInfos;
	private ProgressBar pb_tasks;
	private TextView tv_taskCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_taskmgr);
		tv_tasksCount = (TextView) findViewById(R.id.tv_tasksCount);
		tv_memorInfo = (TextView) findViewById(R.id.tv_memorInfo);
		lv_task_mgr = (ListView) findViewById(R.id.lv_task_mgr);
		pb_tasks = (ProgressBar) findViewById(R.id.pb_tasks);
		tv_taskCount = (TextView) findViewById(R.id.tv_taskCount);

		int processCount = SystemInfoUtils.getProcessCount(this);
		tv_tasksCount.setText("运行中进程:" + processCount + "个");
		long availMem = SystemInfoUtils.getAvailMem(this);
		long totalMem = SystemInfoUtils.getTotalMem();
		tv_memorInfo.setText("可用/总内存:"
				+ Formatter.formatFileSize(this, availMem) + "/"
				+ Formatter.formatFileSize(this, totalMem));

		fillData();

		lv_task_mgr.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (sysTaskInfos != null && usrTaskInfos != null) {
					if (firstVisibleItem > usrTaskInfos.size()) {
						tv_taskCount.setText("系统进程: " + sysTaskInfos.size()
								+ "个");
					} else {
						tv_taskCount.setText("用户进程: " + usrTaskInfos.size()
								+ "个");
					}
				}
			}
		});

		lv_task_mgr.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Object obj = lv_task_mgr.getItemAtPosition(position);
				if (obj != null && obj instanceof TaskInfo) {
					ViewHolder holder = (ViewHolder) view.getTag();
					TaskInfo taskInfo = (TaskInfo) obj;
					if (taskInfo.getPackName().equals(getPackageName())) {
						return;
					}
					if (taskInfo.isChecked()) {
						taskInfo.setChecked(false);
						holder.cb_task_status.setChecked(false);
					} else {
						taskInfo.setChecked(true);
						holder.cb_task_status.setChecked(true);
					}
				}
			}
		});
	}

	private void fillData() {
		pb_tasks.setVisibility(View.VISIBLE);
		new Thread() {
			public void run() {
				usrTaskInfos = new ArrayList<TaskInfo>();
				sysTaskInfos = new ArrayList<TaskInfo>();
				taskInfos = TaskInfoParser
						.getRunningTaskInfos(TaskManagerActivity.this);
				for (TaskInfo taskInfo : taskInfos) {
					if (taskInfo.isUsrTask()) {
						usrTaskInfos.add(taskInfo);
					} else {
						sysTaskInfos.add(taskInfo);
					}
				}
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						pb_tasks.setVisibility(View.INVISIBLE);
						taskMgrAdapter=new TaskMgrAdapter();
						lv_task_mgr.setAdapter(taskMgrAdapter);
					}
				});

			}
		}.start();

	}

	public void killProcess(View view) {

	}

	public void selectAll(View view) {
		for (TaskInfo taskInfo : taskInfos){
			if (taskInfo.getPackName().equals(getPackageName())) {
				continue;
			}
			taskInfo.setChecked(true);
		}
		taskMgrAdapter.notifyDataSetChanged();
	}

	public void selectOpposite(View view) {
		for (TaskInfo taskInfo : taskInfos){
			if (taskInfo.getPackName().equals(getPackageName())) {
				continue;
			}
			taskInfo.setChecked(!taskInfo.isChecked());
		}
		taskMgrAdapter.notifyDataSetChanged();
	}

	public void openSetting(View view) {

	}

	static class ViewHolder {
		public ImageView iv_task_icon;
		public TextView tv_task_name;
		public TextView tv_task_size;
		public CheckBox cb_task_status;
	}

	private class TaskMgrAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return taskInfos.size();
		}

		@Override
		public Object getItem(int position) {
			TaskInfo taskInfo;
			if (position == 0) {
				return null;
			} else if (position == (usrTaskInfos.size() + 1)) {
				return null;
			} else if (position <= usrTaskInfos.size()) {
				taskInfo = usrTaskInfos.get(position - 1);
			} else {
				taskInfo = sysTaskInfos.get(position - 1 - usrTaskInfos.size()
						- 1);
			}
			return taskInfo;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TaskInfo taskInfo;
			if (position == 0) {
				TextView txt = new TextView(getApplicationContext());
				txt.setBackgroundColor(Color.GRAY);
				txt.setTextColor(Color.WHITE);
				txt.setText("用户进程有" + usrTaskInfos.size() + "个");
				return txt;
			} else if (position == (usrTaskInfos.size() + 1)) {
				TextView txt = new TextView(getApplicationContext());
				txt.setBackgroundColor(Color.GRAY);
				txt.setTextColor(Color.WHITE);
				txt.setText("系统进程有" + usrTaskInfos.size() + "个");
				return txt;
			} else if (position <= usrTaskInfos.size()) {
				taskInfo = usrTaskInfos.get(position - 1);
			} else {
				taskInfo = sysTaskInfos.get(position - 1 - usrTaskInfos.size()
						- 1);
			}

			View view;
			ViewHolder holder;
			if (convertView != null && convertView instanceof RelativeLayout) {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			} else {
				view = View.inflate(getApplicationContext(),
						R.layout.item_task_mgr, null);
				holder = new ViewHolder();
				holder.iv_task_icon = (ImageView) view
						.findViewById(R.id.iv_task_icon);
				holder.tv_task_name = (TextView) view
						.findViewById(R.id.tv_task_name);
				holder.tv_task_size = (TextView) view
						.findViewById(R.id.tv_task_size);
				holder.cb_task_status = (CheckBox) view
						.findViewById(R.id.cb_task_status);
				view.setTag(holder);
			}

			holder.iv_task_icon.setImageDrawable(taskInfo.getAppIcon());
			holder.tv_task_name.setText(taskInfo.getAppName());
			holder.tv_task_size.setText("占用内存："
					+ Formatter.formatFileSize(getApplicationContext(),
							taskInfo.getMemSize()));
			holder.cb_task_status.setChecked(taskInfo.isChecked());
			if (taskInfo.getPackName().equals(getPackageName())) {
				holder.cb_task_status.setVisibility(View.INVISIBLE);
			} else {
				holder.cb_task_status.setVisibility(View.VISIBLE);
			}
			return view;
		}

	}
}
