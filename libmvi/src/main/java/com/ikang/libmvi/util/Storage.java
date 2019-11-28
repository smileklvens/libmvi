package com.ikang.libmvi.util;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;

import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static android.content.ContentValues.TAG;
import static android.os.Environment.getExternalStorageState;

/**
 * @author ikang-zhulk
 * @version 1.0.0
 * @describe 存储适配 android Q Scoped Storage
 * <p>
 * 参考
 * <p>
 * https://segmentfault.com/a/1190000019224425?utm_source=tag-newest
 * <p>
 * https://juejin.im/post/5d0b1739e51d4510a73280cc
 *
 * <p>
 * <p>
 * 在 target Q 时，进入存储沙箱模式）。虽然不可以直接去访问 SD 卡的根目录，但是还是可以直接去访问以你的应用的 package 命名的目录（无需权限）。
 * 如果想访问在你的 package name 目录以外的其他的地方的内容，你就必须使用 Mediastore 或者是存储访问框架 SAF。
 *
 * </p>
 */
public class Storage {

    /**
     * 内部存储
     *
     * <li>内部数据：/data/data/包名/XXX</li>
     * <p>
     * //此目录下将每一个APP的存储内容按照包名分类存放好。 比如:
     *
     * <li>1.data/data/包名/shared_prefs 存放该APP内的SP信息
     *
     * <li>2.data/data/包名/databases 存放该APP的数据库信息
     *
     * <li>3.data/data/包名/files 将APP的文件信息存放在files文件夹
     *
     * <li>/4.data/data/包名/cache 存放的是APP的缓存信息
     */
    public static class InternalStorage {

        /**
         * 获取在其中存储内部文件的文件系统目录的绝对路径。
         *
         * @return /data/data/包名/files
         */
        public static File getFilesDir(Context context) {
            return context.getFilesDir();
        }


        /**
         * @param name 文件名
         * @return 删除保存在内部存储的文件。
         */
        public static boolean deleteFile(Context context, String name) {
            return context.deleteFile(name);
        }


        /**
         * 将临时缓存文件保存到的内部目录。 使其占用的空间保持在合理的限制范围内（例如 1 MB）
         *
         * @return /data/data/包名/cache
         */
        public static File getCacheDir(Context context) {
            return context.getCacheDir();
        }

    }


    /**
     * 外部存储
     *
     * <li> /storage/sdcard/Android/data目录或者说/storage/emulated/0/Android/data包目录属于外部存储。
     *
     * <li>官方建议将App的数据存储存储在外部应用私有数据：/storage/emulated/0/Android/data/包名/XXX
     * 这样卸载App时数据会随之被系统清除，不会造成数据残留</li>
     *
     * <li>外部公有数据：/storage/emulated/0  注:在Q中失效</li>
     */
    public static class ExternalStorage {

        private static final String TAG = "ExternalStorage";


        /* Checks if external storage is available for read and write */
        public static boolean isStorageWritable() {
            String state = getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                return true;
            }
            return false;
        }

