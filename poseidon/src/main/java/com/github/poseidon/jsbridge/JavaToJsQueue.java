package com.github.poseidon.jsbridge;

import android.app.Activity;
import android.text.TextUtils;

import java.util.LinkedList;

/**
 * Created by ShuXin on 2018/7/13 17:32
 */
public class JavaToJsQueue {

    private BridgeWebView webView;
    private LinkedList<Message> queue = new LinkedList<>();

    public JavaToJsQueue(BridgeWebView webView) {
        this.webView = webView;
    }

    public void addActionResult(ActionResult actionResult, String callbackID, boolean isFirst) {
        //callbackID说明js端没有回调函数，不用向js端发送数据
        if (TextUtils.isEmpty(callbackID)) {
            return;
        }

        String data = actionResult.getJSONString();
        Message msg = new Message();
        msg.setResponseId(callbackID);
        msg.setResponseData(data);

        enequeueMessage(msg, isFirst);
    }

    public void enequeueMessage(Message msg, boolean isFirst) {
        if (isFirst) {
            queue.addFirst(msg);
        } else {
            queue.add(msg);
        }
    }

    public void dispatchData2Js(final Message m) {
        ((Activity) webView.getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String js = m.encodeAsJs();
                webView.loadUrl(js);
            }
        });
    }

    private void dispatchData2Js() {
        ((Activity) webView.getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int size = queue.size();
                for (int i = 0; i < size; i++) {
                    String js = queue.removeFirst().encodeAsJs();
                    webView.loadUrl(js);
                }
            }
        });
    }

    public void flushQueue() {
        if (!queue.isEmpty()) {
            dispatchData2Js();
        }
    }

}