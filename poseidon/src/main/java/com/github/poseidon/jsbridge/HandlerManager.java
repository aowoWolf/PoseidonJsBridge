package com.github.poseidon.jsbridge;

import android.os.Debug;
import android.util.Log;

import org.json.JSONException;

import java.util.HashMap;

/**
 * Created by ShuXin on 2018/7/12 18:00
 */
public class HandlerManager {

    private static final String TAG = HandlerManager.class.getSimpleName();

    private static final int SLOW_EXEC_WARNING_THRESHOLD = Debug.isDebuggerConnected() ? 60 : 16;
    private BridgeWebView webView;
    private HashMap<String, PoseidonHandler> handlerMap;

    public HandlerManager(BridgeWebView webView) {
        this.webView = webView;
        handlerMap = new HashMap<>();
    }

    //bridgewebview.fluseMessage(callbackid exit)
    public void exec(String service, String action, String rawArgs, String callbackID) {
        CallBack callback = new CallBack(webView, callbackID);
        PoseidonHandler handler = getHandler(service);
        try {

            long handlerStartTime = System.currentTimeMillis();
            boolean wasValidAction = handler.execute(action, rawArgs, callback);
            long duration = System.currentTimeMillis() - handlerStartTime;

            if (duration > SLOW_EXEC_WARNING_THRESHOLD) {
                Log.w(TAG, "THREAD WARNING: exec() call to " + service + "." + action + " blocked the main thread for " + duration + "ms. Handler should use PoseidonInterface.getThreadPool().");
            }

            if (!wasValidAction) {
                ActionResult cr = new ActionResult(ActionResult.Status.INVALID_ACTION);
                //如果子handler的execute方法返回的是false话，执行以下代码会在队列头部添加一个invalid action的message，然后关闭js的接口回调函数
                webView.insertHeadActionResutlt(cr, callbackID);
            }
        } catch (JSONException e) {
            ActionResult cr = new ActionResult(ActionResult.Status.JSON_EXCEPTION);
            callback.sendActionResult(cr);
        } catch (Exception e) {
            Log.e(TAG, "Uncaught exception from handler", e);
            callback.error(e.getMessage(), false);
        }
    }

    private PoseidonHandler getHandler(String service) {
        PoseidonHandler ret = null;
        if (handlerMap.containsKey(service)) {
            ret = handlerMap.get(service);
        } else {
            try {
                Class<? extends PoseidonHandler> c = webView.getServiceHelper().getMap().get(service);
                if (PoseidonHandler.class.isAssignableFrom(c)) {
                    ret = c.newInstance();
                    handlerMap.put(service,ret);
                    //poseidonHandler initialize
                    ret.privateInitialize(webView, webView.poseidon);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "Error adding handler " + service + ".");
            }
        }
        return ret;
    }

}
