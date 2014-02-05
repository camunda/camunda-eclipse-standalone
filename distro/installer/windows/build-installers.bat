@echo OFF
set PRODUCTS_DIR=..\..\..\org.camunda.bpm.modeler.standalone.product\target\products
set WIN32_PRODUCTS_DIR=%PRODUCTS_DIR%\org.camunda.bpm.modeler.standalone.product\win32\win32

set LOCAL_PRODUCTS_DIR=%TMP%\mdist
set LOCAL_INSTALLER_DIR=installer

if NOT "%1"=="" set D_VERSION=/DVERSION=%1

if "%NSIS_HOME%"=="" GOTO Error_No_NSIS

echo Cleanup old files...
del %LOCAL_INSTALLER_DIR%\*.exe >NUL 2>NUL
if exist %LOCAL_PRODUCTS_DIR% rmdir /S /Q %LOCAL_PRODUCTS_DIR%

echo Create local copy of install files...
robocopy %WIN32_PRODUCTS_DIR% %LOCAL_PRODUCTS_DIR% /MIR >NUL

echo Creating 32bit installer...
echo.

"%NSIS_HOME%\makensis" /DSRC_DIR=%LOCAL_PRODUCTS_DIR%\x86\modeler %D_VERSION% camundaModeler.nsi 2>&1
if ERRORLEVEL 1 goto Error_Installer_Failed else (echo Done.)

echo.

echo Creating 64bit installer...
"%NSIS_HOME%\makensis" /DSRC_DIR=%LOCAL_PRODUCTS_DIR%\x86_64\modeler %D_VERSION% /DMODELER_64 camundaModeler.nsi 2>&1
if ERRORLEVEL 1 goto Error_Installer_Failed else (echo Done.)

echo.
echo Cleanup temp files...
rmdir /S /Q %LOCAL_PRODUCTS_DIR%

goto End

:Error_Installer_Failed
echo Failed to create installer.
exit 1

:Error_No_NSIS
echo The environment variable NSIS_HOME is not defined. Make sure you have NSIS installed.
exit 1


:End

echo.

echo Installers saved to installer/ directory.
echo Done.