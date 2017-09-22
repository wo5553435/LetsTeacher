package com.example.sinner.letsteacher.utils.file;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.SparseArray;

import com.pigcms.library.utils.FileUtil;
import com.pigcms.library.utils.Logs;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sinner on 2017-06-29.
 * mail: wo5553435@163.com
 * github: https://github.com/wo5553435
 */

public class FileUtils {
    protected File path;
    protected SparseArray<String> filelist;
    private List<String> lstFile = new ArrayList<String>();  //结果 List
    private String rootfilepath;
    searchFileListener listener;
    private static FileUtils instance;


    public static FileUtils getInstance() {
        if (instance == null) instance = new FileUtils();
        return instance;
    }

    private void FileUtils() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            path = Environment.getExternalStorageDirectory();
        } else {
            Logs.e("bad search ", "maybe no permission!");
        }
    }


    public void getAllFiles(File... filepath) {
        File rootpath = path;
        if (filepath.length != 0) {//默认
            // 遍历接收一个文件路径，然后把文件子目录中的所有文件遍历并输出来
            rootpath = filepath[0];
        }
        int i = 0;
        File files[] = rootpath.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    getAllFiles(f);
                } else {
                    filelist.append(i, f.getAbsolutePath());
                    i++;
                }
            }
        }
        Logs.e("总共有-" + filelist.size() + "get文件", "---");
    }

    /**
     * 查找固定结尾的文件
     *
     * @param Path        根目录
     * @param IsIterative 是否查找子目录
     * @param Extension   拓展名
     * @return
     */
    private void GetFile(String Path, boolean IsIterative, @NonNull String... Extension)  //搜索目录，扩展名，是否进入子文件夹
    {
        File[] files = new File(Path).listFiles();

        for (int i = 0; i < files.length; i++) {
            File f = files[i];
            if (f.isFile()) {

                for (int j = 0; j < Extension.length; j++) {
                    if (f.getPath().substring(f.getPath().length() - Extension[j].length()).equals(Extension[j])) {  //判断扩展名
                        lstFile.add(f.getPath());
                        if (listener != null) listener.searchone(f.getPath());
                    }
                    if (!IsIterative)
                        break;
                }
            } else if (f.isDirectory() && f.getPath().indexOf("/.") == -1) { //忽略点文件（隐藏文件/文件夹）
                GetFile(f.getPath(), IsIterative, Extension);
            }

        }
    }

    public List<String> GetFiles(String Path, @NonNull String... Extension)  //搜索目录，扩展名，是否进入子文件夹
    {
        lstFile.clear();
        GetFile(Path, true, Extension);
        if (listener != null) listener.searchover();
        return lstFile;
    }

    public FileUtils setListener(searchFileListener listener) {
        this.listener = listener;
        return instance;
    }

    public void MoveFile(String filepath, String dirpath) {//移动文件到指定文件夹下
        try {
            File afile = new File(filepath);
            if (afile.renameTo(new File(dirpath + afile.getName()))) {
                System.out.println("File is moved successful!");
            } else {
                System.out.println("File is failed to move!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 直接复制文件夹
     *
     * @param oldPath
     * @param newPath
     */
    public void copyFile(String oldPath, String newPath) {
        FileInputStream fis;
        FileOutputStream fos;
        BufferedOutputStream bufos = null;
        BufferedInputStream bufis = null;
        try {
            fis = new FileInputStream(oldPath);
            bufis = new BufferedInputStream(fis);
            fos = new FileOutputStream(newPath);
            bufos = new BufferedOutputStream(fos);
            int len = 0;
            while ((len = bufis.read()) != -1) {
                bufos.write(len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufis.close();
                bufos.close();
                Logs.e("关闭文件流出问题", "---");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public interface searchFileListener {
        void searchone(String filpath);

        void searchover();
    }
}

