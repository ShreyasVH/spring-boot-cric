$file_content = Get-Content ".\.env"
$file_content = $file_content -join [Environment]::NewLine

$configuration = ConvertFrom-StringData($file_content)

foreach ($entry in $configuration.GetEnumerator()) {
    [System.Environment]::SetEnvironmentVariable($entry.Name, $entry.Value, 'Process')
}

jabba use openjdk@1.17.0;
./mvnw spring-boot:run;