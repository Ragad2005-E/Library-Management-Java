$ErrorActionPreference = 'Stop'
Push-Location $PSScriptRoot
try {
    New-Item -ItemType Directory -Path 'build/test-classes' -Force | Out-Null
    $sources = @(Get-ChildItem -LiteralPath 'src/librarymanagementsys' -Filter '*.java' | ForEach-Object FullName)
    $sources += @(Get-ChildItem -LiteralPath 'test/librarymanagementsys' -Filter '*.java' | ForEach-Object FullName)
    & javac --release 17 -encoding UTF-8 -Xlint:all -d 'build/test-classes' @sources
    if ($LASTEXITCODE -ne 0) { throw 'Compilation failed.' }
    & java -cp 'build/test-classes' librarymanagementsys.RegressionTests
    if ($LASTEXITCODE -ne 0) { throw 'Regression tests failed.' }
} finally { Pop-Location }
