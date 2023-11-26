@echo off
echo 请保证已经安装了JDK1.5以上版本并且可以使用 java 命令。
::if not exist java goto nofile
set cur=%~dp0
set jarfile=%cur%sexftp_2012.0.0.201202120942.jar
if not exist "%jarfile%" goto nofile
java -jar "%jarfile%" "%cur%"
::> out.txt
::notepad out.txt
goto end
:nofile
echo 安装程序启动失败，文件不存在，请保证已解压本安装程序，并且[%jarfile%]这个路径是有效的，再执行本安装程序
:end
pause
