<!DOCTYPE html>
<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

    <style>
        .books {
            font-size: 16pt;
            text-align: center;
            color: #ff1a13;
        }
        td {
            border: #000000 solid 2px;
            padding: 5px;
        }
    </style>

</head>
<body>

    <div class="books">
        <#if books??>
        <table>
            <#list books as book>
                <tr>
                    <td>${book.name}</td>
                    <td>${book.description}</td>
                    <td>${book.author}</td>
                </tr>
            </#list>
        </table>
        <#else>
            <p>There is no books yet =(</p>
        </#if>
    </div>

</body>
</html>