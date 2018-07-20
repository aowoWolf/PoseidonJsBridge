function System(){

    //常量,当前handler的服务名
    this.HANDLER_NAME = 'System'

    this.vibrate = function(delay,successCallback,errorCallback){
        window.WebViewJavascriptBridge.exec( this.HANDLER_NAME,"vibrate",[delay],successCallback,errorCallback );
    }

}
