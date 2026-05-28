const LealoneRpcClient = (function() {
const L = {
    sockjsUrl: "/_lealone_sockjs_",
    serviceUrl: "/service",
    serviceNames: [],
    serviceProxyObjects: [],
    cache: [],

    getService(serviceName) {
        if(this.serviceProxyObjects[serviceName] != undefined)
            return this.serviceProxyObjects[serviceName];
        var object = { serviceName: serviceName };
        this.serviceNames.push(serviceName);
        this.serviceProxyObjects[serviceName] = this.getProxyObject(object);
        return this.serviceProxyObjects[serviceName];
    },

    getProxyObject(object) {
        let that = this;
        let missingMethod = this.missingMethod;
        const proxyObject = new Proxy(object, {
            get(object, property) {
                if (Reflect.has(object, property)) {
                    return Reflect.get(object, property);
                } else {
                    return (...args) => Reflect.apply(missingMethod, that, [object, property, ...args]);
                }
            }
        });
        return proxyObject;
    },

    missingMethod(object, method, ...args) {
        this.call(object, method, ...args);
    },

    toUnderscore(str) {
        return str.replace(/([A-Z])/g, function(v) { return '_' + v.toLowerCase(); });
    },

    toCamel(str) {
        return str.replace(/\_(\w)/g, function(all, letter){
            return letter.toUpperCase();
        });
    },
    
    getKey(serviceName, methodName) {
        return serviceName + "." + this.toUnderscore(methodName);
    },

    call(object, methodName) {
        var methodArgs = [];
        var key = this.getKey(object.serviceName, methodName);
        this.cache[key] = {};
        var length = arguments.length;
        if(typeof arguments[length - 1] == 'function') {
            this.cache[key]["callback"] = arguments[length - 1];
            length--;
        }
        if(length > 2) {
            for(var j = 2; j < length; j++) {
                if(arguments[j].onServiceException) {
                    this.cache[key]["onServiceException"] = arguments[j].onServiceException;
                    this.cache[key]["serviceObject"] = arguments[j];
                    continue;
                }
                methodArgs.push(arguments[j]);
            }
        }
        this.sendRequest(object.serviceName, methodName, methodArgs);
    },

    call4(serviceContext, service, methodName, arguments) {
        if(service.hooks != undefined) { 
            let hook = service.hooks[this.toCamel(methodName)];
            if(hook != undefined) {
                let beforeHook = hook["before"];
                if(beforeHook != undefined) {
                    let ret = beforeHook.apply(serviceContext, arguments);
                    if(ret === false)
                        return
                }
            }
        }

        var methodArgs = [];
        var key = this.getKey(service.serviceName, methodName);
        this.cache[key] = {};
        this.cache[key]["onServiceException"] = serviceContext.onServiceException;
        this.cache[key]["serviceObject"] = serviceContext;
        this.cache[key]["hooks"] = service.hooks;

        var argumentCount = arguments.length;
        if(typeof arguments[argumentCount - 1] == 'function') {
            this.cache[key]["callback"] = arguments[argumentCount - 1];
            argumentCount--;
        }
        // 过滤掉事件对象
        if(argumentCount > 0 && arguments[argumentCount - 1].defaultPrevented != undefined) {
            argumentCount--;
        }
        var names = service.methodInfo[methodName];
        var columnCount = names.length;
        var length = columnCount > argumentCount ? argumentCount : columnCount;
        var columnIndex = 0;
        if(serviceContext.gid == lealone.page && methodName == lealone.methodName) {
            length = lealone.params.length;
            for(var i = 0; i < length; i++) { 
                methodArgs.push(lealone.params[i]);
                columnIndex++;
            }
        }
        else if(argumentCount > 0) {
            for(var i = 0; i < length; i++) { 
                methodArgs.push(arguments[i]);
                columnIndex++;
            }
        }
        if(columnIndex < columnCount) { 
            for(var i = columnIndex; i < columnCount; i++) {
                // 存在相应字段时才加
                if(serviceContext[names[i]] != undefined)
                    methodArgs.push(serviceContext[names[i]]);
                else
                    methodArgs.push('');
            }
        }
        this.sendRequest(service.serviceName, methodName, methodArgs);
    },

    callService(serviceContext, serviceName, methodName, methodArgs) {
        var service = this.serviceProxyObjects[serviceName];
        this.call4(serviceContext, service, methodName, methodArgs);
    },

    loadServices(callback) {
        var object = { serviceName: "lealone_system_service" };
        var systemService = this.getProxyObject(object);
        systemService.loadServices(this.serviceNames.join(","), services => {
            for(var i = 0; i < services.length; i++) {
                var serviceInfo = services[i];
                var service = this.getService(serviceInfo.serviceName);
                var parameterNames = [];
                service.methodInfo = {};
                var funBody = "return {"
                for(var m = 0; m < serviceInfo.serviceMethods.length; m++) {
                    var serviceMethodInfo = serviceInfo.serviceMethods[m];
                    funBody += serviceMethodInfo.methodName + "(){ Lealone.callService(this, '" 
                             + serviceInfo.serviceName + "', '"+ serviceMethodInfo.methodName + "', arguments)},";
                    
                    service.methodInfo[serviceMethodInfo.methodName] = serviceMethodInfo.parameterNames;
                    for(var p = 0; p < serviceMethodInfo.parameterNames.length; p++) {
                        parameterNames.push(serviceMethodInfo.parameterNames[p]);
                    }
                }
                funBody += " }";
                var fun = new Function(funBody);
                service.parameterNames = parameterNames;
                service.methods = fun();
                for(var m in service.methods) {
                    service[m] = service.methods[m];
                }
            }
            if(callback != undefined)
                callback(services);
        });
    },

    initSockJS() {
        var that = this;
        var sockjs = new SockJS(this.sockjsUrl); // {"transports":"xhr_streaming"}
        that.sockjs = sockjs;
        sockjs.onopen = function() {
            that.sockjsReady = true; 
            if(that.penddingMsgs) {
                for(var i = 0; i < that.penddingMsgs.length; i++) {
                    sockjs.send(that.penddingMsgs[i]);
                }
                that.penddingMsgs = [];
            }
        };
        sockjs.onmessage = function(e) {
            that.handleResponse(e);
        };
        sockjs.onclose = function() {
            console.log("SockJS close");
        };
    },

    sendRequest(serviceName, methodName, methodArgs) {
        if(window.axios != undefined) {
            var underscoreMethodName = this.toUnderscore(methodName);
            var url = this.serviceUrl + "/" + serviceName + "/" + underscoreMethodName;
            axios.post(url, { methodArgs : JSON.stringify(methodArgs) })
            .then(response => {
                this.handleResponse(response);
            })
            .catch(error => {
                this.handleError(serviceName, methodName, error.message);
            });
        } else if(window.SockJS != undefined) {
            if(!this.sockjs) {
                this.initSockJS();
            }
            // 格式: type;serviceName.methodName;[arg1,arg2,...argn]
            var msg = "1;" + serviceName + "." + methodName + ";" + JSON.stringify(methodArgs);
            if(this.sockjsReady)
                this.sockjs.send(msg);
            else {
                if(!this.penddingMsgs) {
                    this.penddingMsgs = [];
                } 
                this.penddingMsgs.push(msg);
            }
        } else {
            this.handleError(serviceName, methodName, "axios or sockjs not found");
        }
    },

    handleResponse(response) {
        var a = [];
        if(Array.isArray(response.data))
            a = response.data;
        else
            a = JSON.parse(response.data);
        var type = a[0];
        var serviceAndMethodName = a[1].split(".");
        var serviceName = serviceAndMethodName[0];
        var methodName = serviceAndMethodName[1];
        var service = this.cache[a[1]];
        if(!service) {
            console.log("not found service name: "+ serviceName);
            return;
        }
        var result = a[2];
        switch(type) {
            case 2: // 正常返回
                // 如果有回调就执行它
                if(service["callback"]) {
                    try {
                        result = JSON.parse(result); // 尝试转换成json对象
                    } catch(e) {
                    }
                    service["callback"](result);
                }
                else if(service["serviceObject"]) {
                    try {
                        var serviceObject = service["serviceObject"];
                        var isJson = true;
                        try {
                            result = JSON.parse(result); // 尝试转换成json对象
                        } catch(e) {
                            isJson = false;
                        }

                        let hooks = service["hooks"];
                        if(hooks != undefined) { 
                            let hook = hooks[this.toCamel(methodName)];
                            if(hook != undefined) {
                                let handleHook = hook["handle"];
                                //手动处理结果
                                if(handleHook != undefined) {
                                    handleHook.call(serviceObject, result);
                                    return;
                                }
                            }
                        }
                        //自动关联字段
                        if(isJson) {
                            var isInitMethod = serviceObject.services != undefined && serviceObject.services[1] === true;
                            for(var key in result) {
                                if(isInitMethod) {
                                    if(serviceObject.$data)
                                        serviceObject.$data[key] = result[key];
                                    else
                                        serviceObject[key] = result[key];
                                }
                                else if(serviceObject[key] != undefined) {
                                    serviceObject[key] = result[key];
                                }
                            }
                        }
                        if(hooks != undefined) { 
                            let hook = hooks[this.toCamel(methodName)];
                            if(hook != undefined) {
                                let afterHook = hook["after"];
                                if(afterHook != undefined) {
                                    afterHook.call(serviceObject, result);
                                }
                            }
                        } 
                    } catch(e) {
                        console.log(e);
                    } 
                }
                break;
            case 3: // error
                var msg = "failed to call service: " + a[1] + ", backend error: " + result;
                this.handleError(serviceName, methodName, msg);
                break;
            default:
                var msg = "unknown response type: " + type + ", response data: " + response.data;
                this.handleError(serviceName, methodName, msg);
        }
    },

    handleError(serviceName, methodName, msg) {
        var key = this.getKey(serviceName, methodName);
        var service = this.cache[key];
        if(!service) {
            console.error(msg);
        } else {
            if(service["onServiceException"]) 
                service["onServiceException"](msg);
            else {
                let hooks = service["hooks"];
                if(hooks != undefined) { 
                    let hook = hooks[this.toCamel(methodName)];
                    if(hook != undefined) {
                        let errorHook = hook["error"];
                        if(errorHook != undefined) {
                            errorHook.call(service["serviceObject"], msg);
                            return;
                        }
                    }
                } 
                console.error(msg)
            }
        }
    },
};

return {
    setSockjsUrl: function(url) { L.sockjsUrl = url },
    setServiceUrl: function(url) { L.serviceUrl = url },
    getService: function() { return L.getService.apply(L, arguments) }, 
    loadServices: function() { return L.loadServices.apply(L, arguments) }, 
    callService: function() { return L.callService.apply(L, arguments) }, 
};
})();

if(!window.lealone) window.lealone = LealoneRpcClient;
