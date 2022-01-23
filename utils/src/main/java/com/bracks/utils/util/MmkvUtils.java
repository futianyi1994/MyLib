package com.bracks.utils.util;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tencent.mmkv.MMKV;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * good programmer.
 *
 * @date : 2021-06-03 16:29
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class MmkvUtils {

    private static final Map<String, MmkvUtils> MMKV_UTILS_MAP = new HashMap<>();

    private final MMKV mmkv;

    private MmkvUtils(String mmapID) {
        mmkv = MMKV.mmkvWithID(mmapID);
    }

    private MmkvUtils(String mmapID, int mode) {
        mmkv = MMKV.mmkvWithID(mmapID, mode);
    }

    private MmkvUtils(String mmapID, int mode, @Nullable String cryptKey) {
        mmkv = MMKV.mmkvWithID(mmapID, mode, cryptKey);
    }

    private MmkvUtils(String mmapID, String rootPath) {
        mmkv = MMKV.mmkvWithID(mmapID, rootPath);
    }

    private MmkvUtils(String mmapID, int mode, @Nullable String cryptKey, String rootPath) {
        mmkv = MMKV.mmkvWithID(mmapID, mode, cryptKey, rootPath);
    }

    public static MmkvUtils getInstance() {
        return getInstance("", MMKV.SINGLE_PROCESS_MODE, (String) null, (String) null);
    }

    public static MmkvUtils getInstance(String mmapID) {
        return getInstance(mmapID, MMKV.SINGLE_PROCESS_MODE, (String) null, (String) null);
    }

    public static MmkvUtils getInstance(String mmapID, int mode) {
        return getInstance(mmapID, mode, (String) null, (String) null);
    }

    public static MmkvUtils getInstance(String mmapID, int mode, @Nullable String cryptKey) {
        return getInstance(mmapID, mode, cryptKey, (String) null);
    }

    public static MmkvUtils getInstance(String mmapID, String rootPath) {
        return getInstance(mmapID, MMKV.SINGLE_PROCESS_MODE, (String) null, rootPath);
    }

    public static MmkvUtils getInstance(String mmapID, int mode, @Nullable String cryptKey, String rootPath) {
        if (isSpace(mmapID)) {
            mmapID = "DefaultMMKV";
        }

        MmkvUtils MmkvUtils = MMKV_UTILS_MAP.get(mmapID);
        if (MmkvUtils == null) {
            synchronized (MmkvUtils.class) {
                MmkvUtils = MMKV_UTILS_MAP.get(mmapID);
                if (MmkvUtils == null) {
                    MmkvUtils = new MmkvUtils(mmapID, mode, cryptKey, rootPath);
                    MMKV_UTILS_MAP.put(mmapID, MmkvUtils);
                }
            }
        }
        return MmkvUtils;
    }

    private static boolean isSpace(String s) {
        if (s == null) {
            return true;
        }
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public boolean put(@NonNull String key, String value) {
        return mmkv.encode(key, value);
    }

    @Nullable
    public String getString(@NonNull String key) {
        return getString(key, "");
    }

    @Nullable
    public String getString(@NonNull String key, String defaultValue) {
        return mmkv.decodeString(key, defaultValue);
    }

    public boolean put(@NonNull String key, int value) {
        return mmkv.encode(key, value);
    }

    public int getInt(@NonNull String key) {
        return getInt(key, -1);
    }

    public int getInt(@NonNull String key, int defaultValue) {
        return mmkv.decodeInt(key, defaultValue);
    }

    public boolean put(@NonNull String key, long value) {
        return mmkv.encode(key, value);
    }

    public long getLong(@NonNull String key) {
        return getLong(key, -1L);
    }

    public long getLong(@NonNull String key, long defaultValue) {
        return mmkv.decodeLong(key, defaultValue);
    }

    public boolean put(@NonNull String key, float value) {
        return mmkv.encode(key, value);
    }

    public float getFloat(@NonNull String key) {
        return getFloat(key, -1F);
    }

    public float getFloat(@NonNull String key, float defaultValue) {
        return mmkv.decodeFloat(key, defaultValue);
    }

    public boolean put(@NonNull String key, boolean value) {
        return mmkv.encode(key, value);
    }

    public boolean getBoolean(@NonNull String key) {
        return getBoolean(key, false);
    }

    public boolean getBoolean(@NonNull String key, boolean defaultValue) {
        return mmkv.decodeBool(key, defaultValue);
    }

    public boolean put(@NonNull String key, Set<String> value) {
        return mmkv.encode(key, value);
    }

    @Nullable
    public Set<String> getStringSet(@NonNull String key) {
        return getStringSet(key, Collections.emptySet());
    }

    @Nullable
    public Set<String> getStringSet(@NonNull String key, Set<String> defaultValue) {
        return mmkv.decodeStringSet(key, defaultValue);
    }

    @Nullable
    public String[] getAll() {
        return mmkv.allKeys();
    }

    public boolean contains(@NonNull String key) {
        return mmkv.containsKey(key);
    }

    public void remove(@NonNull String key) {
        mmkv.removeValueForKey(key);
    }

    public void clear() {
        mmkv.clearAll();
    }

    public MMKV getMmkv() {
        return mmkv;
    }
}
