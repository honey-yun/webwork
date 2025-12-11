# Windows PowerShell 排查命令

## 1. 查找Tomcat安装位置

### 方法1：查找Tomcat进程
```powershell
Get-Process | Where-Object {$_.ProcessName -like "*tomcat*" -or $_.ProcessName -like "*java*"} | Select-Object ProcessName, Path
```

### 方法2：查找常见的Tomcat安装目录
```powershell
# 检查常见位置
$commonPaths = @(
    "C:\Program Files\Apache Software Foundation\Tomcat 9.0",
    "C:\apache-tomcat-9.0.63",
    "C:\Tomcat",
    "D:\apache-tomcat-9.0.63",
    "$env:USERPROFILE\apache-tomcat-9.0.63"
)

foreach ($path in $commonPaths) {
    if (Test-Path $path) {
        Write-Host "找到Tomcat: $path" -ForegroundColor Green
    }
}
```

### 方法3：通过服务查找
```powershell
Get-Service | Where-Object {$_.Name -like "*tomcat*"} | Select-Object Name, DisplayName, Status
```

## 2. 查看Tomcat日志（找到Tomcat目录后）

假设Tomcat在 `C:\apache-tomcat-9.0.63`：

```powershell
# 查看最新的日志文件
Get-ChildItem "C:\apache-tomcat-9.0.63\logs" | Sort-Object LastWriteTime -Descending | Select-Object -First 5

# 查看catalina.out（如果存在）
if (Test-Path "C:\apache-tomcat-9.0.63\logs\catalina.out") {
    Get-Content "C:\apache-tomcat-9.0.63\logs\catalina.out" -Tail 50 | Select-String "数据库|登录|错误|异常"
}

# 查看最新的localhost日志
$latestLog = Get-ChildItem "C:\apache-tomcat-9.0.63\logs\localhost.*.log" | Sort-Object LastWriteTime -Descending | Select-Object -First 1
if ($latestLog) {
    Write-Host "查看日志: $($latestLog.FullName)" -ForegroundColor Yellow
    Get-Content $latestLog.FullName -Tail 100 | Select-String "数据库|登录|错误|异常"
}
```

## 3. 检查部署的文件

```powershell
# 查找webwork2部署目录
$webappPaths = @(
    "C:\apache-tomcat-9.0.63\webapps\webwork2",
    "D:\apache-tomcat-9.0.63\webapps\webwork2"
)

foreach ($path in $webappPaths) {
    if (Test-Path $path) {
        Write-Host "找到部署目录: $path" -ForegroundColor Green
        
        # 检查CSS文件
        $cssFile = Join-Path $path "css\style.css"
        if (Test-Path $cssFile) {
            Write-Host "✓ CSS文件存在: $cssFile" -ForegroundColor Green
        } else {
            Write-Host "✗ CSS文件不存在: $cssFile" -ForegroundColor Red
        }
        
        # 检查JS文件
        $jsFile = Join-Path $path "js\main.js"
        if (Test-Path $jsFile) {
            Write-Host "✓ JS文件存在: $jsFile" -ForegroundColor Green
        } else {
            Write-Host "✗ JS文件不存在: $jsFile" -ForegroundColor Red
        }
        
        # 检查JSP文件
        $jspPath = Join-Path $path "WEB-INF\views"
        if (Test-Path $jspPath) {
            Write-Host "✓ JSP目录存在: $jspPath" -ForegroundColor Green
            Get-ChildItem $jspPath -Filter "*.jsp" | Select-Object Name
        } else {
            Write-Host "✗ JSP目录不存在: $jspPath" -ForegroundColor Red
        }
    }
}
```

## 4. 检查项目编译输出

```powershell
# 检查target目录
if (Test-Path "target\webwork2-1.0-SNAPSHOT.war") {
    Write-Host "✓ WAR文件已生成" -ForegroundColor Green
    $warInfo = Get-Item "target\webwork2-1.0-SNAPSHOT.war"
    Write-Host "文件大小: $([math]::Round($warInfo.Length/1MB, 2)) MB"
    Write-Host "修改时间: $($warInfo.LastWriteTime)"
} else {
    Write-Host "✗ WAR文件不存在，需要执行: mvn clean package" -ForegroundColor Red
}
```

## 5. 一键排查脚本

将以下内容保存为 `check.ps1` 并运行：

```powershell
# 一键排查脚本
Write-Host "=== 博客系统排查工具 ===" -ForegroundColor Cyan

# 1. 查找Tomcat
Write-Host "`n[1] 查找Tomcat安装位置..." -ForegroundColor Yellow
$tomcatPaths = @(
    "C:\Program Files\Apache Software Foundation\Tomcat 9.0",
    "C:\apache-tomcat-9.0.63",
    "C:\Tomcat",
    "D:\apache-tomcat-9.0.63"
)

