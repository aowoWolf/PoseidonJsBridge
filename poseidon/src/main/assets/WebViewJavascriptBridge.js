//notation: js file can only use this kind of comments
//since comments will cause error when use in webview.loadurl,
//comments will be remove by java use regexp
(function() {
    if (window.WebViewJavascriptBridge) {
        return;
    }

    var CUSTOM_PROTOCOL_SCHEME = 'yy://poseidon/';

    var bizMessagingIframe;
    var receiveMessageQueue = [];
    var messageHandlers = {};
    var responseCallbacks = {};

    var uniqueId = 1;

    var callbackStatus = {
        OK:1,
        ERROR:0
    };


    //创建消息体队列iframe
    function _createQueueReadyIframe4biz(doc) {
        bizMessagingIframe = doc.createElement('iframe');
        bizMessagingIframe.style.display = 'none';
        doc.documentElement.appendChild(bizMessagingIframe);
    }

    function registerHandler(handlerName, handler) {
        if(typeof handlerName == 'function'){
            if (WebViewJavascriptBridge._messageHandler) {
                throw new Error('WebViewJavascriptBridge.init called twice');
            }
            WebViewJavascriptBridge._messageHandler = handlerName;
        }else{
            messageHandlers[handlerName] = handler;
        }
    }
    // 调用线程
    function exec(service, action, args, success, fail) {
        _doSend({
            handlerName: service + '_' + action,
            data: args
        }, {success:success, fail:fail});
    }

    function _doSend(message, responseCallback) {
        if (responseCallback) {
            var callbackId = 'cb_' + (uniqueId++) + '_' + new Date().getTime();
            responseCallbacks[callbackId] = responseCallback;
            message.callbackId = callbackId;
        }

        var messageQueueString = JSON.stringify(message);
        bizMessagingIframe.src = CUSTOM_PROTOCOL_SCHEME + encodeURIComponent(messageQueueString);
    }

    //提供给native使用,
    function _dispatchMessageFromNative(messageJSON) {
        setTimeout(function() {
            var message = JSON.parse(messageJSON);
            var responseCallback;
            //java call finished, now need to call js callback function
            if (message.responseId) {
                responseCallback = responseCallbacks[message.responseId];
                if (!responseCallback) {
                    return;
                }

                var responseBody = JSON.parse(message.responseData);
                var status = responseBody.status;
                var keepCallback = responseBody.keepCallback;

                if(status == callbackStatus.OK){
                    responseCallback.success(responseBody.message);
                }else{
                    responseCallback.fail(responseBody.message);
                }
                if(!keepCallback) {
                    delete responseCallbacks[message.responseId];
                }
            } else {
                //直接发送
                if (message.callbackId) {
                    var callbackResponseId = message.callbackId;
                    responseCallback = function(responseData) {
                        _doSend({
                            responseId: callbackResponseId,
                            responseData: responseData
                        });
                    };
                }

                var handler = WebViewJavascriptBridge._messageHandler;
                if (message.handlerName) {
                    handler = messageHandlers[message.handlerName];
                }
                //查找指定handler
                try {
                    handler(message.data, responseCallback);
                } catch (exception) {
                    if (typeof console != 'undefined') {
                        console.log("WebViewJavascriptBridge: WARNING: javascript handler threw.", message, exception);
                    }
                }
            }
        });
    }

    //提供给native调用,receiveMessageQueue 在会在页面加载完后赋值为null,所以
    function _handleMessageFromNative(messageJSON) {
        console.log(messageJSON);
        //消息数组，循环分发
//        if (receiveMessageQueue) {
//            receiveMessageQueue.push(messageJSON);
//        }
        _dispatchMessageFromNative(messageJSON);
       
    }

    var WebViewJavascriptBridge = window.WebViewJavascriptBridge = {
        registerHandler: registerHandler,
        exec: exec,
        _handleMessageFromNative: _handleMessageFromNative
    };

    var doc = document;
    _createQueueReadyIframe4biz(doc);
    var readyEvent = doc.createEvent('Events');
    readyEvent.initEvent('WebViewJavascriptBridgeReady');
    readyEvent.bridge = WebViewJavascriptBridge;
    doc.dispatchEvent(readyEvent);
})();
