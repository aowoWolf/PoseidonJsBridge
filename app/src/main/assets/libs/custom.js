function Custom(){

    //常量,当前handler的服务名
    this.SERVICE_NAME = 'customservice'

    this.jstojava = function(msg,isSuccess,successCallback,errorCallback){
        window.WebViewJavascriptBridge.exec( this.SERVICE_NAME,"jstojava",[msg,isSuccess],successCallback,errorCallback );
    }
    //multiplejstojava: 表示java可以向js发送任意次数的回调结果，场景：调用native的接口，但是需要native层返回多个回调信息。
    this.multiplejstojava = function(msg,count,isSuccess,successCallback,errorCallback){
        window.WebViewJavascriptBridge.exec( this.SERVICE_NAME,"multiplejstojava",[msg,count,isSuccess],successCallback,errorCallback );
    }

    this.javatojs = function(handlerName,successCallback,errorCallback){
        window.WebViewJavascriptBridge.exec( this.SERVICE_NAME,"javatojs",[handlerName],successCallback,errorCallback );
    }
}
