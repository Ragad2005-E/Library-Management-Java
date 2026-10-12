$ErrorActionPreference = 'Stop'
Push-Location $PSScriptRoot
try {
    New-Item -ItemType Directory -Path 'build/classes' -Force | Out-Null
    $sources = @(Get-ChildItem -LiteralPath 'src/librarymanagementsys' -Filter '*.java' | ForEach-Object FullName)
    & javac --release 17 -encoding UTF-8 -d 'build/classes' @sources
    if ($LASTEXITCODE -ne 0) { throw 'Compilation failed.' }
    & java -cp 'build/classes' librarymanagementsys.LibraryManagementSys
    if ($LASTEXITCODE -ne 0) { throw 'Application failed.' }
} finally { Pop-Location }
