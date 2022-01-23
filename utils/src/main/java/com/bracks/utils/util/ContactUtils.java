package com.bracks.utils.util;

import android.content.ContentUris;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.Utils;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * good programmer.
 *
 * @date : 2021-04-06 9:47
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class ContactUtils {
    private static final String TAG = "ContactUtils";
    private static final List<ContactsChangeListener> CONTACTS_LISTENERS = new ArrayList<>();
    private static final List<CallRecordsChangeListener> RECORDS_LISTENERS = new ArrayList<>();
    private static Handler handler;
    private final Object lock = new Object();
    private ContactsContentObserver contactsContentObserver;
    private CallRecordsContentObserver callRecordsContentObserver;
    private Runnable callRecordsRunnable = null;
    private Runnable contactsRunnable = null;

    private ContactUtils() {
        HandlerThread mHandlerThread = new HandlerThread("ContactUtils-Thread");
        mHandlerThread.start();
        handler = new Handler(mHandlerThread.getLooper());
    }

    public static ContactUtils getInstance() {
        return SingleInstanceHolder.INSTANCE;
    }

    /**
     * 通过号码获取联系人名字
     *
     * @param phoneNumber
     * @return
     */
    public static String getNameForNumber(String phoneNumber) {
        try {
            Uri lookupUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
            Cursor cursor = Utils
                    .getApp()
                    .getContentResolver()
                    .query(
                            lookupUri,
                            new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME},
                            null,
                            null,
                            null
                    );

            if (cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(TAG, "getNameForNumber : " + e.toString());
        }
        return phoneNumber;
    }

    /**
     * 根据contactId获得联系人头像
     *
     * @param contactId
     * @return
     */
    public static InputStream getContactPhoto(long contactId) {
        Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
        return ContactsContract.Contacts.openContactPhotoInputStream(Utils.getApp().getContentResolver(), contactUri);
    }

    public void registerContactsObserver(ContactsChangeListener contactsChangeListener) {
        synchronized (lock) {
            CONTACTS_LISTENERS.add(contactsChangeListener);
        }
        contactsContentObserver = new ContactsContentObserver(this);
        Utils.getApp().getContentResolver().registerContentObserver(ContactsContract.Contacts.CONTENT_URI, true, contactsContentObserver);
    }

    public void unRegisterContactsObserver(ContactsChangeListener contactsChangeListener) {
        if (contactsContentObserver != null) {
            Utils.getApp().getContentResolver().unregisterContentObserver(contactsContentObserver);
            contactsContentObserver = null;
            synchronized (lock) {
                if (contactsChangeListener != null) {
                    CONTACTS_LISTENERS.remove(contactsChangeListener);
                } else {
                    CONTACTS_LISTENERS.clear();
                }
            }
        }
    }

    public void registerCallRecordsObserver(CallRecordsChangeListener recordsChangeListener) {
        synchronized (lock) {
            RECORDS_LISTENERS.add(recordsChangeListener);
        }
        callRecordsContentObserver = new CallRecordsContentObserver(this);
        Utils.getApp().getContentResolver().registerContentObserver(CallLog.Calls.CONTENT_URI, true, callRecordsContentObserver);
    }

    public void unRegisterCallRecordsObserver(CallRecordsChangeListener recordsChangeListener) {
        if (callRecordsContentObserver != null) {
            Utils.getApp().getContentResolver().unregisterContentObserver(callRecordsContentObserver);
            callRecordsContentObserver = null;
            synchronized (lock) {
                if (recordsChangeListener != null) {
                    RECORDS_LISTENERS.remove(recordsChangeListener);
                } else {
                    RECORDS_LISTENERS.clear();
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getContacts() {
        if (contactsRunnable == null) {
            contactsRunnable = () -> {
                List<Contacts> contacts = new ArrayList<>();
                Cursor contactCursor = Utils
                        .getApp()
                        .getContentResolver()
                        .query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
                while (contactCursor.moveToNext()) {
                    String contactId = contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.Contacts._ID));
                    Contacts contact = new Contacts();
                    contact.setContactId(contactId);
                    //获取联系人姓名
                    String name = contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    contact.setDisplayName(name);
                    //获取联系人电话号码
                    Cursor phoneCursor = Utils
                            .getApp()
                            .getContentResolver()
                            .query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId,
                                    null,
                                    null
                            );
                    List<Contacts.PhoneData> mPhoneList = new ArrayList<>();
                    while (phoneCursor.moveToNext()) {
                        Contacts.PhoneData phoneData = new Contacts.PhoneData();
                        String number = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        number = number.replace("-", "").trim();
                        phoneData.setNumber(number);
                        int phoneType = phoneCursor.getInt(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                        int phoneLable = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LABEL);
                        phoneData.setType(phoneType);
                        if (phoneLable == ContactsContract.CommonDataKinds.Phone.TYPE_CUSTOM) {
                            phoneData.setLabel(phoneCursor.getString(phoneLable));
                        } else {
                            phoneData.setLabel((String) ContactsContract.CommonDataKinds.Phone.getTypeLabel(Utils.getApp().getResources(), phoneType, ""));
                        }
                        mPhoneList.add(phoneData);
                    }
                    contact.setPhoneList(mPhoneList);
                    //获取联系人备注信息
                    Cursor noteCursor = Utils
                            .getApp()
                            .getContentResolver()
                            .query(
                                    ContactsContract.Data.CONTENT_URI,
                                    new String[]{ContactsContract.Data._ID, ContactsContract.CommonDataKinds.Nickname.NAME},
                                    ContactsContract.Data.CONTACT_ID + "=?" + " AND " + ContactsContract.Data.MIMETYPE + "='" + ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE + "'",
                                    new String[]{contactId},
                                    null
                            );
                    if (noteCursor.moveToFirst()) {
                        do {
                            String note = noteCursor.getString(noteCursor.getColumnIndex(ContactsContract.CommonDataKinds.Nickname.NAME));
                            Log.v(TAG, "note : " + note);
                        } while (noteCursor.moveToNext());
                    }
                    contacts.add(contact);
                    phoneCursor.close();
                    noteCursor.close();
                }
                contactCursor.close();
                CONTACTS_LISTENERS.forEach(contactsChangeListener -> contactsChangeListener.onContactsChanged(contacts));
                Log.v(TAG, "getContacts : " + contacts.size());
            };
        }
        handler.removeCallbacks(contactsRunnable);
        handler.post(contactsRunnable);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getCallRecords() {
        if (callRecordsRunnable == null) {
            callRecordsRunnable = () -> {
                /**
                 * @param uri 需要查询的URI，（这个URI是ContentProvider提供的）
                 * @param projection 需要查询的字段
                 * @param selection sql语句where之后的语句
                 * @param selectionArgs ?占位符代表的数据
                 * @param sortOrder 排序方式
                 *
                 */
                Cursor cursor = Utils
                        .getApp()
                        .getContentResolver()
                        .query(
                                //查询通话记录的URI
                                CallLog.Calls.CONTENT_URI,
                                //通话记录的联系人
                                new String[]{
                                        CallLog.Calls.CACHED_NAME
                                        //通话记录的电话号码
                                        , CallLog.Calls.NUMBER
                                        //通话记录的日期
                                        , CallLog.Calls.DATE
                                        //通话时长
                                        , CallLog.Calls.DURATION
                                        //通话类型
                                        , CallLog.Calls.TYPE}
                                //按照时间逆序排列，最近打的最先显示
                                , null,
                                null,
                                CallLog.Calls.DEFAULT_SORT_ORDER
                        );
                List<PhoneRecord> PhoneRecords = new ArrayList<>();
                while (cursor.moveToNext()) {
                    String name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
                    String phoneNumber = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                    long dateLong = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE));
                    int duration = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.DURATION));
                    int type = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE));
                    phoneNumber = phoneNumber.replace("-", "").trim();
                    PhoneRecord phoneRecord = new PhoneRecord();
                    name = TextUtils.isEmpty(name) ? getNameForNumber(phoneNumber) : name;
                    phoneRecord.setDisplayName(name);
                    phoneRecord.setNumber(phoneNumber);
                    phoneRecord.setDuration(duration);
                    phoneRecord.setCallDatetime(TimeUtils.millis2String(dateLong, "yyyyMMdd'T'HHmmss"));
                    phoneRecord.setCallType(type);
                    PhoneRecords.add(phoneRecord);
                }
                cursor.close();
                RECORDS_LISTENERS.forEach(callRecordsChangeListener -> callRecordsChangeListener.onCallRecordsChanged(PhoneRecords));
                Log.v(TAG, "getCallRecords : " + PhoneRecords.size());
            };
        }
        handler.removeCallbacks(callRecordsRunnable);
        handler.post(callRecordsRunnable);
    }

    public interface ContactsChangeListener {
        void onContactsChanged(List<Contacts> contactsList);
    }

    public interface CallRecordsChangeListener {
        void onCallRecordsChanged(List<PhoneRecord> PhoneRecords);
    }

    private static class SingleInstanceHolder {
        private static final ContactUtils INSTANCE = new ContactUtils();
    }

    static class ContactsContentObserver extends ContentObserver {
        private final WeakReference<ContactUtils> mObserverWeakReference;

        public ContactsContentObserver(ContactUtils contactUtils) {
            super(new Handler());
            mObserverWeakReference = new WeakReference<>(contactUtils);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            ContactUtils contactUtils = mObserverWeakReference.get();
            if (contactUtils != null) {
                contactUtils.getContacts();
            }
        }
    }

    static class CallRecordsContentObserver extends ContentObserver {
        private final WeakReference<ContactUtils> mObserverWeakReference;

        public CallRecordsContentObserver(ContactUtils contactUtils) {
            super(new Handler());
            mObserverWeakReference = new WeakReference<>(contactUtils);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            ContactUtils contactUtils = mObserverWeakReference.get();
            if (contactUtils != null) {
                contactUtils.getCallRecords();
            }
        }
    }

    public static class Contacts {
        private String contactId;
        private String displayName;
        private List<Contacts.PhoneData> phoneList;

        public String getContactId() {
            return contactId;
        }

        public void setContactId(String contactId) {
            this.contactId = contactId;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public List<PhoneData> getPhoneList() {
            return phoneList;
        }

        public void setPhoneList(List<PhoneData> phoneList) {
            this.phoneList = phoneList;
        }

        public static class PhoneData {
            private String number;
            private int type;
            private String label;

            public String getNumber() {
                return number;
            }

            public void setNumber(String number) {
                this.number = number;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getLabel() {
                return label;
            }

            public void setLabel(String label) {
                this.label = label;
            }
        }
    }


    public static class PhoneRecord {
        private String displayName;
        private String number;
        private int duration;
        private String callDatetime;
        private int callType;

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public String getCallDatetime() {
            return callDatetime;
        }

        public void setCallDatetime(String callDatetime) {
            this.callDatetime = callDatetime;
        }

        public int getCallType() {
            return callType;
        }

        public void setCallType(int callType) {
            this.callType = callType;
        }
    }
}