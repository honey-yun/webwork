//@charset "UTF-8"; // 文件编码：UTF-8
/**
 * 后台管理JavaScript文件
 */

// 页面加载完成后执行
document.addEventListener('DOMContentLoaded', function() {
    initSidebarToggle();
    initMenuActiveState();
});

/**
 * 初始化侧边栏切换
 */
function initSidebarToggle() {
    const sidebarToggle = document.getElementById('sidebarToggle');
    const adminSidebar = document.getElementById('adminSidebar');

    if (sidebarToggle && adminSidebar) {
        sidebarToggle.addEventListener('click', () => {
            adminSidebar.classList.toggle('active');
        });
    }
}

/**
 * 初始化菜单激活状态
 */
function initMenuActiveState() {
    document.querySelectorAll('.admin-menu-item a').forEach(item => {
        if (item.getAttribute('href') === window.location.pathname.split('/').pop() ||
            (window.location.pathname.includes('admin') && item.textContent.includes('仪表盘'))) {
            item.closest('.admin-menu-item').classList.add('active');
        }
    });
}
