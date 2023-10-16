
function searchBlog(e) {
var count=0
    count++
    console.log(count)
    var values = [];
debugger
    var checkedBoxes = document.querySelectorAll('input[name=check]:checked');
    var values = [];
debugger
    for(i=0;i<checkedBoxes.length;i++)
    {
        values.push(checkedBoxes[i].value)
    }
    /*  var select = document.getElementById('AemCategory');
    var selected = [...select.options]
                    .filter(option => option.selected)
                    .map(option => option.value);
    for(i=0;i<selected.length;i++)
        values.push(selected[i])*/



var parameterName = "category";
var url = "/content/trigger-servlet";

// Construct the query string
var queryString = values.map(function(value) {
  return parameterName + "=" + encodeURIComponent(value);
}).join("&");

// Append the query string to the URL
url += "?" + queryString+"&"+"offset="+0;

    debugger
      var b=document.getElementById("mainDropdown");
    var sub=document.getElementById("subDropdown");

    if(url!=""){
        if(url.value!=""){
        const xhttp = new XMLHttpRequest();
    xhttp.onload = function () {
      let searchResults = JSON.parse(this.responseText);
      totalDiv = "";
      for (let i = 0; i < searchResults.length; i++) {
          debugger
        totalDiv +=
            '  <a href="'+searchResults[i].image+'">  <div style="background-color: aqua;background: #EBECF0; box-shadow: 2px 4px 18px rgba(0, 0, 0, 0.06); border-radius: 12px;padding: 8px 16px 8px 8px;display:flex;align-items:center;gap:30px;"><img src="' +
          searchResults[i].image +
          '" height="100px" width="100px"/><p class="heading-topic">' +
          searchResults[i].profession +"<br>"+searchResults[i].tag+
          '</p></div><p></a>';
      }
        document.getElementById("blog12").innerHTML = totalDiv;
     
    };
    xhttp.open(
      "GET",
      url,
      true
    );
    xhttp.send();
    }
    else
    {
           url ="/content/trigger-servlet?category="+b.value;
        const xhttp = new XMLHttpRequest();
    xhttp.onload = function () {
      let searchResults = JSON.parse(this.responseText);
      totalDiv = "";
      for (let i = 0; i < searchResults.length; i++) {
          debugger
        totalDiv +=
            '  <a href="'+searchResults[i].image+'">  <div style="background-color: aqua;background: #EBECF0; box-shadow: 2px 4px 18px rgba(0, 0, 0, 0.06); border-radius: 12px;padding: 8px 16px 8px 8px;display:flex;align-items:center;gap:30px;"><img src="' +
          searchResults[i].image +
          '" height="100px" width="100px"/><p class="heading-topic">' +
          searchResults[i].profession +"<br>"+searchResults[i].tag+
          '</p></div><p></a>';
      }
      document.getElementById("blog12").innerHTML = totalDiv;
    };
    xhttp.open(
      "GET",
      url,
      true
    );
    xhttp.send();

    }
}else {
      document.getElementById("blog12").innerHTML = "No Results Found!";
    }

}
