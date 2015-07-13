package com.perky.safeguard361.activities;

import java.util.List;

import com.perky.safeguard361.R;
import com.perky.safeguard361.domain.ContactInfo;
import com.perky.safeguard361.services.ContactInfoParser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class SelectContactsActivity extends Activity {
	private ListView lv_contacts;
	private List<ContactInfo> contacts;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contacts);
		contacts = ContactInfoParser.findAll(this);
		lv_contacts = (ListView) findViewById(R.id.lv_contacts);
		lv_contacts.setAdapter(new lvAdapter());
		lv_contacts.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent data = new Intent();
				data.putExtra("phone", contacts.get(position).getPhone());
				setResult(0, data);
				finish();
			}
		});

	}

	private class lvAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return contacts.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			if (convertView == null) {
				view = View.inflate(getApplicationContext(),
						R.layout.item_contact, null);
			} else {
				view = convertView;
			}
			ContactInfo contact = contacts.get(position);
			TextView tv_contact_name = (TextView) view
					.findViewById(R.id.tv_contact_name);
			tv_contact_name.setText(contact.getName());
			TextView tv_contact_number = (TextView) view
					.findViewById(R.id.tv_contact_number);
			tv_contact_number.setText(contact.getPhone());
			return view;
		}

	}
}
