const Lealone = {
    currentUser: localStorage.currentUser,
    screen: "" ,
    page: "", 
    params: {},
    methodName: "",

    route(screen, page, params, methodName) {
        var state = JSON.stringify(this);
        if(params){
            this.params = params;
        }
        this.methodName = methodName;
        // 当前page没有变化，但是参数可能变了，所以手工调用
        if(this.screen == screen && this.page == page) {
            var instance = this.get(page);
            if(Array.isArray(instance.$options.mounted) && typeof instance.$options.mounted[0] == 'function')
                return instance.$options.mounted[0].apply(instance);
            return;
        }
        if(this.screen != screen) {
            this.screen = screen;
            this.page = page;
            sessionStorage.page = page;
            sessionStorage.params = JSON.stringify(this.params);
            location.href = "/" + screen + "/index.html";
            return;
        }
        // 加两次，不然popstate有可能返回null，原因不明
        // window.history.pushState(state, page, "/" + this.screen + "/" + page);
        window.history.pushState(state, page, null);
        this.page = page;
        state = JSON.stringify(this);
        // window.history.pushState(state, page, "/" + this.screen + "/" + page);
        window.history.pushState(state, page, null);
        if(this.screen == screen) {
            var instance = this.find(page);
            if(instance) {
                if(methodName)
                    return instance[methodName].apply(instance, this.params);
                else if(Array.isArray(instance.$options.mounted) && typeof instance.$options.mounted[0] == 'function')
                    return instance.$options.mounted[0].apply(instance);
            }
        }
    },

    createVueApp(screen, defaultPage) {
        this.screen = screen;
        this.page = sessionStorage.page ? sessionStorage.page : defaultPage;
        this.params = sessionStorage.params ? JSON.parse(sessionStorage.params) : {};
        sessionStorage.removeItem("page");
        sessionStorage.removeItem("params");
        var options = {
            data() { return { } },
            computed: {
                currentComponent() {
                    return this.lealone.page;
                }
            },
            mounted() {
                var that = this;
                window.addEventListener('popstate', function(evt){
                    var state = JSON.parse(evt.state);
                    if(!state) return;
                    if(that.lealone.screen != state.screen) {
                        sessionStorage.page = state.page;
                        sessionStorage.params = JSON.stringify(state.params);
                        location.href = "/" + state.screen + "/index.html";
                        return;
                    }
                    that.lealone.currentUser = state.currentUser;
                    that.lealone.params = state.params;
                    that.lealone.screen = state.screen;
                    that.lealone.page = state.page;
                }, false);
            }
        };
        Vue.use(this);
        let lealone = this;
        var app = {
            options: options,
            mount(appName) {
                this.options.el = appName;
                Lealone.loadServices(() => {
                    new Vue(this.options);
                });
            },
            component(name, service, method) {
                lealone.component(name, service, method);
            },
            mixin(options) {
                Vue.mixin(options);
            }
        }
        return app;
    },

    component(name, service, method) { //service和method有可能都是undefined
        var mixins = [];
        var services = [];
        var bindMethod = "";
        var initMethod = "";
        if(typeof method == 'string') { //单一方法
            bindMethod = method;
        }
        else if(method != undefined && 
                (method.initMethod != undefined || method.bindMethod != undefined)) { //通过配置指定方法名
            bindMethod = method.bindMethod;
            initMethod = method.initMethod;
        } else {
            bindMethod = "*"; //默认是所有方法
        }
        services.push(bindMethod);
        services.push(initMethod);
        
        var serviceArray = [];
        if(Array.isArray(service)) {
            serviceArray = service;
        } else {
            if(service != undefined)
                serviceArray.push(service);
        }
        var len = serviceArray.length;
        for(var i = 0; i < len; i++){
            if(serviceArray[i].serviceName == undefined)
                mixins.push(serviceArray[i]);
            else
                services.push(serviceArray[i]);
        }
        Vue.component(name, {
            data() { return { services: services } },
            mixins: mixins, 
            props: {
                // 组件实例的全局唯一ID，默认是组件名
                gid: { type: String, default: name }
            },
            template: document.getElementById(name).innerHTML,
            beforeMount() {
                if(this._beforeMount_) return;
                var len = this.services.length;
                for(var i = 2; i < len; i++){
                    var service = this.services[i];
                    for(var m in service.methods) {
                        if(typeof service[m] == 'function' && 
                                (this.services[0] === "*" || m == this.services[0] || m == this.services[1])) {
                            let method = service[m];
                            let that = this;
                            var fun = function() {
                                method.apply(that, arguments);
                            }
                            this[m] = fun;
                            
                            var methodInfo = service.methodInfo[m];
                            for(var j = 0; j < methodInfo.length; j++){
                                if(this.$data && !this.$data[methodInfo[j]])
                                    this.$data[methodInfo[j]] = "";
                                if(!this[methodInfo[j]])
                                    this[methodInfo[j]] = "";
                            }
                            if(this.$data)
                                this.$data.errorMessage = "";
                            this.errorMessage = "";
                            if(this.services[1]) {
                                this._beforeRender = function(cb) {
                                    method.call(that, cb); 
                                }
                            }
                        }
                    }
                }
                this._beforeMount_ = true;
            }
        })
    },

    install(app, options) {
        var thisLealone = this;
        app.mixin({
            data() { return { lealone: thisLealone } },
            beforeMount() {
                if(this.getComponentInstance && this.lealone.get == undefined) {
                    let thisVm = this;
                    this.lealone.get = function() {
                       var instance = thisVm.getComponentInstance.apply(thisVm, arguments);
                       if(instance.external) {
                           for(var m in instance.$options.beforeMount) {
                               instance.$options.beforeMount[m].apply(instance);
                           }
                       }
                       return instance;
                    }
                    this.lealone.find = function() {
                        return thisVm.findComponentInstance.apply(thisVm, arguments);
                    }
                }
                window.lealone = this.lealone; // 这样组件在内部使用this.lealone和lealone都是一样的
            }
        });
    }
}

Lealone.setSockjsUrl = LealoneRpcClient.setSockjsUrl;
Lealone.getService = LealoneRpcClient.getService;
Lealone.loadServices = LealoneRpcClient.loadServices;
Lealone.callService = LealoneRpcClient.callService;
