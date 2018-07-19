package com.github.poseidon.jsbridge;

import android.os.Debug;
import android.util.Log;

import org.json.JSONException;

/**
 * Created by ShuXin on 2018/7/12 18:00
 */
public class HandlerManager {

    private static final String TAG = HandlerManager.class.getSimpleName();

    private static final int SLOW_EXEC_WARNING_THRESHOLD = Debug.isDebuggerConnected() ? 60 : 16;
    private BridgeWebView webView;

    public HandlerManager(BridgeWebView webView) {
        this.webView = webView;
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
                webView.insertHeadActionResutlt(cr,callbackID);
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
        try {
            Class<? extends PoseidonHandler> c = webView.getServiceHelper().getMap().get(service);
            if (PoseidonHandler.class.isAssignableFrom(c)) {
                ret = c.newInstance();
                //poseidonHandler initialize
                ret.privateInitialize(webView, webView.poseidon);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Error adding handler " + service + ".");
        }
        return ret;
    }

}
