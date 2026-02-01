<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

            <!DOCTYPE html>
            <html lang="en">

            <head>
                <meta charset="utf-8" />
                <meta http-equiv="X-UA-Compatible" content="IE=edge" />
                <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
                <meta name="description" content="Hỏi Dân IT - Dự án laptopshop" />
                <meta name="author" content="Hỏi Dân IT" />
                <title>User - Hỏi Dân IT</title>
                <link href="https://cdn.jsdelivr.net/npm/simple-datatables@7.1.2/dist/style.min.css" rel="stylesheet" />
                <link href="/admin/css/styles.css" rel="stylesheet" />
                <script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js" crossorigin="anonymous"></script>
            </head>

            <body class="sb-nav-fixed">
                <!-- Header -->
                <jsp:include page="../layout/header.jsp" />
                <div id="layoutSidenav">
                    <!-- Sidebar -->
                    <jsp:include page="../layout/sidebar.jsp" />
                    <div id="layoutSidenav_content">
                        <main>
                            <div class="container-fluid px-4">
                                <h1 class="mt-4">User</h1>
                                <ol class="breadcrumb mb-4">
                                    <li class="breadcrumb-item active">User</li>
                                </ol>
                                <form:form action="/admin/user/updateUser" method="post" modelAttribute="updateUser"
                                    enctype="multipart/form-data">
                                    <div class="container mt-5">
                                        <div class="row">
                                            <div class="col-md-6 col-12 mx-auto">
                                                <h3>
                                                    Edit User
                                                </h3>
                                                <hr>
                                                <form:hidden path="id" />
                                                <div class="mb-3">
                                                    <label class="form-label">Role:</label>
                                                    <form:select class="form-control" path="role.id">
                                                        <c:forEach var="role" items="${roles}">
                                                            <form:option value="${role.id}"
                                                                selected="${updateUser.role.id == role.id ? 'selected' : ''}">
                                                                ${role.name}
                                                            </form:option>
                                                        </c:forEach>
                                                    </form:select>
                                                </div>
                                                <div class="mb-3">
                                                    <label for="exampleInputEmail1" class="form-label">Email:</label>
                                                    <form:input type="email" class="form-control" path="email"
                                                        readonly="true" />
                                                </div>
                                                <div class="mb-3">
                                                    <label for="exampleInputPassword1"
                                                        class="form-label">Password:</label>
                                                    <form:input type="text" class="form-control" path="password" />
                                                </div>
                                                <div class="mb-3">
                                                    <label for="exampleInputPassword1" class="form-label">Phone
                                                        Number:</label>
                                                    <form:input type="text" class="form-control" path="phone" />
                                                </div>
                                                <div class="mb-3">
                                                    <label for="exampleInputPassword1" class="form-label">Full
                                                        Name:</label>
                                                    <form:input type="text" class="form-control" path="fullName" />
                                                </div>
                                                <div class="mb-3">
                                                    <label for="exampleInputPassword1"
                                                        class="form-label">Address:</label>
                                                    <form:input type="text" class="form-control" path="address" />
                                                </div>
                                                <div class="mb-3">
                                                    <label class="form-label">Age:</label>
                                                    <form:input type="number" class="form-control" path="age" />
                                                </div>
                                                
                                                <%-- <div class="mb-3">
                                                    <label class="form-label">Avatar:</label>
                                                    <c:if test="${not empty updateUser.avatar}">
                                                        <div class="mb-2">
                                                            <img id="currentAvatar"
                                                                src="/admin/images/avatar/${updateUser.avatar}"
                                                                style="max-width: 150px; height: auto;"
                                                                alt="Current Avatar" class="img-thumbnail">
                                                        </div>
                                                    </c:if>
                                                    <c:if test="${empty updateUser.avatar}">
                                                        <img id="currentAvatar"
                                                            style="max-width: 150px; height: auto; display: none;"
                                                            alt="Current Avatar" class="img-thumbnail">
                                                    </c:if>
                                                    <input type="file" class="form-control" id="hoidanitFile"
                                                        name="hoidanitFile" accept="image/*"
                                                        onchange="previewImage()" />
                                                    <small class="form-text text-muted">Chọn ảnh để thay đổi avatar
                                                        (không bắt buộc)</small>
                                                    <div class="mt-2">
                                                        <img id="previewImage"
                                                            style="max-width: 150px; height: auto; display: none;"
                                                            alt="Preview Avatar" class="img-thumbnail">
                                                    </div>
                                            </div> --%>

                                            <button type="submit" class="btn btn-primary">Update
                                                User</button>
                                        </div>
                                    </div>
                            </div>
                            </form:form>
                    </div>
                    </main>
                    <jsp:include page="../layout/footer.jsp" />
                </div>
                </div>
                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"
                    crossorigin="anonymous"></script>
                <script src="/admin/js/scripts.js"></script>
                <script>
                    function previewImage() {
                        const fileInput = document.getElementById('hoidanitFile');
                        const previewImage = document.getElementById('previewImage');
                        const currentAvatar = document.getElementById('currentAvatar');

                        if (fileInput.files && fileInput.files[0]) {
                            const reader = new FileReader();
                            reader.onload = function (e) {
                                previewImage.src = e.target.result;
                                previewImage.style.display = 'block';
                                if (currentAvatar) {
                                    currentAvatar.style.display = 'none';
                                }
                            }
                            reader.readAsDataURL(fileInput.files[0]);
                        } else {
                            previewImage.style.display = 'none';
                            if (currentAvatar) {
                                currentAvatar.style.display = 'block';
                            }
                        }
                    }
                </script>
            </body>

            </html>