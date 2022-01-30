package com.bracks.player.lrcview;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.os.Build;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.bracks.player.Constant;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * good programmer.
 *
 * @date : 2020-06-24 10:22
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class LrcUtils {
    private static final String TAG = "LrcUtils";
    /********************************************************************************************************************/


    private static final Pattern PATTERN_LINE = Pattern.compile("((\\[\\d\\d:\\d\\d\\.\\d{2,3}\\])+)(.+)");
    private static final Pattern PATTERN_TIME = Pattern.compile("\\[(\\d\\d):(\\d\\d)\\.(\\d{2,3})\\]");
    /**
     * 歌词内容列表 （包括歌词和时间）
     */
    private List<LyricContent> lyricList = new ArrayList<>();

    /**
     * 从文件解析双语歌词
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    static List<LrcEntry> parseLrc(File[] lrcFiles) {
        if (lrcFiles == null || lrcFiles.length != 2 || lrcFiles[0] == null) {
            return null;
        }

        File mainLrcFile = lrcFiles[0];
        File secondLrcFile = lrcFiles[1];
        List<LrcEntry> mainEntryList = parseLrc(mainLrcFile);
        List<LrcEntry> secondEntryList = parseLrc(secondLrcFile);

        if (mainEntryList != null && secondEntryList != null) {
            for (LrcEntry mainEntry : mainEntryList) {
                for (LrcEntry secondEntry : secondEntryList) {
                    if (mainEntry.getTime() == secondEntry.getTime()) {
                        mainEntry.setSecondText(secondEntry.getText());
                    }
                }
            }
        }
        return mainEntryList;
    }

    /**
     * 从文件解析歌词
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static List<LrcEntry> parseLrc(File lrcFile) {
        if (lrcFile == null || !lrcFile.exists()) {
            return null;
        }

        List<LrcEntry> entryList = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(lrcFile), StandardCharsets.UTF_8));
            String line;
            while ((line = br.readLine()) != null) {
                List<LrcEntry> list = parseLine(line);
                if (list != null && !list.isEmpty()) {
                    entryList.addAll(list);
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Collections.sort(entryList);
        return entryList;
    }

    /**
     * 从文本解析双语歌词
     */
    static List<LrcEntry> parseLrc(String[] lrcTexts) {
        if (lrcTexts == null || lrcTexts.length != 2 || TextUtils.isEmpty(lrcTexts[0])) {
            return null;
        }

        String mainLrcText = lrcTexts[0];
        String secondLrcText = lrcTexts[1];
        List<LrcEntry> mainEntryList = parseLrc(mainLrcText);
        List<LrcEntry> secondEntryList = parseLrc(secondLrcText);

        if (mainEntryList != null && secondEntryList != null) {
            for (LrcEntry mainEntry : mainEntryList) {
                for (LrcEntry secondEntry : secondEntryList) {
                    if (mainEntry.getTime() == secondEntry.getTime()) {
                        mainEntry.setSecondText(secondEntry.getText());
                    }
                }
            }
        }
        return mainEntryList;
    }

    /**
     * 从文本解析歌词
     */
    private static List<LrcEntry> parseLrc(String lrcText) {
        if (TextUtils.isEmpty(lrcText)) {
            return null;
        }

        if (lrcText.startsWith("\uFEFF")) {
            lrcText = lrcText.replace("\uFEFF", "");
        }

        List<LrcEntry> entryList = new ArrayList<>();
        String[] array = lrcText.split("\\n");
        for (String line : array) {
            List<LrcEntry> list = parseLine(line);
            if (list != null && !list.isEmpty()) {
                entryList.addAll(list);
            }
        }

        Collections.sort(entryList);
        return entryList;
    }

    /**
     * 获取网络文本，需要在工作线程中执行
     */
    static String getContentFromNetwork(String url, String charset) {
        String lrcText = null;
        try {
            URL _url = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) _url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            if (conn.getResponseCode() == 200) {
                InputStream is = conn.getInputStream();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len;
                while ((len = is.read(buffer)) != -1) {
                    bos.write(buffer, 0, len);
                }
                is.close();
                bos.close();
                lrcText = bos.toString(charset);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lrcText;
    }

    /**
     * 解析一行歌词
     */
    private static List<LrcEntry> parseLine(String line) {
        if (TextUtils.isEmpty(line)) {
            return null;
        }

        line = line.trim();
        // [00:17.65]让我掉下眼泪的
        Matcher lineMatcher = PATTERN_LINE.matcher(line);
        if (!lineMatcher.matches()) {
            return null;
        }

        String times = lineMatcher.group(1);
        String text = lineMatcher.group(3);
        List<LrcEntry> entryList = new ArrayList<>();

        // [00:17.65]
        Matcher timeMatcher = PATTERN_TIME.matcher(times);
        while (timeMatcher.find()) {
            long min = Long.parseLong(timeMatcher.group(1));
            long sec = Long.parseLong(timeMatcher.group(2));
            String milString = timeMatcher.group(3);
            long mil = Long.parseLong(milString);
            // 如果毫秒是两位数，需要乘以10
            if (milString.length() == 2) {
                mil = mil * 10;
            }
            long time = min * DateUtils.MINUTE_IN_MILLIS + sec * DateUtils.SECOND_IN_MILLIS + mil;
            entryList.add(new LrcEntry(time, text));
        }
        return entryList;
    }

    /**
     * 转为[分:秒]
     */
    static String formatTime(long milli) {
        int m = (int) (milli / DateUtils.MINUTE_IN_MILLIS);
        int s = (int) ((milli / DateUtils.SECOND_IN_MILLIS) % 60);
        String mm = String.format(Locale.getDefault(), "%02d", m);
        String ss = String.format(Locale.getDefault(), "%02d", s);
        return mm + ":" + ss;
    }

    static void resetDurationScale() {
        try {
            @SuppressLint("SoonBlockedPrivateApi") Field mField = ValueAnimator.class.getDeclaredField("sDurationScale");
            mField.setAccessible(true);
            mField.setFloat(null, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 缓存歌词文件
     *
     * @param hash
     * @param lrcData
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void cacheLrc(String hash, String lrcData) {
        FileIOUtils.writeFileFromString(getCacheFile(hash), lrcData);

        AtomicInteger deleteCount = new AtomicInteger();
        List<File> files = FileUtils.listFilesInDir(new File(Constant.KUGOU_LYCIC_PATH), Comparator.comparingLong(File::lastModified));
        if (files.size() == Constant.MAX_CACHE_LRC_COUNT + 1) {
            FileUtils.delete(files.get(0));
            Log.i(TAG, "more max lrc cache delete oldest cache : " + files.get(0).getName());
        } else if (files.size() > Constant.MAX_CACHE_LRC_COUNT + 1) {
            deleteCount.set(files.size() - Constant.MAX_CACHE_LRC_COUNT);
            files.forEach(file -> {
                if (deleteCount.get() == 0) {
                    return;
                }
                Log.i(TAG, "more max lrc cache delete lrc cache : " + file.getName());
                FileUtils.delete(file);
                deleteCount.getAndDecrement();
            });
        }
    }

    public static boolean isCacheLrc(String hash) {
        return FileUtils.isFileExists(getCacheFile(hash));
    }

    public static String getCacheLrc(String hash) {
        return FileIOUtils.readFile2String(getCacheFile(hash));
    }

    /********************************************************************************/

    private static File getCacheFile(String hash) {
        return new File(Constant.KUGOU_LYCIC_PATH.concat(hash).concat(".lrc"));
    }

    /**
     * 解析lrc文件
     *
     * @param file
     * @throws FileNotFoundException
     * @throws IOException
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void read(File file) throws FileNotFoundException, IOException {
        String Lrc_data = "";
        // /mnt/sdcard/我不知道爱是什么.lrc
        FileInputStream mFileInputStream = new FileInputStream(file);
        InputStreamReader mInputStreamReader = new InputStreamReader(mFileInputStream, StandardCharsets.UTF_8);
        BufferedReader mBufferedReader = new BufferedReader(mInputStreamReader);
        //[ti:我不知道爱是什么] ar:艾怡良]
        while ((Lrc_data = mBufferedReader.readLine()) != null) {
            // ti:我不知道爱是什么]
            Lrc_data = Lrc_data.replace("[", "");
            // ti:我不知道爱是什么@
            Lrc_data = Lrc_data.replace("]", "@");
            // [00:00.00, 我爱歌词网 www.5ilrc.com] split是去掉@并在此处用逗号分隔成两个字符串。最后放到一个数组里。
            String[] splitLrc_data = Lrc_data.split("@");
            if (splitLrc_data.length > 1) {
                LyricContent mLyricContent = new LyricContent();
                // [00:00.00, 我爱歌词网 www.5ilrc.com],取数组里面的第2个数据作为歌词。
                mLyricContent.setLyric(splitLrc_data[1]);
                // 取数组里面的第1个数据，放到TimeStr里都转成秒为单位后出来作为歌词时间。0 400 9490 12490 15860 15860 35560
                int LyricTime = TimeStr(splitLrc_data[0]);
                mLyricContent.setLyricTime(LyricTime);
                lyricList.add(mLyricContent);
            }
        }
        mBufferedReader.close();
        mInputStreamReader.close();
    }

    /**
     * 时间转换
     *
     * @param timeStr 00:40.57
     * @return
     */
    private int TimeStr(String timeStr) {
        //00.40.57
        timeStr = timeStr.replace(":", ".");
        //00@40@57
        timeStr = timeStr.replace(".", "@");
        //[00, 40, 57]
        String[] timeData = timeStr.split("@");
        //数组里的第1个数据是分0
        int minute = Integer.parseInt(timeData[0]);
        //数组里的第2个数据是秒40
        int second = Integer.parseInt(timeData[1]);
        //数组里的第3个数据是秒57
        int millisecond = Integer.parseInt(timeData[2]);
        //40000+570=40570
        int currentTime = (minute * 60 + second) * 1000 + millisecond * 10;
        return currentTime;
    }

    public List<LyricContent> getLyricContent() {
        return lyricList;
    }
}
