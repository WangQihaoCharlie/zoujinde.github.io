<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<HTML>
<title>Sign In</title>
<%@ include file="head.jsp"%>
<div style="width:100%; margin:auto; overflow:auto;">
  <br><br><br><br><br>
  <label>User name</label><input id="text_user"/><br><br>
  <label>Password </label><input type="password" id="text_pass"/><br>
  <br><br>
  <input type="button" onclick="signIn()" value="Sign In" style="width:910px;"/>
  <hr style="font-size:1px;">
  <label id="result" style="width:910px;"/>
</div>
</HTML>

<script type="text/javascript">
  var httpRequest = getHttpRequest();
  var result = document.getElementById("result");

  // Sign in
  function signIn() {
    var user = document.getElementById("text_user").value.trim();
    var pass = document.getElementById("text_pass").value.trim();
    if (user.length < 3) {
      var text = "Please input the user name. (length>=3)";
      result.innerText = text;
      alert(text);
      return;
    }
    if (pass.length < 3) {
      var text = "Please input the password. (length>=3)";
      result.innerText = text;
      alert(text);
      return;
    }
    var json = {'act':'signIn', 'user_name':user, 'password':pass};
    json = JSON.stringify(json);
    // Post URL is Servlet, the sync is true
    httpRequest.open("POST", "user", true);
    // Only post method needs to set header
    httpRequest.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    // Set callback
    httpRequest.onreadystatechange = signInResult;
    httpRequest.send(json);
  }

  // Sign in result
  function signInResult() {
    if(httpRequest.readyState==4) {
      var text = httpRequest.responseText.trim();
      if(httpRequest.status==200) { // 200 OK
        if (text.endsWith('.jsp')) {
          window.location.href = text;
        } else {
          result.innerText = text;
          alert(text);
        }
      } else {
        text = httpRequest.status + text;
        result.innerText = text;
        alert(text);
      }
    }
  }

</script>
