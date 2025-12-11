# CSS路径问题说明

## 问题原因

### 为什么 `css/style.css` 不工作？

**目录结构**：
```
webapp/
├── css/
│   └── style.css
├── views/
│   └── login.jsp
└── js/
    └── main.js
```

**问题分析**：
1. JSP文件在 `views/` 目录下
2. CSS文件在 `css/` 目录下
3. 当JSP使用相对路径 `css/style.css` 时，浏览器会根据**当前页面的URL**来解析路径

**具体场景**：
- 用户访问：`http://localhost:8080/webwork2/login`
- Servlet转发到：`/views/login.jsp`
- 浏览器认为当前路径是：`/login`
- 相对路径 `css/style.css` 被解析为：`/login/css/style.css` ❌
- 正确的路径应该是：`/webwork2/css/style.css` ✅

## 解决方案

### 方案1：使用 `${pageContext.request.contextPath}`（推荐）

```jsp
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
<script src="${pageContext.request.contextPath}/js/main.js"></script>
```

**优点**：
- ✅ 无论JSP在哪个目录都能正确工作
- ✅ 无论应用部署在什么context path都能工作
- ✅ 最可靠的方式

**解析结果**：
- `${pageContext.request.contextPath}` = `/webwork2`
- 最终路径 = `/webwork2/css/style.css` ✅

### 方案2：使用相对路径 `../css/style.css`

```jsp
<link rel="stylesheet" href="../css/style.css">
<script src="../js/main.js"></script>
```

**优点**：
- ✅ 路径简单
- ✅ 不需要EL表达式

**缺点**：
- ❌ 如果JSP目录结构改变，路径会失效
- ❌ 如果通过Servlet转发，路径可能不正确

### 方案3：使用绝对路径 `/webwork2/css/style.css`

```jsp
<link rel="stylesheet" href="/webwork2/css/style.css">
<script src="/webwork2/js/main.js"></script>
```

**缺点**：
- ❌ 硬编码了context path，如果部署路径改变需要修改代码
- ❌ 不推荐使用

## 为什么Filter已经修复了还是不行？

Filter已经正确配置，可以识别并放行CSS文件。但是：

1. **路径解析发生在浏览器端**，不是服务器端
2. 如果CSS路径错误（404），Filter根本不会处理这个请求
3. 浏览器会显示"找不到资源"的错误

## 验证方法

### 1. 检查浏览器开发者工具

按F12打开开发者工具，查看Network标签：
- 如果CSS文件状态码是 **404**，说明路径错误
- 如果CSS文件状态码是 **200**，但样式不生效，可能是其他问题

### 2. 直接访问CSS文件

在浏览器中直接访问：
```
http://localhost:8080/webwork2/css/style.css
```

- 如果能看到CSS内容，说明文件存在且Filter工作正常
- 如果404，说明文件不存在或路径错误

### 3. 查看HTML源码

在浏览器中右键"查看页面源码"，检查CSS链接：
```html
<!-- 错误示例 -->
<link rel="stylesheet" href="css/style.css">

<!-- 正确示例 -->
<link rel="stylesheet" href="/webwork2/css/style.css">
```

## 已修复

所有JSP文件已更新为使用 `${pageContext.request.contextPath}/css/style.css`，这是最可靠的方案。

## 测试步骤

1. 重新编译打包：`mvn clean package`
2. 重新部署到Tomcat
3. 访问登录页面：`http://localhost:8080/webwork2/login`
4. 按F12查看Network标签，确认CSS文件状态码为200
5. 查看页面样式是否正常显示

