const userHooks = {
    getUser: {
        before() {
            if(!lealone.currentUser) {
                lealone.route('user', 'login');
                return false;
            }
            this.userId = lealone.currentUser;
        }
    },
    register: {
        before() {
            if(this.password != this.password2) {
                this.errorMessage = "密码验证 必须和 密码 相同";
                return false;
            }
        },
        after(response) {
            if(!lealone.currentUser) {
                lealone.currentUser = this.userId;
                localStorage.currentUser = this.userId;
            }
            location.href = "/";
        }
    },
    update: {
        before() { this.account.userId = lealone.currentUser },
        after() { lealone.route('user', 'account') }
    },
    login: {
        after(data) {
            lealone.currentUser = data.userId;
            localStorage.currentUser = data.userId;
            location.href = "/";
        },
        error(msg) { this.errorMessage = "用户名或密码不正确,请重新输入" }
    }
}

UserService.hooks = userHooks;
