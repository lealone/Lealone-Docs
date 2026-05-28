const storeAddProduct = { 
    data() {
        return {
            product: {categoryid: "", productid: "", name: "", descn: "", logo: ""},
            file: "",
            errorMessage: ""
        }
    },
    methods: {
        getFile(event) {
            this.file = event.target.files[0];
        },
        addProduct() {
            if(!this.product.productid) {
                this.errorMessage = "必须填写产品ID";
                return;
            }
            var formData = new FormData();
			this.product.logo = this.file.name;
            formData.append('product', JSON.stringify(this.product));
            formData.append('logo_file', this.file);
            // 不需要手工增加Content-Type头，axios会自动删除
            // var config = {
            // headers: {
            // 'Content-Type': 'multipart/form-data'
            // }
            // };
            // axios.post(PetStore.StoreService + '/add_product', formData, config)
            const CancelToken = axios.CancelToken;
            const source = CancelToken.source();
            axios.post(PetStore.StoreService + '/add_product', formData, { cancelToken: source.token })
            .then(response => {
                lealone.route("store", "category-list");
            })
            .catch(error => {
                if (axios.isCancel(error)) {
                    console.log('Request canceled', error.message)
                } else {
                    console.log(error);
                    this.errorMessage = "添加产品失败";
                }
            });
            //source.cancel("请求已取消");
        }
    },
    mounted() {
        this.product.categoryid = lealone.params.categoryid;
    }
}
