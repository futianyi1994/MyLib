package com.bracks.utils.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.UriUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * good programmer.
 *
 * @date : 2019-02-26 下午 03:46
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :文件相关工具类
 */
public class FileUtils {

    private static final String TAG = "FileUtils";
    public static final int CHOOSE_FILE_CODE = 1001;

    /**
     * configured android:authorities in AndroidManifest (https://developer.android.com/reference/android/support/v4/content/FileProvider)
     */
    public static final String AUTHORITY = "YOUR_AUTHORITY.provider";

    private FileUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /********************************************自定义*********************************************/

    /**
     * 按修改时间排序
     *
     * @param files 未排序的文件集合
     * @return 排序后的文件集合
     */
    public static List<File> fileSortWithLastModified(List<File> files) {
        Collections.sort(files, new Comparator<File>() {
            @Override
            public int compare(File file, File newFile) {
                if (file.lastModified() < newFile.lastModified()) {
                    return 1;
                } else if (file.lastModified() == newFile.lastModified()) {
                    return 0;
                } else {
                    return -1;
                }
            }
        });
        return files;
    }

    /**
     * 扫描指定文件,发送广播通知更新数据库
     *
     * @param ctx      上下文
     * @param filePath 文件路径
     */
    public static void scanFileAsync(Context ctx, String filePath) {
        scanFileAsync(ctx, new File(filePath));
    }

    /**
     * 扫描指定目录,发送广播通知更新数据库
     *
     * @param ctx  上下文
     * @param file 文件
     */
    public static void scanFileAsync(Context ctx, File file) {
        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        scanIntent.setData(UriUtils.file2Uri(file));
        ctx.sendBroadcast(scanIntent);
    }

    /**
     * 扫描指定目录,发送广播通知更新数据库
     *
     * @param ctx 上下文
     * @param dir 目录
     */
    public static void scanDirAsync(Context ctx, String dir) {
        scanDirAsync(ctx, new File(dir));
    }

    /**
     * @param ctx  上下文
     * @param file 文件
     */
    public static void scanDirAsync(Context ctx, File file) {
        Intent scanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_DIR");
        scanIntent.setData(UriUtils.file2Uri(file));
        ctx.sendBroadcast(scanIntent);
    }

    /*************************************************调用系统文件管理器*******************************************************/

