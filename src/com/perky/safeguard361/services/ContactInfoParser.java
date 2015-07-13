package com.perky.safeguard361.services;

import java.util.ArrayList;
import java.util.List;

import com.perky.safeguard361.domain.ContactInfo;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class ContactInfoParser {
	public static List<ContactInfo> findAll(Context context) {
		List<ContactInfo> result = new ArrayList<ContactInfo>();
		ContentResolver resolver = context.getContentResolver();
		Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
		Uri uriData = Uri.parse("content://com.android.contacts/data");
		Cursor cur = resolver.query(uri, new String[] { "contact_id" }, null,
				null, null);
		while (cur.moveToNext()) {
			ContactInfo contact = new ContactInfo();
			String id = cur.getString(0);
			contact.setId(id);
			Cursor curData = resolver.query(uriData, new String[] { "data1",
					"mimetype" }, "raw_contact_id=?", new String[] { id },
					null);
			while (curData.moveToNext()){
				String data1 = curData.getString(0);
				String mime_type = curData.getString(1);
				
				if ("vnd.android.cursor.item/phone_v2".equals(mime_type)){
					contact.setPhone(data1);
				}else if("vnd.android.cursor.item/postal-address_v2".equals(mime_type)){
					contact.setAddress(data1);
				}else if ("vnd.android.cursor.item/email_v2".equals(mime_type)){
					contact.setEmail(data1);
				}else if ("vnd.android.cursor.item/name".equals(mime_type)){
					contact.setName(data1);
				}else if ("vnd.android.cursor.item/im".equals(mime_type)){
					contact.setIm(data1);
				}
			}
			result.add(contact);
			curData.close();
		}
		cur.close();
		return result;
	}
}
