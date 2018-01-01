package com.jaky.utils;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

/**
 * Created by Jack on 2017/12/31.
 */

public class RawResUtils {

    public static String readContent(Context context, int rawResourceId) {
        BufferedReader breader = null;
        InputStream is = null;

        try {
            is = context.getResources().openRawResource(rawResourceId);
            breader = new BufferedReader(new InputStreamReader(is));
            StringBuilder total = new StringBuilder();
            String line = null;

            while((line = breader.readLine()) != null) {
                total.append(line);
            }

            String var6 = total.toString();
            return var6;
        } catch (Exception var10) {
            ;
        } finally {
            closeQuietly(breader);
            closeQuietly(is);
        }

        return null;
    }

    public static Map<String, List<Integer>> integerMapFromRawResource(Context context, int rawResourceId) {
        String content = readContent(context, rawResourceId);
        return (Map) JSON.parseObject(content, new TypeReference<Map<String, List<Integer>>>() {
        }, new Feature[0]);
    }

    public static void closeQuietly(Closeable closeable) {
        try {
            if(closeable != null) {
                closeable.close();
            }
        } catch (IOException var2) {
            var2.printStackTrace();
        }

    }
}
