        function appendSuccessLog(data){
                document.getElementById("logsuccess").innerHTML += data+"<br>";
        }
        function appendErrorLog(data){
                document.getElementById("logerror").innerHTML += data+"<br>";
        }
        function appendOtherLog(data){
                document.getElementById("logother").innerHTML += data+"<br>";
        }

        var poseidonBridge ;
        function connectWebViewJavascriptBridge(callback) {
            if (window.WebViewJavascriptBridge) {
                callback(WebViewJavascriptBridge)
            } else {
                document.addEventListener(
                    'WebViewJavascriptBridgeReady'
                    , function() {
                        callback(WebViewJavascriptBridge)
                    },
                    false
                );
            }
        }

        connectWebViewJavascriptBridge(function(bridge) {
             poseidonBridge = bridge;
//            bridge.registerHandler(function(data, responseCallback) {
//                appendOtherLog(data)
//                if (responseCallback) {
//                    var responseData = ">>>>js new Date = "+ new Date;
//                    responseCallback(responseData);
//                }
//            });
//
//            bridge.registerHandler("onJavaCallJsEvent", function(data, responseCallback) {
//                appendOtherLog(data)
//                var responseData = ">>>>js new Date = "+ new Date;
//                responseCallback(responseData);
//            });
        })

        function jsToJava() {
            var msg = document.getElementById("jstojava").value;
            var isSuccess = document.getElementById("jstojava_check").checked;
            var customservice = new Custom();
            customservice.jstojava(
                msg,
                isSuccess,
                function (success){
                    appendSuccessLog(success)
                },
                function (error){
                    appendErrorLog(error)
                }
            );
        }

        function multipleJstoJava() {
            var msg = document.getElementById("multijstojava").value;
            var conut = document.getElementById("mulit_count").value;
            var isSuccess = document.getElementById("mulit_check").checked;
            var customservice = new Custom();
            customservice.multiplejstojava(
                msg,
                count,
                isSuccess,
                function (success){
                    appendSuccessLog(success)
                },
                function (error){
                    appendErrorLog(error)
                }
            );
        }

        function registerHandler() {
            var handlerName = document.getElementById("handlerName").value;
            var needCallback = document.getElementById("check1").checked;
            var needHandler = (handlerName == "" || handlerName == undefined || handlerName == null) ? false: true;
            if(needCallback){
                bridge.registerHandler(handlerName, function(data, responseCallback) {
                    appendOtherLog("needCallback"+data)
                    responseCallback("hello world");
                });
            }else{
                if(!needHandler){
                    bridge.registerHandler(function(data) {
                        appendOtherLog("not needHandlerName"+data)
                    });
                }else{
                    bridge.registerHandler(handlerName, function(data) {
                        appendOtherLog("need HandlerName but callback"+data)
                    });
                }
            }

            var customservice = new Custom();
            customservice.javatojs(
                handlerName,
                function (success){
                    appendSuccessLog(success)
                },
                function (error){
                    appendErrorLog(error)
                }
            );


        }
