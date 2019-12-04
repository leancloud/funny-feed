<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>LeanEngine</title>
<link rel="stylesheet" href="/stylesheets/style.css" />
</head>
<body>
 <h1>Status 列表</h1>
 <form action="/status" method="post">
   <select name="user">
     <c:forEach items="${users}" var="username">
       <option value="${username}">${username}</option>
     </c:forEach>
   </select>
   <br/>
   <textarea name="content" rows="4" cols="50">say something... </textarea>
   <input type="submit" />
 </form>

<span>
<table>
<thead>
    <tr>
        <td><h3>objectId</h3></td>
        <td><h3>content</h3></td>
        <td><h3>createdAt</h3></td>
    </tr>
</thead>
<tbody>
<c:forEach items="${statuses}" var="status">
    <tr>      
        <td>${status.objectId}</td>
        <td>${status.message}</td>
        <td>${status.createdAt}</td>
    </tr>    
</c:forEach>
</tbody>
</table>
</span>
</body>
</html>
