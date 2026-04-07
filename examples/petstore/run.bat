@echo off

set JAVA_OPTS=-database petstore^
 -config sql/lealone.sql^
 sql/llm.sql sql/tables.sql sql/init-data.sql sql/services.sql

java -jar %LEALONE_HOME%/lealone-8.0.0-SNAPSHOT.jar %JAVA_OPTS%


REM java -cp %LEALONE_HOME%/lealone-8.0.0-SNAPSHOT.jar com.lealone.service.template.TemplateCompiler -webRoot ./web -targetDir ./data

