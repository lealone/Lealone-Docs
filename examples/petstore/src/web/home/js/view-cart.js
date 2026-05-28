const viewCart = { 
    data() {
        return {
            cartId: localStorage.currentUser ? localStorage.currentUser : ""
        }
    }
}
const viewCartHooks = {
    removeItem: { after() { this.getItems() } }
}

ViewCartService.hooks = viewCartHooks;
