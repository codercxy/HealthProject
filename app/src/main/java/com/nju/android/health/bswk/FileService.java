package com.nju.android.health.bswk;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/2/23.
 */
public class FileService {
    private Context context;

    public FileService(Context context){
        this.context = context;
    }
    /**
     * 把用户名和密码保存到手机ROM
     * @param password 输入要保存的密码
     * @param username 要保存的用户名
     * @param filename 保存到哪个文件
     * @return
     */
    public boolean saveToRom(String password, String username, int usertype, String filename) throws Exception {
        //以私有的方式打开一个文件
        FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
        String result = username+":"+password+":"+usertype;
        fos.write(result.getBytes());
        fos.flush();
        fos.close();
        return true;
    }
    public Map<String,String> getUserInfo(String filename) throws Exception {
        System.out.println("filename........."+filename);
        File file = new File("data/data/com.xiaheng.bswk/files/"+filename);
        FileInputStream fis = new FileInputStream(file);
        //以上的两句代码也可以通过以下的代码实现：
        //FileInputStream fis = context.openFileInput(filename);
        byte[] data = SteamTools.getBytes(fis);
        String result = new String(data);
        String results[] = result.split(":");
        Map<String,String> map = new HashMap<String,String>();
        map.put("mem_account", results[0]);
        map.put("mem_token", results[1]);
        map.put("type",results[2]);
        fis.close();
        return map;
    }
}
