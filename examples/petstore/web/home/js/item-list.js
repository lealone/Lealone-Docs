const itemlist = { 
    data() { return { itemAdded: false } },
    methods: {
        addCartItem(itemId) {
            if(!localStorage.currentUser) { //需要登录
                lealone.route('user', 'login');
                return false;
            }
            var cart = lealone.get('view-cart');
            cart.addItem(cart.cartId, itemId, data=>{
                this.itemAdded = true;
            });
        }
    }
}