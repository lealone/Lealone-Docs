create config lealone (
    base_dir: './data',
    protocol_server_engine: (
        name: 'http',
        enabled: true,
        port: 8080,
        web_root: './web',
        environment: 'dev',
        jdbc_url: 'jdbc:lealone:embed:petstore?user=root',

        redirect_filter: '/:/home/index.html, /user:/user/index.html,'|| 
                         '/store:/store/index.html, /redirect.do:@location',
                         
        file_upload_filter: '/service/store_service/add_product',
        
        filters: 'com.lealone.services.jwt.JwtFilter: /service/user_service/update,'
                                              || '/service/user_service/get_user,'
                                              || '/service/view_cart_service/*'
    )
)
