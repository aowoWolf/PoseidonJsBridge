package com.github.poseidon.jsbridge;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebView;

@SuppressLint("SetJavaScriptEnabled")
public class BridgeWebView extends WebView {

    private ServiceProxy serviceProxy;
    private PoseidonBridge poseidonBridge;
    private JavaToJsQueue javaToJsQueue;
    protected PoseidonInterface poseidon;


    public BridgeWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BridgeWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public BridgeWebView(Context context) {
        super(context);
        init();
    }

    private void init() {
        this.setVerticalScrollBarEnabled(false);
        this.setHorizontalScrollBarEnabled(false);
        this.getSettings().setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        this.setWebViewClient(generateBridgeWebViewClient());
        //init HandlerManager
        poseidon = generatePoseidon();
        serviceProxy = new ServiceProxy();
        javaToJsQueue = new JavaToJsQueue(this);
        poseidonBridge = new PoseidonBridge(javaToJsQueue, this);

    }

    protected PoseidonInterface generatePoseidon() {
        return new PoseidonInterfaceImpl(((Activity) getContext()));
    }


    /**
     * 注册自己的handler，可以理解成把用户自己继承PoseindonHandler的类与WebView绑定起来
     *
     * @param handlerconfig 自定义的handler配置信息
     * @return true:绑定成功
     */
    public boolean registerHandler(HandlerConfig handlerconfig) {
        return serviceProxy.put(handlerconfig.getServiceMap());
    }

    ServiceProxy getServiceProxy() {
        return serviceProxy;
    }

    JavaToJsQueue getJavaToJsQueue() {
        return javaToJsQueue;
    }

    protected PoseidonBridge getPoseidonBridge() {
        return poseidonBridge;
    }

    void sendActionResult(ActionResult actionResult, String callbackID) {
        javaToJsQueue.addActionResult(actionResult, callbackID, false);
    }

    //当execute方法返回false，调用此方法在queue头部插入invalid action
    void insertHeadActionResutlt(ActionResult actionResult, String callbackID) {
        javaToJsQueue.addActionResult(actionResult, callbackID, true);
    }

    protected BridgeWebViewClient generateBridgeWebViewClient() {
        return new BridgeWebViewClient(this);
    }

    void callJshandler(String handlerName, String data, PoseidonBridge.ResponseFunction responseCallback) {
        poseidonBridge.callJshandler(handlerName, data, responseCallback);
    }

}
