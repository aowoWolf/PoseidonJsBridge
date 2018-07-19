package com.github.customhandler;

import android.widget.Toast;

import com.github.poseidon.jsbridge.CallBack;
import com.github.poseidon.jsbridge.PoseidonBridge;
import com.github.poseidon.jsbridge.PoseidonHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ShuXin on 2018/7/16 16:49
 */
public class CustomHandler extends PoseidonHandler {

    public static final String TAG = CustomHandler.class.getSimpleName();
    private static String ACTION_TEST = "test";

    @Override
    public boolean execute(String action, JSONArray args, final CallBack callback) throws JSONException {
        if (ACTION_TEST.equals(action)) {
            String str1 = args.getString(0);
            final String str2 = args.getString(1);
//            String str3 = args.getString(4);
            Toast.makeText(poseidon.getActivity(), str1 + "_______" + str2, Toast.LENGTH_SHORT).show();
            long time = System.currentTimeMillis();
            poseidon.getThreadPool().execute(new Runnable() {
                public void run() {
                    for (int i = 0; i < 20; i++) {
                        callback.error("Js call Java>>>_____" + i+str2 + "<br>", i < 10);
//            callback.error("Js call Java>>>Java:this message come from customHandler_____" + time,true);
//                        dispatchedJSEvent("onJavaCallJsEvent", "Java call Js>>>Java:CustomHandler_____" + i, new PoseidonBridge.ResponseCallback() {
                        dispatchedJSEvent("onJavaCallJsEvent", new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒 E ").format(new Date()) + "_______" + i+"<br>", new PoseidonBridge.ResponseCallback() {
                            @Override
                            public void receiveDataFromJs(String data) {
                                Toast.makeText(poseidon.getActivity(), data, Toast.LENGTH_SHORT).show();
                            }
                        });
//                        dispatchedJSEvent(new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒 E ").format(new Date())+"______"+i+"<br>");
                    }
                }
            });
            return true;
        }

        return false;
    }
}
