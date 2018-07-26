# Poseidon

[English document](https://github.com/aowoWolf/PoseidonJsBridge/blob/master/README-en.md)  

<p align="center">
   <img alt="GitPoint" title="GitPoint" src="https://github.com/aowoWolf/PoseidonJsBridge/blob/master/readmeRes/poseidon_Logo.jpg?raw=true"><br>
   <img alt="GitPoint" title="GitPoint" src="https://github.com/aowoWolf/PoseidonJsBridge/blob/master/readmeRes/logo_name.png?raw=true" width="650">
 </p>


Poseidon 是一座连接 Java 和 JavaScript 的桥梁。它为两者之间的双向调用提供了更简单又更高效的方法。

## 功能

- 实现 JavaScript 与 Java 之间双向调用
- 能行车、能走人 :yum:

## 演示
- 演示设备：魅蓝note2
- Android 版本：5.1
  因为只是屏幕截图，所以只能看到设备的信息，看不到震动和闪光灯的效果。当然你也可以下载这个[Demo.apk](https://github.com/aowoWolf/PoseidonJsBridge/releases/download/V1.00.00_20180723/PoseidonDemo.apk).

  ![image](https://github.com/aowoWolf/PoseidonJsBridge/blob/1ae300163ea63fa2ad9d0d18a6538d846899e12c/readmeRes/poseidon_systemtest.gif?raw=true)  
  以下图片主要演示 JavaScript 调用 Java 的时候，Java 层可以返回**单条**信息也可以返回**若干条**消息，只要你想。
  ![image](https://github.com/aowoWolf/PoseidonJsBridge/blob/1ae300163ea63fa2ad9d0d18a6538d846899e12c/readmeRes/poseidon_customtest.gif?raw=true)


## 开始
如果你想使用 Poseidon,你可以把它当做一个依赖添加到你的项目中。

使用Maven：

``` xml
<dependency>
  <groupId>com.github.aowuWolf</groupId>
  <artifactId>poseidon</artifactId>
  <version>1.0.0</version>
  <type>pom</type>
</dependency>
```
或者你可以使用 Gradle：

``` groovy
compile 'com.github.aowuWolf:poseidon:1.0.0'
```
## 使用

### JavaScript 调用 Java

在 Java 端分三步配置，如下描述：

1. 初始化 WebView

```java
BridgeWevView bridgeWebView = (BridgeWebView)findViewById(R.id.webView);

	//注册自己定义的handler
	bridgeWebView.registerHandler(new CustomConfig());
	bridgeWebView.registerHandler(new SystemConfig());

	String url = "file:///android_asset/index.html";
	bridgeWebView.loadUrl(url);
```

2. 自定义 Handler
   Handler 在此处的意思可以理解成基于 poseidon 开发的一系列插件，每个插件有自己的服务(server)，然后每个服务又有着各自的行为(action)。从而扩展标准浏览器的能力.
   那么如何定义自己的 Hanaler 呢？细节可以参考 customhandler 模块中的 CustomHandler 或 SystemHandler。
``` java
public class SystemHandler extends PoseidonHandler {
    public static final String TAG = SystemHandler.class.getSimpleName();

    private static final String ACTION_VIBRATE = "vibrate";
    private static final String ACTION_LIGHT = "light";
    private static final String ACTION_DEVICEINFO = "deviceinfo";

	private Vibrator vibrator;
    
    //此方法主要初始化一些简单的配置，此方法会在第一次加载的时候执行，往后不再执行
    @Override
    protected void initialize(BridgeWebView webview, PoseidonInterface poseidon) {
        super.initialize(webview, poseidon);
        //初始化振动的配置
        Context context = webview.getContext();
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
            //切换闪光灯
            return true;
        } else if (ACTION_DEVICEINFO.equals(action)) {
           //获取设备信息
            return true;
        }
        return super.execute(action, args, callback);
    }
}
```

3. Handler配置

有了 Handler 还不够，还需要把自己若干个 Handler 设置到 HandlerConfig 中，然后 webview 调用 `registerHandler` 方法，这样 BridgeWebView 才有能使用 Handler 里的功能。
```java
public class SystemConfig implements HandlerConfig{
    /**
     * map.put("service",Handler.class)
     * 此service对应js端的service，需要保持一致
     *
     * @return
     */
    @Override
    public HashMap<String, Class<? extends PoseidonHandler>> getServiceMap() {
        return new HashMap<String, Class<? extends PoseidonHandler>>() {
            {
            	//这里的key就是handler中的service，如果还有其他的服务，可以继续put
                put("System", SystemHandler.class);
            }
        };
    }
}
```
到此，Java 端配置就基本完成了。

JavaScript 端相对比较简单，如下描述：

JavaScript 只要调用 `WebViewJavascriptBridge.exec(service, action, args, success, fail) ` 方法就可以调用自己`Handler`里的逻辑了。
``` javascript
    /**
     * @param service	此处的System就是上面SystemConfig中的service
     * @param action SystemHandler中的action名字
     * @param args args默认数组，传递多个参数，请和Handler中execute方法的args对应起来
     * @param sunccess 成功的回调
     * @param fail 失败的回调
     */
        window.WebViewJavascriptBridge.exec( "System","vibrate",[delay],successCallback,errorCallback );
```
不过，我更推荐将一个 service 的所有 action 的接口放到一个文件，使用起来简单、开发效率也会变高。当然前端人员也可以对这些 JavaScript 的接口使用 promise 进行封装，以下是 [system.js](https://github.com/aowoWolf/PoseidonJsBridge/blob/1ae300163ea63fa2ad9d0d18a6538d846899e12c/app/src/main/assets/libs/system.js):
``` javascript
function System(){

    //常量,当前handler的服务名
    this.SERVICE_NAME = 'System'

    this.vibrate = function(delay,successCallback,errorCallback){
        window.WebViewJavascriptBridge.exec( this.SERVICE_NAME,"vibrate",[delay],successCallback,errorCallback );
    }

    this.light = function(successCallback,errorCallback){
        window.WebViewJavascriptBridge.exec( this.SERVICE_NAME,"light",[],successCallback,errorCallback );
    }

    this.deviceinfo = function(successCallback,errorCallback){
        window.WebViewJavascriptBridge.exec( this.SERVICE_NAME,"deviceinfo",[],successCallback,errorCallback );
    }
}
```
### Java调用JavaScript
Java调用 JavaScript  主要通过 PoseidonHandler.java 里的三个 `dispatchedJsEvent` 方法
1. Java调用一个名为"my_handler"的事件，并且需要回调函数返回 JavaScript 返回的信息
``` java
//第一个参数是js端注册的handler名，第二个参数是要发送的数据，第三个参数是回调函数，接收从js返回的数据
dispatchedJSEvent("my_handler", "Data from Java>>>", new ResponseCallback() {
	@Override
	public void receiveDataFromJs(String data) {
		//处理js反馈回来的数据
	}
}
```
然后Js层可以注册一个`my_handler`的事件，如下：
```javascript
window.WebViewJavascriptBridge.registerHandler("my_handler", function(data, responseCallback) {
	console.log(data);//Data from Java>>>
	responseCallback("hello world");
});
```
2. Java调用`my_handler`事件，不需要回调函数  
  那么Java端只需要调用`dispatchedJSEvent(String handlerName, String data)`这个函数就可以了。
3. Java 直接发送数据
```javascript
//js端只需要注册一个无名的handler即可，
poseidonBridge.registerHandler(function(data) {
	console.log(data);
});
```
Java只需要调用`dispatchedJSEvent(String data)`便可。
## 计划
 - 将```webview.registerHandler(new HanderlConfig());```换成其他的方式，至于什么方式还没想好。
 - 返回到js的消息队里里的信息，统一放到js端处理

## 彩蛋
demo还有个仿微信打飞机的小游戏。这不是重点，重点是我添加了一个坠机会振动的功能。其实也很简单，就是调用 SystemHandler 的振动 action。[点我下载](https://github.com/aowoWolf/PoseidonJsBridge/releases/download/V1.00.00_20180723/PoseidonDemo.apk)
