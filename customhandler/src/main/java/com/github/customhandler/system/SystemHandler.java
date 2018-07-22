package com.github.customhandler.system;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Vibrator;
import android.util.Log;

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
    public static final String TAG = SystemHandler.class.getSimpleName();

    private static final String ACTION_VIBRATE = "vibrate";
    private static final String ACTION_LIGHT = "light";


    private Context context;
    private Vibrator vibrator;
    private Camera camera;
    private Parameters parameters;

    @Override
    protected void initialize(BridgeWebView webview, PoseidonInterface poseidon) {
        super.initialize(webview, poseidon);
        Log.d(TAG, "initialize: >>>>>>>>>>>>>");
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
        if (ACTION_VIBRATE.equals(action)) {//震动
            int delay = args.getInt(0);
            vibrator.vibrate(delay);
            callback.success(false);
            return true;
        } else if (ACTION_LIGHT.equals(action)) {//闪光灯
            if (camera == null) {
                try {
                    camera = Camera.open();
                } catch (Exception e) {
                    callback.error("Failed to open the camera, please refer to: " + e.getMessage(), false);
                }
            }
            //判断闪光灯是否开启
            if (isFlashlightOn()) {
                camera.getParameters().setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                camera.setParameters(camera.getParameters());
                camera.stopPreview();
                camera.release();
                camera = null;
                callback.success("close camera success", false);
            } else {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                camera.setParameters(parameters);
                camera.startPreview();
                callback.success("open camera success", false);
            }
            return true;
        }

        return super.execute(action, args, callback);
    }

    private boolean isFlashlightOn() {
        try {
            parameters = camera.getParameters();
            String flashMode = parameters.getFlashMode();
            if (flashMode.equals(Camera.Parameters.FLASH_MODE_TORCH)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
}
