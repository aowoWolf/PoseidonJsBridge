# Poseidon

The English document is still under construction.Please read the [Chinese document](https://github.com/aowoWolf/PoseidonJsBridge)  

<p align="center">
   <img  src="https://github.com/aowoWolf/PoseidonJsBridge/blob/master/readmeRes/poseidon_Logo.jpg?raw=true"><br>
   <img  src="https://github.com/aowoWolf/PoseidonJsBridge/blob/master/readmeRes/logo_name.png?raw=true" width="550">
 </p>

[![Build Status](https://travis-ci.org/aowoWolf/PoseidonJsBridge.svg?branch=master)](https://travis-ci.org/aowoWolf/PoseidonJsBridge)

Poseidon is a bridge connecting Java and JavaScript. It provides a simpler and more efficient methods for two-way calls between the two.

##Function

- Implement two-way calls between JavaScript and Java.
- can drive and walk ​:yum:​

## Demonstration
- Demonstration equipment: Meizu note2
- Android version: 5.1
  Because it is just screenshot,So you can only see information about the device, without the effects of the vibration and the flash.Of course, you can download this[Demo.apk](https://github.com/aowoWolf/PoseidonJsBridge/releases/download/V1.00.00_20180723/PoseidonDemo.apk).

  ![image](https://github.com/aowoWolf/PoseidonJsBridge/blob/1ae300163ea63fa2ad9d0d18a6538d846899e12c/readmeRes/poseidon_systemtest.gif?raw=true)  
  The following picture shows how JavaScript calls Java, the Java layer can return either a single message or several messages as long as you want.
  ![image](https://github.com/aowoWolf/PoseidonJsBridge/blob/1ae300163ea63fa2ad9d0d18a6538d846899e12c/readmeRes/poseidon_customtest.gif?raw=true)


## Begin
If you want to use Poseidon, you can add it to your project as a dependency.

Use Maven:

``` xml
<dependency>
  <groupId>com.github.aowuWolf</groupId>
  <artifactId>poseidon</artifactId>
  <version>1.0.0</version>
  <type>pom</type>
</dependency>
```
Or you can use Gradle:

``` groovy
compile 'com.github.aowuWolf:poseidon:1.0.0'
```
## make use of

### JavaScript calls Java

There are three steps to configure on the Java side, as described below：

1. Initialize WebView

```java
BridgeWevView bridgeWebView = (BridgeWebView)findViewById(R.id.webView);

	//Register your own defined handler
	bridgeWebView.registerHandler(new CustomConfig());
	bridgeWebView.registerHandler(new SystemConfig());

	String url = "file:///android_asset/index.html";
	bridgeWebView.loadUrl(url);
```

2. Custom Handler
   What Handler means here can be understood as a series of plug-ins based on poseidon.Each plug-in has its own server, and each server has its own action, thereby extend the capabilities of standard browsers.
   So how do you define your own Hanaler? Refer to the CustomHandler or SystemHandler in the customhandler module for details.
``` java
public class SystemHandler extends PoseidonHandler {
    public static final String TAG = SystemHandler.class.getSimpleName();

    private static final String ACTION_VIBRATE = "vibrate";
    private static final String ACTION_LIGHT = "light";
    private static final String ACTION_DEVICEINFO = "deviceinfo";

	private Vibrator vibrator;
    
    //This method mainly initializes some simple configurations, it will be executed the first time it was loaded and will no longer be executed.
    @Override
    protected void initialize(BridgeWebView webview, PoseidonInterface poseidon) {
        super.initialize(webview, poseidon);
        //Initial vibration configuration
        Context context = webview.getContext();
        vibrator = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
    }

    /**
     * @param action
     * @param args
     * @param callback
     * @return True indicates that the plug-in works properly,if it's false, the JS side will not receive the correct feedback information, only receive the information of "Invaid action"
     * @throws JSONException
     */
    @Override
    public boolean execute(String action, JSONArray args, CallBack callback) throws JSONException {
        if (ACTION_VIBRATE.equals(action)) {//vibration
            int delay = args.getInt(0);
            vibrator.vibrate(delay);
            callback.success(false);
            return true;
        } else if (ACTION_LIGHT.equals(action)) {//flash lamp
            //switch flash
            return true;
        } else if (ACTION_DEVICEINFO.equals(action)) {
           //get device information
            return true;
        }
        return super.execute(action, args, callback);
    }
}
```

3.Handler configuration

With Handler is not enough, you need to set several of your own Handler into the HandlerConfig, and then webview calls the registerHandler method, so that BridgeWebView can use the functionality in Handler.
```java
public class SystemConfig implements HandlerConfig{
    /**
     * map.put("service",Handler.class)
     * This service needs to be consistent with the service on the js side
     *
     * @return
     */
    @Override
    public HashMap<String, Class<? extends PoseidonHandler>> getServiceMap() {
        return new HashMap<String, Class<? extends PoseidonHandler>>() {
            {
            	//The key here is the service in handler. If there are other services, you can continue put
                put("System", SystemHandler.class);
            }
        };
    }
}
```
Now the Java side configuration is almost complete.

The JavaScript side is relatively simple, as described below：

JavaScript can invoke logic in its own Handler just by calling the WebViewJavascriptBridge.exec(service, action, args, success, fail) method.
``` javascript
    /**
     * @param service	The System here is the service in the SystemConfig above
     * @param action Action’s name in SystemHandler
     * @param args The args default array, passing multiple parameters, corresponds to the args of the execute method in Handler
     * @param sunccess Successful callback
     * @param fail Failed callback
     */
        window.WebViewJavascriptBridge.exec( "System","vibrate",[delay],successCallback,errorCallback );
```
However, I prefer to put all of the action interfaces of a service in one file, which is easy to use and more efficient in development. Of course, front-end personnel can also use promise to encapsulate these JavaScript interfaces.The following are [system.js](https://github.com/aowoWolf/PoseidonJsBridge/blob/1ae300163ea63fa2ad9d0d18a6538d846899e12c/app/src/main/assets/libs/system.js):
``` javascript
function System(){

    //Constant, the service name of the current handler
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
###Java calls JavaScript
Java calls JavaScript mainly through three dispatchedJsEvent methods in PoseidonHandler.java.
1. Java calls an event called "my_handler" and requires a callback function to return the information returned by JavaScript.
``` java
//The first parameter is the name of handler registered on the js side, the second parameter is the data to be sent, and the third parameter is the callback function that receives the data returned from the js.
dispatchedJSEvent("my_handler", "Data from Java>>>", new ResponseCallback() {
	@Override
	public void receiveDataFromJs(String data) {
		//Processing data returned by js
	}
}
```
Then the Js layer can register a my_handler event, as follows:
```javascript
window.WebViewJavascriptBridge.registerHandler("my_handler", function(data, responseCallback) {
	console.log(data);//Data from Java>>>
	responseCallback("hello world");
});
```
2. When Java calls the my_handler event without the need for a callback function.  
  Then the Java side just needs to call the dispatchedJSEvent(String handlerName, String data) function.
3. Java can send data directly
```javascript
//The js side only needs to register a nameless handler，
poseidonBridge.registerHandler(function(data) {
	console.log(data);
});
```
Java only needs to call dispatchedJSEvent(String data).

## workflow of Poseidon
<p align="center">
   <img  src="https://github.com/aowoWolf/PoseidonJsBridge/blob/1d035b4deaff904bb3eadbf86cc8fc7282f965c3/readmeRes/flowchart.png?raw=true">
 </p>

## Plan
 - Change the webview.registerHandler(new HanderlConfig()); to something else, but i haven't figured out what way to do it.
 - Information returned to the js message team, unified into the js side processing.

## Surprise
The Demo has a mini game of playing airplanes like the game in wechat. That's not the point. The point is, I added a feature that vibrates when it crashes.In fact, it is also very simple,Just calling the vibrational action of SystemHandler.[Click me to download](https://github.com/aowoWolf/PoseidonJsBridge/releases/download/V1.00.00_20180723/PoseidonDemo.apk)