        /* Checks if external storage is available to at least read */
        public static boolean isStorageReadable() {
            String state = getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state) ||
                    Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
                return true;
            }
            return false;
        }

        /**
         * @return /storage/emulated/0/Android/data/包名/cache
         */
        public static File getCacheDir(Context context) {
            return context.getExternalCacheDir();
        }

        public static File createDirInCacheDir(Context context, String name) {
            File file = getCacheDir(context);
            if (file != null) {
                File newFile = new File(file, name);
                if (!newFile.exists()) {
                    if (!newFile.mkdirs()) {
                        Log.e(TAG, "getCacheDirWithName:  Directory not created");
                        return null;
                    }
                }
                return newFile;
            }
            return null;
        }

        public static String getCacheDirPath(Context context) {
            File file = getCacheDir(context);
            if (file != null) {
                return file.getPath();
            } else {
                return null;
            }
        }

        public static File[] getCacheDirs(Context context) {
            return ContextCompat.getExternalCacheDirs(context);
        }

        /**
         * 保存应用私有文件 官方建议
         *
         * @param type The type of files directory to return. May be {@code null} for the root of
         *             the files directory or one of the following constants for a subdirectory:
         *             {@link Environment#DIRECTORY_MUSIC}, {@link
         *             Environment#DIRECTORY_PODCASTS}, {@link Environment#DIRECTORY_RINGTONES},
         *             {@link Environment#DIRECTORY_ALARMS}, {@link
         *             Environment#DIRECTORY_NOTIFICATIONS}, {@link
         *             Environment#DIRECTORY_PICTURES}, or {@link
         *             Environment#DIRECTORY_MOVIES}.
         *             <p>
         *             如果您不需要特定的媒体目录，请传递 null 以接收应用私有目录的根目录。
         * @return /storage/emulated/0/Android/data/包名/files/{type}
         */
        public static File getFilesDir(Context context, String type) {
            return context.getExternalFilesDir(type);
        }

        public static File createDirInFilesDir(Context context, String type, String name) {
            File file = getFilesDir(context, type);
            if (file != null) {
                File newFile = new File(file, name);
                if (!newFile.exists()) {
                    if (!newFile.mkdirs()) {
                        Log.e(TAG, "getFilesDirWithName:  Directory not created");
                        return null;
                    }
                }
                return newFile;
            }
            return null;
        }

        public static String getFilesDirPath(Context context, String type) {
            File file = getFilesDir(context, type);
            if (file != null) {
                return file.getPath();
            } else {
                return null;
            }
        }

        public static File[] getFilesDirs(Context context, String type) {
            return ContextCompat.getExternalFilesDirs(context, type);
        }


        /**
         * 沙箱外  通过 Mediastore  API 对外部媒体文件，照片，视频，图片的读取或写入
         * <p>
         * 1、无需权限 用Mediastore 去提供内容。
         * 2、如果应用想要去查看其他应用为 Mediastore 提供的内容则须 READ_EXTERNAL_STORAGE权限
         * <p>
         * 采用 ContentResolver.query() 和 ContentResolver.openFileDescriptor()获取文件
         */

        /**
         * @describe 保存媒体Video 文件到公共区域
         */
        public static Uri insertVideoIntoMediaStore(Context context, String fileName, boolean isVideo) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.Video.Media.DISPLAY_NAME, fileName);
            contentValues.put(MediaStore.Video.Media.DATE_TAKEN, System.currentTimeMillis());
            contentValues.put(MediaStore.Video.Media.MIME_TYPE, isVideo ? "video/mp4" : "image/jepg");

            return context.getContentResolver().insert(isVideo ? MediaStore.Video.Media.EXTERNAL_CONTENT_URI : MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        }


        /**
         * 判断瑞是否存在
         *
         * @param context
         * @param uri
         * @return
         */
        public boolean isContentUriExists(Context context, Uri uri) {
            if (null == context) {
                return false;
            }
            ContentResolver cr = context.getContentResolver();
            try {
                AssetFileDescriptor afd = cr.openAssetFileDescriptor(uri, "r");
                if (null == afd) {
//                    iterator.remove();
                } else {
                    try {
                        afd.close();
                    } catch (IOException e) {
                    }
                }
            } catch (FileNotFoundException e) {
                return false;
            }

            return true;
        }


        public static void copy(File src, ParcelFileDescriptor parcelFileDescriptor) throws IOException {
            FileInputStream istream = new FileInputStream(src);
            try {
                FileOutputStream ostream = new FileOutputStream(parcelFileDescriptor.getFileDescriptor());
                try {
                    copy(istream, ostream);
                } finally {
                    ostream.close();
                }
            } finally {
                istream.close();
            }
        }

        public static void copy(ParcelFileDescriptor parcelFileDescriptor, File dst) throws IOException {
            FileInputStream istream = new FileInputStream(parcelFileDescriptor.getFileDescriptor());
            try {
                FileOutputStream ostream = new FileOutputStream(dst);
                try {
                    copy(istream, ostream);
                } finally {
                    ostream.close();
                }
            } finally {
                istream.close();
            }
        }


        public static void copy(InputStream ist, OutputStream ost) throws IOException {
            byte[] buffer = new byte[4096];
            int byteCount = 0;
            while ((byteCount = ist.read(buffer)) != -1) {  // 循环从输入流读取 buffer字节
                ost.write(buffer, 0, byteCount);        // 将读取的输入流写入到输出流
            }
        }


        /**
         *
         */
        public Bitmap getSharedStorageBitmapByUri(Context appContext,Uri captureUri) {
            ContentResolver cr = appContext.getContentResolver();
            ParcelFileDescriptor fd = null;
            try {
                fd = cr.openFileDescriptor(captureUri, "r");
                return BitmapFactory.decodeFileDescriptor(fd.getFileDescriptor());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    /**
     * 获取目录文件大小
     */
    public static long getDirSize(File dir) {
        if (dir == null) {
            return 0;
        }
        if (!dir.isDirectory()) {
            return 0;
        }
        long dirSize = 0;
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                dirSize += file.length();
            } else if (file.isDirectory()) {
                dirSize += file.length();
                dirSize += getDirSize(file); // 递归调用继续统计
            }
        }
        return dirSize;
    }

    public static long getCacheSize(Context context) {
        long internalCacheSize = getDirSize(InternalStorage.getCacheDir(context));
        long externalCacheSize = getDirSize(ExternalStorage.getCacheDir(context));
        return internalCacheSize + externalCacheSize;
    }


    /**
     * 清除缓存：
     * <p>
     * 1. 将内部数据下的cache包下的内容（/data/data/包名/cache/XXX）清除 。
     * <p>
     * 2. 将外部私有数据下的cache包（/storage/emulated/0/Android/data/包名/cache）清除，
     */
    public static void clearCache(Context context) {
        File mInternalCacheFile = InternalStorage.getCacheDir(context);
        deleteFolderFile(mInternalCacheFile, true);
        File mExternalCacheFile = ExternalStorage.getCacheDir(context);
        deleteFolderFile(mExternalCacheFile, true);
    }


    /**
     * 删除指定目录下文件及目录
     */
    public static void deleteFolderFile(File dir, boolean deleteThisPath) {
        try {
            if (dir.isDirectory()) {// 如果下面还有文件
                File files[] = dir.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFolderFile(files[i], true);
                }
            }
            if (deleteThisPath) {
                if (!dir.isDirectory()) {// 如果是文件，删除
                    dir.delete();
                } else {// 目录
                    if (dir.listFiles().length == 0) {// 目录下没有文件或者目录，删除
                        dir.delete();
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "deleteFolderFile: ", e);
            e.printStackTrace();
        }
    }

}
