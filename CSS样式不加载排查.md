# CSS样式不加载排查指南

## 问题：CSS返回200但样式不生效

### 可能原因和解决方法

#### 1. Content-Type 问题（最常见）

**问题**：CSS文件虽然返回200，但Content-Type可能不正确，导致浏览器不解析CSS。

**解决方法**：
- ✅ 已修复：Filter现在会为CSS文件设置正确的Content-Type: `text/css; charset=UTF-8`
- ✅ 已修复：Filter现在会为JS文件设置正确的Content-Type: `application/javascript; charset=UTF-8`

#### 2. 浏览器缓存问题

**解决方法**：
1. 按 `Ctrl + Shift + Delete` 清除浏览器缓存
2. 或按 `Ctrl + F5` 强制刷新页面
3. 或在开发者工具中勾选"Disable cache"

#### 3. 检查CSS文件内容

在浏览器中直接访问CSS文件：
```
http://localhost:8080/webwork2/css/style.css
```

**应该看到**：
```css
/* 全局样式 */
* {
    box-sizing: border-box;
}
...
```

**如果看到HTML内容**，说明CSS文件被当作HTML处理了，需要检查Filter。

#### 4. 检查浏览器开发者工具

按F12打开开发者工具：

**Network标签**：
1. 找到 `style.css` 文件
2. 点击查看详情
3. 检查：
   - **Status**: 应该是 200
   - **Type**: 应该是 `text/css`（不是 `text/html`）
   - **Size**: 应该有内容大小（不是0）
   - **Response Headers**: 应该包含 `Content-Type: text/css`

**Elements标签**：
1. 检查 `<link>` 标签是否正确
2. 检查CSS是否被加载（应该能看到样式规则）

**Console标签**：
1. 查看是否有CSS相关的错误

#### 5. 检查HTML源码

右键页面 → "查看页面源码"，检查：
```html
<!-- 正确 -->
<link rel="stylesheet" href="/webwork2/css/style.css">

<!-- 错误示例 -->
<link rel="stylesheet" href="css/style.css">
<link rel="stylesheet" href="../css/style.css">
```

#### 6. 检查CSS选择器

确认CSS选择器与HTML结构匹配：

**HTML结构**：
```html
<body class="login-page">
    <div class="login-container">
        <h1>用户登录</h1>
        ...
    </div>
</body>
```

**CSS应该有**：
```css
.login-page { ... }
.login-container { ... }
```

#### 7. 测试方法

**方法1：直接在浏览器控制台测试**
```javascript
// 在浏览器控制台执行
document.querySelector('link[rel="stylesheet"]').href
// 应该返回完整的CSS文件URL

// 测试CSS是否加载
getComputedStyle(document.body).fontFamily
// 应该返回 "Arial, sans-serif"
```

**方法2：临时添加内联样式测试**
在JSP的 `<head>` 中添加：
```html
<style>
body { background-color: red !important; }
</style>
```
如果红色背景显示，说明CSS加载机制正常，问题在外部CSS文件。

**方法3：检查CSS文件编码**
确保CSS文件是UTF-8编码，没有BOM。

## 已实施的修复

1. ✅ Filter现在会为CSS文件设置正确的Content-Type
2. ✅ Filter现在会为JS文件设置正确的Content-Type
3. ✅ 所有JSP文件使用 `${pageContext.request.contextPath}` 引用CSS/JS

## 验证步骤

1. **重新编译部署**：
   ```bash
   mvn clean package
   ```

2. **清除浏览器缓存**：
   - 按 `Ctrl + Shift + Delete`
   - 或按 `Ctrl + F5` 强制刷新

3. **检查Network标签**：
   - 打开开发者工具（F12）
   - 查看 `style.css` 的Content-Type是否为 `text/css`

4. **直接访问CSS文件**：
   ```
   http://localhost:8080/webwork2/css/style.css
   ```
   应该看到CSS代码，不是HTML

5. **检查页面样式**：
   - 登录页面应该有居中布局
   - 背景色应该是 `#f5f5f5`
   - 登录框应该有白色背景和阴影

## 如果仍然不工作

请提供以下信息：
1. 浏览器开发者工具中 `style.css` 的完整响应头信息
2. 直接访问CSS文件时看到的内容（前几行）
3. 浏览器控制台是否有任何错误
4. 页面源码中 `<link>` 标签的完整内容

