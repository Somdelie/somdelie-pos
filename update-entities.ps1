# PostgreSQL UUID Update Script
$entityFiles = Get-ChildItem -Path "src/main/java" -Recurse -Filter "*.java" | Where-Object { 
    $content = Get-Content $_.FullName -Raw
    $content -match "@Entity" -and $content -match "private UUID"
}

$updatedCount = 0

foreach ($file in $entityFiles) {
    $content = Get-Content $file.FullName -Raw
    $originalContent = $content
    
    # Update UUID column definition from MySQL BINARY(16) to PostgreSQL uuid
    $content = $content -replace '@Column\(columnDefinition\s*=\s*"BINARY\(16\)"\)', '@Column(updatable = false, nullable = false, columnDefinition = "uuid")'
    
    # Add GenericGenerator import if not present and UUID is used
    if ($content -match "private UUID" -and $content -notmatch "import org.hibernate.annotations.GenericGenerator") {
        $content = $content -replace "(import jakarta.persistence.*;\s*)", "`$1`nimport org.hibernate.annotations.GenericGenerator;"
    }
    
    # Update @GeneratedValue to use UUID generator
    $content = $content -replace '@GeneratedValue\(strategy\s*=\s*GenerationType\.AUTO\)\s*@Column\(updatable = false, nullable = false, columnDefinition = "uuid"\)', '@GeneratedValue(generator = "UUID")`n    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")`n    @Column(updatable = false, nullable = false, columnDefinition = "uuid")'
    
    if ($content -ne $originalContent) {
        Set-Content -Path $file.FullName -Value $content
        $updatedCount++
        Write-Host " Updated: $($file.Name)" -ForegroundColor Cyan
    }
}

Write-Host "`n Total entity files updated: $updatedCount" -ForegroundColor Green
