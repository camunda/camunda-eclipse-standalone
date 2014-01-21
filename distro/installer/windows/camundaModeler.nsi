;camunda Modeler installer generator script
;based on Modern UI example scripts
;Written by Nico Rehwaldt

; How to use
; ----------
;
; to build specify
; 
; SRC_DIR= installable files
;
; VER_MAJOR= major version
; VER_MINOR= minor version
; VER_REVISION= bug fix version
;
; MODELER_64 for 64bit build
;

;--------------------------------
; environment variables

!ifdef MODELER_64
  !define BITS 64
  !define NAMESUFFIX " (64 bit)"
!else
  !define BITS 32
  !define NAMESUFFIX ""
!endif

!ifdef VER_MAJOR & VER_MINOR
  !define /ifndef VER_REVISION 0
  !define /ifndef VER_BUILD 0

  !define /ifndef VERSION "${VER_MAJOR}.${VER_MINOR}.${VER_REVISION}"
!endif

!define /ifndef VER_MAJOR 0
!define /ifndef VER_MINOR 0
!define /ifndef VERSION 'dev-build'


;--------------------------------
;Product Configuration

!define PRODUCT_NAME "camunda Modeler${NAMESUFFIX}"
!define PRODUCT_PUBLISHER "camunda Services GmbH"
!define PRODUCT_WEB_SITE "https://github.com/Nikku/camunda-modeler-standalone"
!define PRODUCT_UNINST_KEY "Software\Microsoft\Windows\CurrentVersion\Uninstall\${PRODUCT_NAME}"
!define PRODUCT_INSTALL_NAME "camunda\${PRODUCT_NAME}"
!define PRODUCT_INSTALL_DIR "${PRODUCT_INSTALL_NAME}"


;--------------------------------
; dependencies

  !include "ext\FileAssoc.nsh"
  !include "MUI2.nsh"


;--------------------------------
; basic definitions

!define SHCNF_IDLIST 0


;--------------------------------
;General

  ;Name and file
  Name "${PRODUCT_NAME}"
  OutFile "installer/modeler${BITS}-${VERSION}-setup.exe"
  
  Unicode true

  ;Default installation folder
  InstallDir "$PROGRAMFILES\${PRODUCT_INSTALL_DIR}"
  
  ;Get installation folder from registry if available
  InstallDirRegKey HKLM "Software\${PRODUCT_INSTALL_NAME}" ""

  BrandingText "${PRODUCT_PUBLISHER}"

  ;Request application privileges for Windows Vista
  RequestExecutionLevel admin


;--------------------------------
;Variables

  Var StartMenuFolder


;--------------------------------
;Interface Settings
  !define MUI_ABORTWARNING

  !define MUI_ICON "resources\images\icons\install.ico"
  !define MUI_UNICON "resources\images\icons\uninstall.ico"


;--------------------------------
;Pages

  !insertmacro MUI_PAGE_LICENSE "resources\docs\license.txt"
  !insertmacro MUI_PAGE_COMPONENTS
  !insertmacro MUI_PAGE_DIRECTORY
  
  ;Start Menu Folder Page Configuration
  !define MUI_STARTMENUPAGE_REGISTRY_ROOT "HKLM" 
  !define MUI_STARTMENUPAGE_REGISTRY_KEY "Software\${PRODUCT_INSTALL_NAME}" 
  !define MUI_STARTMENUPAGE_REGISTRY_VALUENAME "Start Menu Folder"
  
  !insertmacro MUI_PAGE_STARTMENU Application $StartMenuFolder
  
  !insertmacro MUI_PAGE_INSTFILES
  
  !insertmacro MUI_UNPAGE_CONFIRM
  !insertmacro MUI_UNPAGE_INSTFILES

;--------------------------------
;Languages
 
  !insertmacro MUI_LANGUAGE "English"

;--------------------------------
;Installer Sections