    /**
     * Creates View intent for given file
     *
     * @param file
     * @return The intent for viewing file
     */
    public static Intent getViewIntent(Context context, File file) {
        //Uri uri = Uri.fromFile(file);
        Uri uri = FileProvider.getUriForFile(context, AUTHORITY, file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String url = file.toString();
        if (url.contains(".doc")
                || url.contains(".docx")) {
            // Word document
            intent.setDataAndType(uri, "application/msword");
        } else if (url.contains(".pdf")) {
            // PDF file
            intent.setDataAndType(uri, "application/pdf");
        } else if (url.contains(".ppt")
                || url.contains(".pptx")) {
            // Powerpoint file
            intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        } else if (url.contains(".xls")
                || url.contains(".xlsx")) {
            // Excel file
            intent.setDataAndType(uri, "application/vnd.ms-excel");
        } else if (url.contains(".zip")
                || url.contains(".rar")) {
            // WAV audio file
            intent.setDataAndType(uri, "application/x-wav");
        } else if (url.contains(".rtf")) {
            // RTF file
            intent.setDataAndType(uri, "application/rtf");
        } else if (url.contains(".wav")
                || url.contains(".mp3")) {
            // WAV audio file
            intent.setDataAndType(uri, "audio/x-wav");
        } else if (url.contains(".gif")) {
            // GIF file
            intent.setDataAndType(uri, "image/gif");
        } else if (url.contains(".jpg")
                || url.contains(".jpeg")
                || url.contains(".png")) {
            // JPG file
            intent.setDataAndType(uri, "image/jpeg");
        } else if (url.contains(".txt")) {
            // Text file
            intent.setDataAndType(uri, "text/plain");
        } else if (url.contains(".3gp")
                || url.contains(".mpg")
                || url.contains(".mpeg")
                || url.contains(".mpe")
                || url.contains(".mp4")
                || url.contains(".avi")) {
            // Video files
            intent.setDataAndType(uri, "video/*");
        } else {
            intent.setDataAndType(uri, "*/*");
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        return intent;
    }

    /**
     * Get the Intent for selecting content to be used in an Intent Chooser.
     *
     * @return The intent for opening a file with Intent.createChooser()
     */
    public static Intent createGetContentIntent() {
        // Implicitly allow the user to select a particular kind of data
        final Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        // The MIME data type filter
        intent.setType("*/*");
        // Only return URIs that can be opened with ContentResolver
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        return intent;
    }

    /**
     * 调用系统文件管理器
     *
     * @param activity
     */
    public static void chooseFile(Activity activity) {
        try {
            ActivityUtils.startActivityForResult(activity, Intent.createChooser(createGetContentIntent(), "Choose File"), CHOOSE_FILE_CODE);
        } catch (ActivityNotFoundException e) {
            ToastUtils.showLong("文件管理器未安装");
        }
    }

    /**
     * Convert Uri into File, if possible.
     *
     * @return file A local file that the Uri was pointing to, or null if the
     * Uri is unsupported or pointed to a remote resource.
     * @author paulburke
     * @see #getPath(Context, Uri)
     */
    public static File getFile(Context context, Uri uri) {
        if (uri != null) {
            String path = getPath(context, uri);
            if (isLocal(path)) {
                return new File(path);
            }
        }
        return null;
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.<br>
     * <br>
     * Callers should check whether the path is local before assuming it
     * represents a local file.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     * @see #isLocal(String)
     * @see #getFile(Context, Uri)
     */
    public static String getPath(final Context context, final Uri uri) {
        String absolutePath = getLocalPath(context, uri);
        return absolutePath != null ? absolutePath : uri.toString();
    }

    @SuppressLint("NewApi")
    private static String getLocalPath(final Context context, final Uri uri) {
        Log.d(TAG + "UriInfo:",
                "\tAuthority: " + uri.getAuthority() +
                        "\n, Fragment: " + uri.getFragment() +
                        "\n, Port: " + uri.getPort() +
                        "\n, Query: " + uri.getQuery() +
                        "\n, Scheme: " + uri.getScheme() +
                        "\n, Host: " + uri.getHost() +
                        "\n, Segments: " + uri.getPathSegments().toString()
        );
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // LocalStorageProvider
            if (isLocalStorageDocument(uri)) {
                // The path is the id
                return DocumentsContract.getDocumentId(uri);
            }
            // ExternalStorageProvider
            else if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                } else if ("home".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/documents/" + split[1];
                } else {
                    return null;
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                if (id != null && id.startsWith("raw:")) {
                    return id.substring(4);
                }
                String[] contentUriPrefixesToTry = new String[]{
                        "content://downloads/public_downloads",
                        "content://downloads/my_downloads",
                        "content://downloads/all_downloads"
                };
                for (String contentUriPrefix : contentUriPrefixesToTry) {
                    Uri contentUri = ContentUris.withAppendedId(Uri.parse(contentUriPrefix), Long.valueOf(id));
                    try {
                        String path = getDataColumn(context, contentUri, null, null);
                        if (path != null) {
                            return path;
                        }
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                }
                //如果使用ContentResolver路径不能被检索,需要使用流复制文件访问缓存
                //根据uri获取文件名
                String fileName = getFileNameFromUri(context, uri);
                //创建缓存目录"documents"
                File cacheDir = new File(context.getCacheDir(), "documents");
                com.blankj.utilcode.util.FileUtils.createOrExistsDir(cacheDir);
                //生成文件
                File file = new File(cacheDir, fileName);
                com.blankj.utilcode.util.FileUtils.createFileByDeleteOldFile(file);
                //测试模式打印缓存文件
                for (File cacheFile : cacheDir.listFiles()) {
                    Log.d(TAG, "cacheFile = " + cacheFile.getPath());
                }
                String destPath = file.getAbsolutePath();
                try {
                    //写入文件
                    FileIOUtils.writeFileFromIS(destPath, context.getContentResolver().openInputStream(uri));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                return destPath;
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };
                return getDataColumn(context, contentUri, selection, selectionArgs);
            } else if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
                return getDataColumn(context, uri, null, null);
            } else {
                return null;
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri)) {
                return uri.getLastPathSegment();
            }
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        } else {
            return null;
        }
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        final String column = MediaStore.Files.FileColumns.DATA;
        final String[] projection = {column};
        try (Cursor cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                if (AppUtils.isAppDebug()) {
                    DatabaseUtils.dumpCursor(cursor);
                }
                final int columnIndex = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(columnIndex);
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    private static String getFileNameFromUri(Context context, Uri uri) {
        String mimeType = context.getContentResolver().getType(uri);
        String filename = null;
        if (mimeType == null) {
            String path = getPath(context, uri);
            if (path == null) {
                int index = uri.toString().lastIndexOf('/');
                filename = uri.toString().substring(index + 1);
            } else {
                File file = new File(path);
                filename = file.getName();
            }
        } else {
            Cursor returnCursor = context.getContentResolver().query(uri, null, null, null, null);
            if (returnCursor != null) {
                int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                returnCursor.moveToFirst();
                filename = returnCursor.getString(nameIndex);
                returnCursor.close();
            }
        }
        return filename;
    }

    /**
     * @return Whether the URI is a local one.
     */
    public static boolean isLocal(String url) {
        return url != null && !url.startsWith("http://") && !url.startsWith("https://");
    }

    /**
     * @return True if Uri is a MediaStore Uri.
     * @author paulburke
     */
    public static boolean isMediaUri(Uri uri) {
        return "media".equalsIgnoreCase(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is local.
     */
    public static boolean isLocalStorageDocument(Uri uri) {
        return AUTHORITY.equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }


    /***************************************************************************************************************/


}
