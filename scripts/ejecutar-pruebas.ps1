$ErrorActionPreference = 'Stop'
$raizProyecto = Split-Path -Parent $PSScriptRoot
Push-Location $raizProyecto
try {
    $destinoClases = Join-Path $raizProyecto 'target/pruebas-wagner'
    New-Item -ItemType Directory -Force -Path $destinoClases | Out-Null
    $fuentes = @(Get-ChildItem src/main/java, src/test/java -Recurse -Filter *.java | ForEach-Object { $_.FullName })
    & javac --release 17 -encoding UTF-8 -d $destinoClases @fuentes
    if ($LASTEXITCODE -ne 0) { throw 'Falló la compilación.' }
    & java -cp $destinoClases com.mycompany.parkingcoto.ReglasNegocioTest
    $codigoPruebas = $LASTEXITCODE
} finally {
    Pop-Location
}
exit $codigoPruebas
