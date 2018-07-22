        function appendSuccessLog(data){
                document.getElementById("logsuccess").innerHTML += data+"<br>";
        }
        function appendErrorLog(data){
                document.getElementById("logerror").innerHTML += data+"<br>";
        }
        function appendOtherLog(data){
                document.getElementById("logother").innerHTML += data+"<br>";
        }


        function testClick() {
            var customservice = new Custom();
            customservice.test(
                "hello world",
                "####    "+new Date().getTime()+"   ####",
                function (success){
                    console.log("success");
                    console.log(success);
                    appendSuccessLog(success)
                },
                function (error){
                    console.log("error");
                    console.log(error);
                    appendErrorLog(error)
                }
            );
        }

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
            bridge.registerHandler(function(data, responseCallback) {
                appendOtherLog(data)
                if (responseCallback) {
                    var responseData = ">>>>js new Date = "+ new Date;
                    responseCallback(responseData);
                }
            });

            bridge.registerHandler("onJavaCallJsEvent", function(data, responseCallback) {
                appendOtherLog(data)
                var responseData = ">>>>js new Date = "+ new Date;
                responseCallback(responseData);
            });

        })

