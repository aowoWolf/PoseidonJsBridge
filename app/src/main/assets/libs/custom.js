function Custom(){

    //常量,当前handler的服务名
    this.SERVICE_NAME = 'customservice'

    this.test = function(param1,param2,successCallback,errorCallback){
        window.WebViewJavascriptBridge.exec( this.SERVICE_NAME,"test",[param1,param2],successCallback,errorCallback );
    }

}
