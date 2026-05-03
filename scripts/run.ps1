$ErrorActionPreference = "Stop"

if (-not (Test-Path "out")) {
    & "$PSScriptRoot\compile.ps1"
}

java -cp out com.abarrotes.app.Main

