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
@REM
@REM  Initial Developer: zhh

@echo off
if "%OS%" == "Windows_NT" setlocal

if NOT DEFINED JAVA_HOME goto :err

pushd %~dp0..
if NOT DEFINED LEALONE_HOME set LEALONE_HOME=%CD%
popd

REM ***** JAVA options *****
REM set JAVA_OPTS=-ea^
REM  -Xms10M^
REM  -Xmx1G^
REM  -XX:+HeapDumpOnOutOfMemoryError^
REM  -XX:+UseParNewGC^
REM  -XX:+UseConcMarkSweepGC^
REM  -XX:+CMSParallelRemarkEnabled^
REM  -XX:SurvivorRatio=8^
REM  -XX:MaxTenuringThreshold=1^
REM  -XX:CMSInitiatingOccupancyFraction=75^
REM  -XX:+UseCMSInitiatingOccupancyOnly

set JAVA_OPTS=%JAVA_OPTS%

set str=%1
if "%str%"=="-debug" (
    set JAVA_OPTS=%JAVA_OPTS% -agentlib:jdwp=transport=dt_socket,address=8000,server=y,suspend=y
    goto exec
)
if "%str%"=="" (
    goto exec
)

:exec
set CLASSPATH="%LEALONE_HOME%\conf;%LEALONE_HOME%\lib\*"
"%JAVA_HOME%\bin\java" %JAVA_OPTS% -cp %CLASSPATH% com.lealone.examples.petstore.main.PetStore -database petstore -initSql "set @disable_generate_code true" -sqlScripts "../sql/tables.sql,../sql/init-data.sql,../sql/services.sql" %*
goto finally

:err
echo JAVA_HOME environment variable must be set!
pause

:finally

ENDLOCAL
