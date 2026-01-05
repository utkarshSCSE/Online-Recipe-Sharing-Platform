<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<table border="1">
    <tr><th>ID</th><th>Title</th><th>Author</th><th>Description</th></tr>
    <c:forEach var="r" items="${recipeList}">
        <tr>
            <td>${r.id}</td>
            <td>${r.title}</td>
            <td>${r.author}</td>
            <td>${r.description}</td>
        </tr>
    </c:forEach>
</table>