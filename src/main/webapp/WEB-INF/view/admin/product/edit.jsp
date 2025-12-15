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
                <title>Product - Hỏi Dân IT</title>
                <link href="https://cdn.jsdelivr.net/npm/simple-datatables@7.1.2/dist/style.min.css" rel="stylesheet" />
                <link href="/admin/css/styles.css" rel="stylesheet" />
                <script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js" crossorigin="anonymous"></script>
                <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
                <script>
                    $(document).ready(() => {
                        const avatarFile = $("#avatarFile");
                        const originalImage = "${updateProduct.image}";
                        if (originalImage) {
                            const urlImage = "/admin/images/product/" + originalImage;
                            $("#avatarPreview").attr("src", urlImage);
                            $("#avatarPreview").css({ "display": "block" });
                        }

                        avatarFile.change(function (e) {
                            const imgURL = URL.createObjectURL(e.target.files[0]);
                            $("#avatarPreview").attr("src", imgURL);
                            $("#avatarPreview").css({ "display": "block" });
                        });
                    });
                </script>

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
                                <h1 class="mt-4">Product</h1>
                                <ol class="breadcrumb mb-4">
                                    <li class="breadcrumb-item active">Product</li>
                                </ol>
                                <form:form action="/admin/product/updateProduct" method="post"
                                    modelAttribute="updateProduct" enctype="multipart/form-data">
                                    <div class="container mt-5">
                                        <div class="col-md-6 col-12 mx-auto">
                                            <h3>Edit a Product</h3>
                                            <hr>
                                            <form:hidden path="id" />
                                            <div class="row">
                                                <div class="col-md-6">
                                                    <div class="mb-3">
                                                        <label for="name" class="form-label">Name:</label>
                                                        <form:input type="text" class="form-control" path="name"
                                                            cssErrorClass="form-control is-invalid" />
                                                        <form:errors path="name" cssClass="invalid-feedback" />
                                                    </div>
                                                </div>
                                                <div class="col-md-6">
                                                    <div class="mb-3">
                                                        <label for="price" class="form-label">Price:</label>
                                                        <form:input type="number" class="form-control" path="price"
                                                            cssErrorClass="form-control is-invalid" />
                                                        <form:errors path="price" cssClass="invalid-feedback" />
                                                    </div>
                                                </div>
                                            </div>

                                            <div class="row">
                                                <div class="col-md-12">
                                                    <label for="detailDesc" class="form-label">Detail
                                                        description:</label>
                                                    <form:textarea type="text" class="form-control" path="detailDesc"
                                                        rows="3" cssErrorClass="form-control is-invalid" />
                                                    <form:errors path="detailDesc" cssClass="invalid-feedback" />
                                                </div>
                                            </div>

                                            <div class="row">
                                                <div class="col-md-6">
                                                    <div class="mb-3">
                                                        <label for="quantity" class="form-label">Quantity:</label>
                                                        <form:input type="number" class="form-control" path="quantity"
                                                            cssErrorClass="form-control is-invalid" />
                                                        <form:errors path="quantity" cssClass="invalid-feedback" />
                                                    </div>
                                                </div>
                                                <div class="col-md-6">
                                                    <div class="mb-3">
                                                        <label for="shortDesc" class="form-label">Short
                                                            description:</label>
                                                        <form:input type="text" class="form-control" path="shortDesc"
                                                            cssErrorClass="form-control is-invalid" />
                                                        <form:errors path="shortDesc" cssClass="invalid-feedback" />
                                                    </div>
                                                </div>
                                            </div>

                                            <div class="row">
                                                <div class="col-md-6">
                                                    <div class="mb-3">
                                                        <label for="factory" class="form-label">Factory:</label>
                                                        <form:select class="form-select"
                                                            aria-label="Default select example" path="factory">
                                                            <form:option value="Asus">Asus</form:option>
                                                            <form:option value="Dell">Dell</form:option>
                                                            <form:option value="Apple">Apple</form:option>
                                                        </form:select>
                                                    </div>
                                                </div>
                                                <div class="col-md-6">
                                                    <div class="mb-3">
                                                        <label for="target" class="form-label">Target:</label>
                                                        <form:select class="form-select"
                                                            aria-label="Default select example" path="target">
                                                            <form:option value="Office">Văn phòng</form:option>
                                                            <form:option value="Gaming">Gaming</form:option>
                                                            <form:option value="Macbook">Macbook</form:option>
                                                        </form:select>
                                                    </div>
                                                </div>
                                                <!-- <div class="col-md-6">
                                                        <div class="mb-3">
                                                            <label for="sold" class="form-label">Sold:</label>
                                                            <form:input type="text" class="form-control" path="sold" />
                                                        </div>
                                                    </div> -->
                                            </div>

                                            <div class="row">
                                                <div class="mb-3">
                                                    <label for="avatarFile" class="form-label">Choose
                                                        Images:</label>
                                                    <input class="form-control" type="file" id="avatarFile"
                                                        accept=".png, .jpg, .jpeg" name="hoidanitFile" />
                                                </div>
                                            </div>
                                            <div class="row">
                                                <div class="mb-3">
                                                    <img id="avatarPreview" src="" alt="Avatar Preview"
                                                        style="display: none; max-width: 200px;">
                                                </div>
                                            </div>
                                            <button type="submit" class="btn btn-primary">Update</button>
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
            </body>

            </html>