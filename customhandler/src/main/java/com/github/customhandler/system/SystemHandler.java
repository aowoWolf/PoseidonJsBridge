package com.github.customhandler.system;

import android.content.Context;
import android.os.Vibrator;

import com.github.poseidon.jsbridge.BridgeWebView;
import com.github.poseidon.jsbridge.CallBack;
import com.github.poseidon.jsbridge.PoseidonHandler;
import com.github.poseidon.jsbridge.PoseidonInterface;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by ShuXin on 2018/7/20 9:38
 */
public class SystemHandler extends PoseidonHandler {

    private static final String ACTION_VIBRATE = "vibrate";


    private Vibrator vibrator;
    private Context context;

    @Override
    protected void initialize(BridgeWebView webview, PoseidonInterface poseidon) {
        super.initialize(webview, poseidon);
        //初始化一些需要的配置
        context = webview.getContext();
        vibrator = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
    }

    /**
     * @param action
     * @param args
     * @param callback
     * @return true表示插件正常工作，false的话，js端将不能收到正确的反馈信息，只能收到"Invaid action"的信息
     * @throws JSONException
     */
    @Override
    public boolean execute(String action, JSONArray args, CallBack callback) throws JSONException {
        if (ACTION_VIBRATE.equals(action)) {
            int delay = args.getInt(0);
            vibrator.vibrate(delay);
            callback.success(false);
        } else if (false) {
        }

        return super.execute(action, args, callback);
    }
}
