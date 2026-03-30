create config lealone (
    base_dir: '../data',
    protocol_server_engine: (
        name: 'tomcat',
        enabled: true,
        port: 8080,
        web_root: '../web',
        jdbc_url: 'jdbc:lealone:embed:petstore?user=root',
        router: 'com.lealone.examples.petstore.web.PetStoreRouter'
    )
)
