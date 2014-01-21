@echo OFF
set PRODUCTS_DIR=..\..\..\org.camunda.bpm.modeler.standalone.product\target\products
set WIN32_PRODUCTS_DIR=%PRODUCTS_DIR%\org.camunda.bpm.modeler.standalone.product\win32\win32

set LOCAL_PRODUCTS_DIR=temp
set LOCAL_INSTALLER_DIR=installer

if NOT "%1"=="" set D_VERSION_MAJOR=/DVER_MAJOR=%1
if NOT "%2"=="" set D_VERSION_MINOR=/DVER_MINOR=%2

if "%NSIS_HOME%"=="" GOTO :Error_No_NSIS

echo Cleanup old files...
del %LOCAL_INSTALLER_DIR%\*.exe

echo Create local copy of install files...
robocopy %WIN32_PRODUCTS_DIR% %LOCAL_PRODUCTS_DIR% /MIR >NUL

echo Creating 32bit installer...
"%NSIS_HOME%\makensis" /DSRC_DIR=%LOCAL_PRODUCTS_DIR%\x86\modeler %D_VERSION_MAJOR% %D_VERSION_MINOR% camundaModeler.nsi 2>&1
if ERRORLEVEL 1 goto Error_Installer_Failed else (echo Done.)

echo.

echo Creating 64bit installer...
"%NSIS_HOME%\makensis" /DSRC_DIR=%LOCAL_PRODUCTS_DIR%\x86_64\modeler %D_VERSION_MAJOR% %D_VERSION_MINOR% /DMODELER_64 camundaModeler.nsi 2>&1
if ERRORLEVEL 1 goto Error_Installer_Failed else (echo Done.)


echo Copying generated installers ...
robocopy %LOCAL_INSTALLER_DIR% %PRODUCTS_DIR% *.exe >NUL

GOTO End

:Error_Installer_Failed
echo Failed to create installer.
exit 1

:Error_No_NSIS
echo The environment variable NSIS_HOME is not defined. Make sure you have NSIS installed.
exit 1

:End