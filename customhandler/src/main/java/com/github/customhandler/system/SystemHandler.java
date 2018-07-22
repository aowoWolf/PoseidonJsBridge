package com.github.customhandler.system;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Build;
import android.os.Vibrator;
import android.util.Log;

import com.github.poseidon.jsbridge.BridgeWebView;
import com.github.poseidon.jsbridge.CallBack;
import com.github.poseidon.jsbridge.PoseidonHandler;
import com.github.poseidon.jsbridge.PoseidonInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by ShuXin on 2018/7/20 9:38
 */
public class SystemHandler extends PoseidonHandler {
    public static final String TAG = SystemHandler.class.getSimpleName();

    private static final String ACTION_VIBRATE = "vibrate";
    private static final String ACTION_LIGHT = "light";
    private static final String ACTION_DEVICEINFO = "deviceinfo";


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
        } else if (ACTION_DEVICEINFO.equals(action)) {
            callback.success(getDeviceInfo(), false);
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

    public JSONObject getDeviceInfo() throws JSONException {
        JSONObject ret = new JSONObject();
        DeviceInfo deviceInfo = new DeviceInfo(context);
        ret.put("app的签名", deviceInfo.AppSignature());
        ret.put("app的名称", deviceInfo.AppName());
        ret.put("app的版本号", deviceInfo.VersionCode() + "");
        ret.put("app的版本号名", deviceInfo.VersionName());
        ret.put("app的包名", deviceInfo.PackgeName());
        ret.put("手机的IMEI号", deviceInfo.IMEI());
        ret.put("手机的IMSI", deviceInfo.IMSI());
        ret.put("手机的号码", deviceInfo.Num());
        ret.put("手机产品的序列号", deviceInfo.SN());
        ret.put("手机的sim号", deviceInfo.SIM());
        ret.put("手机的ID", deviceInfo.ID());
        ret.put("手机的mac地址", deviceInfo.MAC());
        ret.put("系统国家", deviceInfo.Country());
        ret.put("系统语言", deviceInfo.Language());
        ret.put("屏幕的高", deviceInfo.Height() + "");
        ret.put("屏幕的宽", deviceInfo.Width() + "");
        ret.put("系统版本名", Build.VERSION.RELEASE);
        ret.put("系统版本号", Build.VERSION.SDK_INT + "");
        ret.put("系统型号", Build.MODEL);
        ret.put("系统定制商", Build.BRAND);
        ret.put("系统的主板", Build.BOARD);
        ret.put("手机制造商", Build.PRODUCT);
        ret.put("系统2", Build.HOST);
        ret.put("系统3", Build.TIME + "    " + System.currentTimeMillis());
        ret.put("系统4", Build.USER);
        ret.put("系统硬件执照商", Build.MANUFACTURER);
        ret.put("builder类型", Build.MANUFACTURER);
        return ret;
    }
}
