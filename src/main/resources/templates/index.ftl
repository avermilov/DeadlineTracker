<#-- @ftlvariable name="entries" type="kotlin.collections.List<com.jetbrains.handson.website.BlogEntry>" -->
<!DOCTYPE html>
<html lang="en">
<head>
    <title style="color: crimson">Deadline Tracker</title>
    <style>
        body {
            background-image: url("/static/deadline.gif");
            background-size: 50vw 100vh;
        }
    </style>
</head>
<body style="text-align: center; font-family: sans-serif">
<img src="/static/true_deadline.gif" alt="this is fine">
<p style="color: crimson"><i><b>Powered by burning deadlines and mental decomposition!</b></i></p>
<h2 style="color: crimson">Deadlines:</h2>
<#list entries as item>
    <div>
        <h3 style="color: crimson">${item.headline}</h3>
        <p style="color: crimson">${item.body}</p>
    </div>
<#else>
    <p style="color: crimson">NO DEADLINES!</p>
</#list>
<div>
    <h3 style="color: crimson">Add a burning deadline!</h3>
    <form action="/submit" method="post">
        <input type="text" name="headline">
        <br>
        <textarea name="body"></textarea>
        <br>
        <input type="submit" value="Add!">
    </form>
</div>
<div>
    <h3 style="color: crimson">Remove a dead deadline!</h3>
    <form action="/removedeadline" method="post">
        <input type="text" name="deleteIndex">
        <br>
        <#--        <textarea name="body"></textarea>-->
        <#--        <br>-->
        <input type="submit" value="Remove!">
    </form>
</div>
<div>
    <h3 style="color: crimson">Remove all the deadlines!</h3>
    <form action="/removeall" method="post">
        <input type="submit" value="Clear!">
    </form>
</div>
</body>
</html>