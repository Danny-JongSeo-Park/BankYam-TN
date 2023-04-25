<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


<head>
    <title>뱅크얌</title>
    <link rel="stylesheet" type="text/css" href="/css/nav.css" />
    <link rel="stylesheet" type="text/css" href="/css/main.css" />
    <link rel="stylesheet" type="text/css" href="/css/footer.css" />
    <link rel="stylesheet" type="text/css" href="/css/login.css" />
    <link rel="stylesheet" type="text/css" href="/css/join.css" />
    <link rel="stylesheet" type="text/css" href="/css/profile.css" />
    <script type="text/javascript" src="https://code.jquery.com/jquery-3.2.0.min.js" ></script>
    <script type="text/javascript" language="javascript"
    			src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.0/jquery.min.js"></script>
    <script src="/js/trim.js"></script>
    <script language="javascript">
        function openTrPop(){
            var tr_width = '730';
            var tr_height = '730';
            var tr_left = Math.ceil(( window.screen.width - tr_width )/2);
            var tr_top = Math.ceil(( window.screen.height - tr_height )/2);
            var popup = window.open('/account/transfer','transfer', 'width='+ tr_width +', height='+ tr_height +', left=' + tr_left + ', top='+ tr_top);
        }
    </script>
</head>
<body style="margin: 0;">
    <center>
    <nav class="nav-main">
        <header class="nav-header">
            <div class="nav-header-1">
                <a href="/" title="메인 화면"><img src="/img/YamLogo.png" class="nav-image"></a>
                <a href="/" title="메인 화면"><img src="/img/YamLogoHover.png" class="nav-image-hover"></a>
            </div>
            <div class="nav-header-1">
            <ul class="nav-menu">
                <li class="nav-menu-item menu-show" id="nav-post">
                    <a href="#">뱅킹</a>
                    <div class="nav-post-box">
                        <c:if test="${sessionScope.membery eq null}">
                            <a href="/account/transactionList" target="_self" class="hidden-a">이체</a>
                        </c:if>
                        <c:if test="${sessionScope.membery ne null}">
                            <a onclick="openTrPop()" target="_blank" class="hidden-a">이체</a>
                        </c:if>
                        <a class="hidden-a" href="/account/transactionList">거래내역</a>
                        <a class="hidden-a" href="/accountM/accounts">계좌관리</a>
                    </div>
                </li>
                <li class="nav-menu-item menu-show" id="nav-post">
                    <a href="#">얌톡</a>
                    <div class="nav-post-box">
                        <a class="hidden-a" href="/chat/list">대화방</a>
                        <a class="hidden-a" href="/friend/friends?content=list">친구관리</a>
                    </div>
                </li>
                <li class="nav-menu-item menu-show-full">
                    <a href="">뱅크얌</a>
                </li>
                <li class="nav-menu-item log-btn">
                    <c:if test="${sessionScope.membery eq null}">
                        <button class="log-btn-full" onclick="location.href='/member/join'">JOIN</button>
                    </c:if>
                    <c:if test="${sessionScope.membery ne null}">
                        <button class="log-btn-full" onclick="location.href='/member/profile'">PROFILE</button>
                    </c:if>
                </li>
                <li class="nav-menu-item log-btn">
                    <c:if test="${sessionScope.membery eq null}">
                        <button class="log-btn-full" onclick="location.href='/member/login'">LOGIN</button>
                    </c:if>
                    <c:if test="${sessionScope.membery ne null}">
                        <button class="log-btn-full" onclick="location.href='/member/logout_ok'">LOGOUT</button>
                    </c:if>
                </li>
            </ul>
            <c:if test="${sessionScope.membery eq null}">
            <div class="response-login"><button class="response-login-button" onclick="location.href='/member/login'"><img src="/img/login.png" id="login-image" style="width:100%;height:100%;object-fit:cover;"/></button></div>
            </c:if>
            <c:if test="${sessionScope.membery ne null}">
                <div class="response-login"><button class="response-login-button" onclick="location.href='/member/profile'"><img src="/img/login.png" id="login-image" style="width:100%;height:100%;object-fit:cover;"/></button></div>
            </c:if>
            </div>
        </header>
    </nav>
    </center>
</body>
