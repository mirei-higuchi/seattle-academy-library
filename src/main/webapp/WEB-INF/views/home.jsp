<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<%@ page contentType="text/html; charset=utf8"%>
<%@ page import="java.util.*"%>
<html>
<head>
<title>ホーム｜シアトルライブラリ｜シアトルコンサルティング株式会社</title>
<link href="<c:url value="/resources/css/reset.css" />" rel="stylesheet" type="text/css">
<link href="https://fonts.googleapis.com/css?family=Noto+Sans+JP" rel="stylesheet">
<link href="<c:url value="/resources/css/default.css" />" rel="stylesheet" type="text/css">
<link href="https://use.fontawesome.com/releases/v5.6.1/css/all.css" rel="stylesheet">
<link href="<c:url value="/resources/css/home.css" />" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.6.4/css/all.css">
</head>
<body class="wrapper">
    <header>
        <div class="left">
            <img class="mark" src="resources/img/logo.png" />
            <div class="logo">Seattle Library</div>
        </div>
        <div class="right">
            <ul>
                <li><a href="<%=request.getContextPath()%>/home" class="menu">Home</a></li>
                <li><a href="<%=request.getContextPath()%>/">ログアウト</a></li>
            </ul>
        </div>
    </header>
    <main>
        <h1>Home</h1>
        <a href="<%=request.getContextPath()%>/addBook" class="btn_add_book">書籍の追加</a> <a href="<%=request.getContextPath()%>/bulkRegist" class="btn_bulk_book">一括登録</a> <a href="<%=request.getContextPath()%>/lendingBook" class="btn_lending_book">貸出可書籍一覧 </a>
        <form action="searchBook" method="post">
            <span style="”line-height: 200%;">検索したいタイトルを入力してください。</span> <input type="search" name="search" placeholder="キーワードを入力"> <input class="searchBook" type="submit" name="submit" value="検索">
            <center>
                <c:if test="${!empty noLendingMessage}">
                    <div class="error">${noLendingMessage}</div>
                </c:if>
            </center>
            <center>
                <c:if test="${!empty errorMessage}">
                    <div class="error">${errorMessage}</div>
                </c:if>
            </center>
        </form>
        <div class="content_body">
            <c:if test="${!empty resultMessage}">
                <div class="error_msg">${resultMessage}</div>
            </c:if>
            <div>
                <c:forEach var="bookInfo" items="${bookList}">
                    <div class="books">
                        <div class="book_box">
                            <form method="post" class="book_thumnail" action="<%=request.getContextPath()%>/details">
                                <a href="javascript:void(0)" onclick="this.parentNode.submit();"> <c:if test="${bookInfo.thumbnail =='null'}">
                                        <img class="book_noimg" src="resources/img/noImg.png">
                                    </c:if> <c:if test="${bookInfo.thumbnail !='null'}">
                                        <img class="book_noimg" src="${bookInfo.thumbnail}">
                                    </c:if>
                                </a> <input type="hidden" name="bookId" value="${bookInfo.bookId}">
                            </form>
                            <ul>
                                <li class="book_title">${bookInfo.title}</li>
                                <li class="book_author">${bookInfo.author}</li>
                                <li class="book_publish_Date">${bookInfo.publishDate}</li>
                                <li class="book_publisher">${bookInfo.publisher}</li>
                            </ul>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
        </div>
        <div id="page_top">
            <a href="#"></a>
        </div>
        </div>
        </div>
        </div>
    </main>
</body>
</html>