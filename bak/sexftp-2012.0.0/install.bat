@echo off
echo �뱣֤�Ѿ���װ��JDK1.5���ϰ汾���ҿ���ʹ�� java ���
::if not exist java goto nofile
set cur=%~dp0
set jarfile=%cur%sexftp_2012.0.0.201202120942.jar
if not exist "%jarfile%" goto nofile
java -jar "%jarfile%" "%cur%"
::> out.txt
::notepad out.txt
goto end
:nofile
echo ��װ��������ʧ�ܣ��ļ������ڣ��뱣֤�ѽ�ѹ����װ���򣬲���[%jarfile%]���·������Ч�ģ���ִ�б���װ����
:end
pause
