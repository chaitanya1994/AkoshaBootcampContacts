package database.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import contacts.pojo.ContactsData;

/**
 * Created by chaitanya on 15/02/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private String name;
    private String phone;
    private String photo;
    private int id;
    public static final List<ContactsData> DATA_LIST = new ArrayList<>();
    private static final String DATABASE_NAME = "LocalContacts.db";
    private static final String TABLE_NAME = "contacts";
    private static final String CONTACTS_COLUMN_ID = "id";
    private static final String CONTACTS_COLUMN_NAME = "name";
    private static final String CONTACTS_COLUMN_PHONE = "phone";
    private static final String CONTACTS_COLUMN_PHOTO = "photo";
    public static final String CREATE_QUERY = "CREATE TABLE contacts(id integer primary key, name text, phone text,  photo text)";
    public static final String UPDATE_QUERY = "DROP TABLE IF EXISTS contacts";
    private static final String SELECT_ALL = "SELECT * from Contacts order by name";
    private HashMap hashMap;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_QUERY);
    }

    public boolean isAvailable() {
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery(SELECT_ALL, null);
        return cursor.getCount() > 0 ? true : false;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(UPDATE_QUERY);
        onCreate(db);
    }

    public void insertContact(int id, String name, String phone, String photo) {

        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACTS_COLUMN_ID, id);
        contentValues.put(CONTACTS_COLUMN_NAME, name);
        contentValues.put(CONTACTS_COLUMN_PHONE, phone);
        contentValues.put(CONTACTS_COLUMN_PHOTO, photo);
        database.insert(TABLE_NAME, null, contentValues);
    }

    public void clearDatabase() {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL("Delete from contacts where 1");
    }

    public static List<ContactsData> getDataList() {
        return DATA_LIST;
    }

    public List<ContactsData> getAllContacts() {
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery(SELECT_ALL, null);
        cursor.moveToFirst();
        Integer count = cursor.getCount();
        Log.d("count", count.toString());
        DATA_LIST.clear();
        while (!cursor.isAfterLast()) {
            name = phone = photo = null;
            id = 0;
            id = cursor.getInt(cursor.getColumnIndex(CONTACTS_COLUMN_ID));
            name = cursor.getString(cursor.getColumnIndex(CONTACTS_COLUMN_NAME));
            phone = cursor.getString(cursor.getColumnIndex(CONTACTS_COLUMN_PHONE));
            photo = cursor.getString(cursor.getColumnIndex(CONTACTS_COLUMN_PHOTO));
            DATA_LIST.add(new ContactsData(id, name, phone, photo));
            cursor.moveToNext();
        }
        return DATA_LIST;
    }


    public void addToDb(String name, String number) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACTS_COLUMN_NAME, name);
        contentValues.put(CONTACTS_COLUMN_PHONE, number);
        database.insert(TABLE_NAME, null, contentValues);
        getAllContacts();
    }

 public void addToDb(String name, String number, String uri) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACTS_COLUMN_NAME, name);
        contentValues.put(CONTACTS_COLUMN_PHONE, number);
        contentValues.put(CONTACTS_COLUMN_PHOTO,uri);
        database.insert(TABLE_NAME, null, contentValues);
        getAllContacts();
    }

    public void deleteContact(int id) {
        SQLiteDatabase database = getWritableDatabase();
        database.delete(TABLE_NAME, CONTACTS_COLUMN_ID + " = " + id, null);
        getAllContacts();
    }

    public void editContact(String nameEdit, String numberEdit, int id) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACTS_COLUMN_NAME,nameEdit);
        contentValues.put(CONTACTS_COLUMN_PHONE,numberEdit);
        database.update(TABLE_NAME, contentValues, CONTACTS_COLUMN_ID + " = " + id, null);
        getAllContacts();
    }

    public void editContact(String nameEdit, String numberEdit, String uri, int id) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACTS_COLUMN_NAME,nameEdit);
        contentValues.put(CONTACTS_COLUMN_PHONE,numberEdit);
        contentValues.put(CONTACTS_COLUMN_PHOTO,uri);
        database.update(TABLE_NAME, contentValues, CONTACTS_COLUMN_ID + " = " + id, null);
        getAllContacts();
    }
}