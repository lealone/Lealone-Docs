#!/bin/sh
mvn package -Dmaven.test.skip=true
java -cp petstore-main/target/petstore-1.0.0.jar com.lealone.plugins.service.template.TemplateCompiler -webRoot petstore-web/web -targetDir petstore-web/target
mvn assembly:assembly -Dmaven.test.skip=true
