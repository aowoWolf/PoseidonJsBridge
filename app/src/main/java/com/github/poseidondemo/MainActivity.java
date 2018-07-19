package com.github.poseidondemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;

import com.github.customhandler.CustomConfig;
import com.github.poseidon.jsbridge.BridgeWebView;

public class MainActivity extends AppCompatActivity {
    private BridgeWebView bridgeWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {

        bridgeWebView = findViewById(R.id.webView);

        //设置点击返回键可返回上一级
        bridgeWebView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                //按下返回键且抬起
                if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_BACK && keyEvent.getAction() == KeyEvent.ACTION_UP) {
                    if (bridgeWebView.canGoBack()) {
                        bridgeWebView.goBack();
                        return true;
                    }
                }
                return false;
            }
        });

        //注册自己定义的handler，从而扩展标准浏览器的能力
        bridgeWebView.registerHandler(new CustomConfig());
        bridgeWebView.loadUrl("file:///android_asset/demo.html");
    }
}
