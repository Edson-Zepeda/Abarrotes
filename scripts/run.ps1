$ErrorActionPreference = "Stop"

if (-not (Test-Path "out")) {
    & "$PSScriptRoot\compile.ps1"
}

java -cp "out;lib/*" com.abarrotes.app.Main

