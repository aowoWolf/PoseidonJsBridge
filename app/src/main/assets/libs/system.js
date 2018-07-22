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
