<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${pageTitle != null ? pageTitle : 'Quản lý trọ'}"/></title>
    <style>
        * { box-sizing: border-box; }
        body { font-family: Arial, sans-serif; margin: 0; background: #f5f5f5; }
        .app { display: flex; min-height: 100vh; }
        .sidebar { position: fixed; left: 0; top: 0; width: 200px; height: 100vh; background: #1e293b; padding: 16px; overflow-y: auto; z-index: 100; }
        .sidebar a { display: block; color: #e2e8f0; text-decoration: none; padding: 8px 0; border-radius: 4px; transition: all 0.2s; }
        .sidebar a:hover { color: #fff; }
        .sidebar a.active { background: #2563eb; color: #fff; padding-left: 8px; }
        .main { flex: 1; display: flex; flex-direction: column; margin-left: 200px; margin-top: 60px; }
        .header { position: fixed; top: 0; right: 0; left: 200px; background: #fff; padding: 16px 24px; display: flex; justify-content: space-between; align-items: center; gap: 16px; box-shadow: 0 1px 3px rgba(0,0,0,0.1); min-height: 60px; z-index: 99; }
        .header h2 { margin: 0; font-size: 1.25rem; flex-shrink: 0; }
        .user-info { display: flex; align-items: center; gap: 12px; }
        .user-info span { color: #334155; }
        .logout-btn { background: #dc2626; color: #fff; border: none; padding: 8px 16px; border-radius: 4px; font-size: 0.875rem; cursor: pointer; text-decoration: none; display: inline-flex; align-items: center; }
        .logout-btn:hover { background: #b91c1c; }
        .content { padding: 24px; flex: 1; }
        .footer { padding: 12px 24px; background: #e2e8f0; font-size: 0.875rem; color: #64748b; }
        .card { background: #fff; border-radius: 8px; padding: 20px; margin-bottom: 16px; box-shadow: 0 1px 3px rgba(0,0,0,0.1); }
        .stats { display: flex; gap: 16px; flex-wrap: wrap; }
        .stat { background: #fff; padding: 20px; border-radius: 8px; min-width: 140px; box-shadow: 0 1px 3px rgba(0,0,0,0.1); }
        .stat span { font-size: 1.5rem; font-weight: bold; color: #2563eb; }
    </style>
</head>
<body>
    <div class="app">
        <jsp:include page="sidebar.jsp"/>
        <div class="main">
            <jsp:include page="header.jsp"/>
            <div class="content">
                <jsp:doBody/>
            </div>
            <jsp:include page="footer.jsp"/>
        </div>
    </div>
</body>
</html>
