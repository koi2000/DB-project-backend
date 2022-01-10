package qd.cs.koi.database.websocket.utils;

public class StrUtil {
    public static void emptyStr(String... str){
        for(String s : str){
            if(!isEmptyStr(s)) throw new NullPointerException("Text Object to Null");
        }
    }
    public static boolean isEmptyStr(String str){
        return str != null && str.length() > 0;
    }
}