Section "Modeler" SecModelerMain

  SetOutPath "$INSTDIR"
  SetOverwrite ifnewer
  
  ;Create menu entries for all users
  SetShellVarContext all

  ; add installable artifacts
  File /r "${SRC_DIR}\*.*"
  File /r "..\..\skeleton\*.*"

  ;Store installation folder
  WriteRegStr HKLM "Software\${PRODUCT_INSTALL_NAME}" "" $INSTDIR

  ; add uninstall integration
  WriteRegStr HKLM "${PRODUCT_UNINST_KEY}" "DisplayName" "${PRODUCT_NAME}"
  WriteRegStr HKLM "${PRODUCT_UNINST_KEY}" "UninstallString" `"$INSTDIR\uninstall.exe"`
  WriteRegStr HKLM "${PRODUCT_UNINST_KEY}" "DisplayIcon" `"$INSTDIR\modeler.exe"`
  WriteRegStr HKLM "${PRODUCT_UNINST_KEY}" "DisplayVersion" "${VERSION}"
  WriteRegDWORD HKLM "${PRODUCT_UNINST_KEY}" "VersionMajor" "${VER_MAJOR}"
  WriteRegDWORD HKLM "${PRODUCT_UNINST_KEY}" "VersionMinor" "${VER_MINOR}"
  ; WriteRegStr HKLM "${PRODUCT_UNINST_KEY}" "URLInfoAbout" "${PRODUCT_WEB_SITE}"
  ; WriteRegStr HKLM "${PRODUCT_UNINST_KEY}" "Publisher" "${PRODUCT_PUBLISHER}"
  
  ; associate file
  !insertmacro APP_ASSOCIATE "bpmn" "modeler.bpmnFile" "BPMN 2.0 diagram file" \
      "$INSTDIR\modeler.exe,0" "Open with camunda Modeler" `"$INSTDIR\modeler.exe" "%1"`

  ; update file assoc in shell
  System::Call 'Shell32::SHChangeNotify(i ${SHCNE_ASSOCCHANGED}, i ${SHCNF_IDLIST}, i 0, i 0)'

  ;Create uninstaller
  WriteUninstaller "$INSTDIR\uninstall.exe"

  !insertmacro MUI_STARTMENU_WRITE_BEGIN Application
    
    ;Create shortcuts
    CreateDirectory "$SMPROGRAMS\$StartMenuFolder"
    CreateShortCut "$SMPROGRAMS\$StartMenuFolder\camunda Modeler.lnk" "$INSTDIR\modeler.exe"
    CreateShortCut "$SMPROGRAMS\$StartMenuFolder\Uninstall.lnk" "$INSTDIR\Uninstall.exe"
  
  !insertmacro MUI_STARTMENU_WRITE_END

SectionEnd

;--------------------------------
;Descriptions

  ;Language strings
  LangString DESC_SecModelerMain ${LANG_ENGLISH} "Contains the Modeler executables that allow you to model BPMN 2.0 diagrams"

  ;Assign language strings to sections
  !insertmacro MUI_FUNCTION_DESCRIPTION_BEGIN
    !insertmacro MUI_DESCRIPTION_TEXT ${SecModelerMain} $(DESC_SecModelerMain)
  !insertmacro MUI_FUNCTION_DESCRIPTION_END

;--------------------------------
;Uninstaller Section

Section "Uninstall"
  
  ;Create menu entries for all users
  SetShellVarContext all

  ;ADD YOUR OWN FILES HERE...

  RMDir /r "$INSTDIR\configuration"
  RMDir /r "$INSTDIR\p2"
  RMDir /r "$INSTDIR\plugins"
  RMDir /r "$INSTDIR\workspace"

  Delete "$INSTDIR\Uninstall.exe"
  Delete "$INSTDIR\artifacts.xml"
  Delete "$INSTDIR\modeler.exe"
  Delete "$INSTDIR\modeler.ini"

  RMDir "$INSTDIR"
  
  !insertmacro MUI_STARTMENU_GETFOLDER Application $StartMenuFolder
  
  Delete "$SMPROGRAMS\$StartMenuFolder\Uninstall.lnk"
  Delete "$SMPROGRAMS\$StartMenuFolder\camunda Modeler.lnk"
  RMDir "$SMPROGRAMS\$StartMenuFolder"

  ; remove file assoc
  !insertmacro APP_UNASSOCIATE "bpmn" "modeler.bpmnFile"
  
  ; update shell associations
  System::Call 'Shell32::SHChangeNotify(i ${SHCNE_ASSOCCHANGED}, i ${SHCNF_IDLIST}, i 0, i 0)'

  DeleteRegKey /ifempty HKLM "Software\${PRODUCT_INSTALL_NAME}"
  DeleteRegKey HKLM "${PRODUCT_UNINST_KEY}"

SectionEnd