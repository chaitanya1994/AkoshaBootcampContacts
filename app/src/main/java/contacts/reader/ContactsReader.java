package contacts.reader;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import com.example.chaitanya.akoshabootcampcontacts.Contacts;

import database.helper.DatabaseHelper;

/**
 * Created by chaitanya on 15/02/16.
 */
public class ContactsReader {


    public void addContactstoDb(Context context) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        ContentResolver contentResolver = context.getContentResolver();

        Cursor phones = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        if (phones.getCount() <= 0) {
            return;
        }
        if (!databaseHelper.isAvailable()) {
            while (phones.moveToNext()) {
                int id = phones.getInt(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));
                String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String photo = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));

                databaseHelper.insertContact(id, name, phoneNumber, photo);
            }
        }
        databaseHelper.getAllContacts();
    }

}
