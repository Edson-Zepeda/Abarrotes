$ErrorActionPreference = "Stop"

$sourceFiles = Get-ChildItem -Path "src/main/java" -Recurse -Filter "*.java" | ForEach-Object { $_.FullName }

if (-not $sourceFiles) {
    throw "No se encontraron archivos Java en src/main/java."
}

New-Item -ItemType Directory -Force -Path "out" | Out-Null
javac -encoding UTF-8 -cp "lib/*" -d out $sourceFiles
Write-Host "Compilacion completada en out/"

