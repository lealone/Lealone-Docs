@REM
@REM  Licensed to the Apache Software Foundation (ASF) under one or more
@REM  contributor license agreements.  See the NOTICE file distributed with
@REM  this work for additional information regarding copyright ownership.
@REM  The ASF licenses this file to You under the Apache License, Version 2.0
@REM  (the "License"); you may not use this file except in compliance with
@REM  the License.  You may obtain a copy of the License at
@REM
@REM      http://www.apache.org/licenses/LICENSE-2.0
@REM
@REM  Unless required by applicable law or agreed to in writing, software
@REM  distributed under the License is distributed on an "AS IS" BASIS,
@REM  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@REM  See the License for the specific language governing permissions and
@REM  limitations under the License.

@echo off
if "%OS%" == "Windows_NT" setlocal

set ARG=%1

if /i "%ARG%" == "" goto usage
if /i "%ARG%" == "-e" goto e
if /i "%ARG%" == "-ec" goto ec
if /i "%ARG%" == "-es" goto es
if /i "%ARG%" == "-p" goto p
if /i "%ARG%" == "-pc" goto pc
if /i "%ARG%" == "-i" goto i
if /i "%ARG%" == "-c" goto c
if /i "%ARG%" == "-dt" goto dt

goto usage

:usage
echo usage: build [options]
echo    options:
echo    -e            mvn eclipse:eclipse
echo    -ec           mvn eclipse:clean
echo    -es           mvn eclipse:eclipse -DdownloadSources=true
echo    -p            mvn package assembly:assembly -Dmaven.test.skip=true
echo    -pc           mvn clean package assembly:assembly -Dmaven.test.skip=true
echo    -i            mvn install -Dmaven.test.skip=true
echo    -c            mvn clean
echo    -dt           mvn dependency:tree
goto end

:e
call mvn eclipse:eclipse
goto end

:ec
call mvn eclipse:clean
goto end

:es
call mvn eclipse:eclipse -DdownloadSources=true
goto end

:p
REM call mvn package assembly:assembly -Dmaven.test.skip=true

call mvn package -Dmaven.test.skip=true
java -cp petstore-main/target/petstore-1.0.0.jar^
     com.lealone.plugins.service.template.TemplateCompiler^
     -webRoot petstore-web/web -targetDir petstore-web/target
call mvn assembly:assembly -Dmaven.test.skip=true
goto end

:pc
mvn clean package assembly:assembly -Dmaven.test.skip=true
goto p

:c
call mvn clean
goto end

:i
call mvn install -Dmaven.test.skip=true
goto end

:dt
call mvn dependency:tree
goto end

:end