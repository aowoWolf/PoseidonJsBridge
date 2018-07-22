package com.github.customhandler.test;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.github.poseidon.jsbridge.CallBack;
import com.github.poseidon.jsbridge.PoseidonHandler;
import com.github.poseidon.jsbridge.ResponseCallback;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ShuXin on 2018/7/16 16:49
 */
public class CustomHandler extends PoseidonHandler {

    public static final String TAG = CustomHandler.class.getSimpleName();

    private static String ACTION_JSTOJAVA = "jstojava";
    private static String ACTION_MULTIJSTOJAVA = "multiplejstojava";
    private static String ACTION_JAVATOJS = "javatojs";

    @Override
    public boolean execute(String action, JSONArray args, final CallBack callback) throws JSONException {
        if (ACTION_JSTOJAVA.equals(action)) {
            String msg = args.getString(0);
            Log.d(TAG, "execute: jstojava"+msg);
            Toast.makeText(poseidon.getActivity(), msg, Toast.LENGTH_SHORT).show();
            boolean isSuccess = args.getBoolean(1);
            if (isSuccess) {
                callback.success("success msg: js call java ", false);
            } else {
                callback.error("error msg: js call java ", false);
            }
            return true;
        } else if (ACTION_MULTIJSTOJAVA.equals(action)) {
            String msg = args.getString(0);
            Log.d(TAG, "execute:multi  jstojava"+msg);
            Toast.makeText(poseidon.getActivity(), msg, Toast.LENGTH_SHORT).show();
            int count = args.getInt(1);
            boolean isSuccess = args.getBoolean(2);
            for (int i = 0; i < count; i++) {
                if (isSuccess) {
                    callback.success("success msg: multiple js call java " + "_______" + i, i < count - 1);
                } else {
                    callback.error("error msg: multiple js call java " + "_______" + i, i < count - 1);
                }
            }
            return true;
        } else if (ACTION_JAVATOJS.equals(action)) {
            String handlerName = args.getString(1);
            Log.d(TAG, "execute:java to js"+handlerName);
            if (TextUtils.isEmpty(handlerName)) {
                dispatchedJSEvent("java call js______" + new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒 E ").format(new Date()));
            } else {
                dispatchedJSEvent(handlerName, new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒 E ").format(new Date()), new ResponseCallback() {
                    @Override
                    public void receiveDataFromJs(String data) {
                        Toast.makeText(poseidon.getActivity(), data, Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return true;
        }

        return false;
    }
}