$foundTomcat = $null
foreach ($path in $tomcatPaths) {
    if (Test-Path $path) {
        $foundTomcat = $path
        Write-Host "找到Tomcat: $path" -ForegroundColor Green
        break
    }
}

if (-not $foundTomcat) {
    Write-Host "未找到Tomcat，请手动指定路径" -ForegroundColor Red
    $foundTomcat = Read-Host "请输入Tomcat路径"
}

# 2. 检查部署
Write-Host "`n[2] 检查部署文件..." -ForegroundColor Yellow
$webappPath = Join-Path $foundTomcat "webapps\webwork2"
if (Test-Path $webappPath) {
    Write-Host "✓ 部署目录存在: $webappPath" -ForegroundColor Green
    
    # 检查关键文件
    $files = @(
        "css\style.css",
        "js\main.js",
        "WEB-INF\views\login.jsp",
        "WEB-INF\web.xml"
    )
    
    foreach ($file in $files) {
        $fullPath = Join-Path $webappPath $file
        if (Test-Path $fullPath) {
            Write-Host "  ✓ $file" -ForegroundColor Green
        } else {
            Write-Host "  ✗ $file (不存在)" -ForegroundColor Red
        }
    }
} else {
    Write-Host "✗ 未找到部署目录，请先部署WAR文件" -ForegroundColor Red
}

# 3. 查看日志
Write-Host "`n[3] 查看最新日志..." -ForegroundColor Yellow
$logPath = Join-Path $foundTomcat "logs"
if (Test-Path $logPath) {
    $latestLog = Get-ChildItem $logPath -Filter "*.log" | Sort-Object LastWriteTime -Descending | Select-Object -First 1
    if ($latestLog) {
        Write-Host "最新日志: $($latestLog.Name)" -ForegroundColor Cyan
        Write-Host "最后50行中包含'数据库'或'登录'的内容:" -ForegroundColor Cyan
        Get-Content $latestLog.FullName -Tail 50 | Select-String "数据库|登录|错误|异常|SQL" | Select-Object -Last 10
    }
}

# 4. 检查数据库配置
Write-Host "`n[4] 检查数据库配置..." -ForegroundColor Yellow
$dbUtilPath = "src\main\java\com\blog\dao\DBUtil.java"
if (Test-Path $dbUtilPath) {
    $content = Get-Content $dbUtilPath -Raw
    if ($content -match "USERNAME\s*=\s*""([^""]+)""") {
        Write-Host "数据库用户名: $($matches[1])" -ForegroundColor Cyan
    }
    if ($content -match "PASSWORD\s*=\s*""([^""]+)""") {
        Write-Host "数据库密码: $($matches[1])" -ForegroundColor Cyan
    }
}

Write-Host "`n=== 排查完成 ===" -ForegroundColor Cyan
```

## 6. 快速测试命令

```powershell
# 测试数据库连接（需要先找到Tomcat并启动）
# 访问: http://localhost:8080/webwork2/testdb

# 测试登录页面
# 访问: http://localhost:8080/webwork2/login

# 测试CSS文件
# 访问: http://localhost:8080/webwork2/css/style.css
```

## 7. 重新部署命令

```powershell
# 在项目根目录执行
# 1. 编译打包
mvn clean package

# 2. 停止Tomcat（如果正在运行）
# 找到Tomcat的bin目录，执行:
# .\shutdown.bat

# 3. 删除旧部署
Remove-Item "C:\apache-tomcat-9.0.63\webapps\webwork2" -Recurse -Force -ErrorAction SilentlyContinue
Remove-Item "C:\apache-tomcat-9.0.63\webapps\webwork2.war" -Force -ErrorAction SilentlyContinue

# 4. 复制新WAR文件
Copy-Item "target\webwork2-1.0-SNAPSHOT.war" "C:\apache-tomcat-9.0.63\webapps\webwork2.war"

# 5. 启动Tomcat
# .\startup.bat
```

## 注意事项

1. **路径中的反斜杠**：PowerShell中路径可以使用 `\` 或 `/`，但建议使用 `\` 或 `Join-Path` 命令
2. **权限问题**：某些操作可能需要管理员权限
3. **Tomcat路径**：根据实际安装位置修改路径
4. **日志文件**：Windows下Tomcat可能使用 `localhost.YYYY-MM-DD.log` 而不是 `catalina.out`

