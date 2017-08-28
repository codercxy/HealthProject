package com.nju.android.health.bswk;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by Administrator on 2016/2/23.
 */
public class SteamTools {
    public static byte[] getBytes(InputStream is) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while((len=is.read(buffer)) != -1){
            baos.write(buffer, 0, len);
        }
        baos.flush();
        baos.close();
        return baos.toByteArray();
    }
}
