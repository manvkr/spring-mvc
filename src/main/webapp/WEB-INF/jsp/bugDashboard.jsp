<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
     String base = System.getenv("APP_API_BASE_URL");
     if(base == null) base = System.getProperty("APP_API_BASE_URL");
     if(base == null || base.trim().isEmpty()){
        String schema = request.getScheme();
        String host = request.getServerName();
        int port = request.getServerPort();
        boolean standard = ("http".equalsIgnoreCase(schema) && port == 80)
                            ||  ("https".equalsIgnoreCase(schema) && port == 443);
        String ctx = request.getContextPath();
        base = schema + "://" + host + (standard ? "" : ":" + port) + ctx;
     }
     base = base.replaceAll("/+$","");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Bug Management</title>
    <script src="https://code.jquery.com/jquery-3.7.1.min.js" integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo=" crossorigin="anonymous"></script>
    <!-- Link the CSS file -->
    <link rel="stylesheet" href="<c:url value='/resources/css/style.css'/>">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">

</head>
<body>
<h1>Bug Management</h1>
<div class="form-section">
    <h3>Add Bug</h3>
    <form id="bugForm">
        <label>Bug Title: <input type="text" name="bugTitle" required></label><br>
        <label>Description: <input type="description" name="description" required></label><br>
        <label>Status: 
            <select name="status" required>
                <option value="OPEN">OPEN</option>
                <option value="IN_PROGRESS">IN PROGRESS</option>
                <option value="CLOSED">CLOSED</option>
            </select>
        </label><br>
        <label>Severity:
            <select name="severity" required>
                <option value="LOW">LOW</option>
                <option value="MEDIUM">MEDIUM</option>
                <option value="HIGH">HIGH</option>
            </select>
        </label><br>
        <button type="submit">Save Bug</button>
    </form>
</div>

<div style="margin-left: 5%;">
    <label>Filter by severity:
        <select id="severityFilter">
            <option value="ALL">ALL</option>
            <option value="LOW">LOW</option>
            <option value="MEDIUM">MEDIUM</option>
            <option value="HIGH">HIGH</option>
        </select>
    </label>
</div>

<table id="bugTable">
    <thead>
    <tr>
        <th>Bug Title</th>
        <th>Description</th>
        <th>Status</th>
        <th>Severity</th>
        <th>Created At</th>
    </tr>
    </thead>
    <tbody></tbody>
</table>

<script>

    window.API_BASE = "<%= base %>";
    function apiUrl(path){
        return window.API_BASE + "/bug" +path;
    }

    function loadBug(filterSeverity) {
        let url = apiUrl("/list");
        if (filterSeverity && filterSeverity !== 'ALL') {
            // we can either filter client-side or request server-side
            url += '?severity=' + filterSeverity;
        }
        $.getJSON(url, function(data) {
            const tbody = $('#bugTable tbody');
            tbody.empty();
            data.forEach(function(u) {
                const severityClasses = {
                    'LOW': 'severity-low',
                    'HIGH': 'severity-high',
                    'MEDIUM': 'severity-medium'
                };

                const severityClass = severityClasses[u.severity] || 'severity-low';
                tbody.append('<tr data-severity="' + u.severity + '">' +
                    '<td>' + u.bugTitle + '</td>' +
                    '<td>' + u.description + '</td>' +
                    '<td>' + u.status.replace('_', ' ') + '</td>' +
                    '<td class="' + severityClass + '">' + u.severity + '</td>' +
                    '<td>' + (u.createdAt ? u.createdAt.replace('T',' ') : '') + '</td>' +
                    '</tr>');
            });
        });
    }

    $(function() {
        loadBug();

        $('#bugForm').on('submit', function(e) {
            e.preventDefault();
            const form = $(this);
            const formData = form.serialize();
            $.ajax({
                url: apiUrl("/add"),
                method: 'POST',
                data: formData,
                success: function(newBug, textStatus, xhr) {
                    if (xhr.status >= 200 && xhr.status < 300) {
                        form[0].reset();
                        loadBug($('#severityFilter').val());
                    } else {
                        alert('Unexpected severity while saving: ' + xhr.severity + ' ' + xhr.responseText);
                    }
                },
                error: function(xhr) {
                    let msg = 'Error saving bug';
                    if (xhr.responseJSON) {
                        msg += ': ' + (xhr.responseJSON.message || JSON.stringify(xhr.responseJSON));
                    } else if (xhr.responseText) {
                        msg += ': ' + xhr.responseText;
                    }
                    alert(msg);
                }
            });
        });

        $('#severityFilter').on('change', function() {
            const selected = $(this).val();
            loadBug(selected);
        });
    });
</script>
</body>
</html>