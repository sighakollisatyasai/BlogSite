function searchBlog1(e) {
  /*  var checkedBoxes = document.querySelectorAll('input[name=check]:checked');
    var values = [];
debugger
    for(i=0;i<checkedBoxes.length;i++)
    {
        values.push(checkedBoxes[i].value)
    }*/
          var values = [];
console.log("Hello"+document.getElementById('aem-software').value)
if(document.getElementById('search').value==""){
    debugger
if(document.getElementById("aem-software").value!=""){
    var select = document.getElementById('aem-software');}
if(document.getElementById("doctor").value!=""){
    var select = document.getElementById('doctor');}
    if(document.getElementById('enterprise').value!=""){
    var select = document.getElementById('enterprise');}
    if(document.getElementById('software').value!=""){
    var select = document.getElementById('software');}
    if(document.getElementById('dropdown').value!=""){
    var select = document.getElementById('dropdown');}
    var selected = [...select.options]
                    .filter(option => option.selected)
                    .map(option => option.value);
    for(i=0;i<selected.length;i++)
        values.push(selected[i])



var parameterName = "category";
var url = "/content/trigger-servlet";

// Construct the query string
var queryString = values.map(function(value) {
  return parameterName + "=" + encodeURIComponent(value);
}).join("&");

// Append the query string to the URL
url += "?" + queryString+"&"+"offset="+e;
}
    else
    {
        debugger
        if(document.getElementById('search').value.length>3)
        {
        var url = "/content/trigger-servlet";
        url+="?"+"search="+document.getElementById('search').value;
        }
    }

    debugger
    var b=document.getElementById("mainDropdown");
    var sub=document.getElementById("subDropdown");

    if(url!=""){
        const xhttp = new XMLHttpRequest();
    xhttp.onload = function () {
      let searchResults = JSON.parse(this.responseText);
      totalDiv = "";
      for (let i = 0; i < searchResults.length; i++) {
          debugger
        totalDiv +=
            '  <a href="'+searchResults[i].path+'.html'+'">  <div style="background-color: aqua;background: #EBECF0; box-shadow: 2px 4px 18px rgba(0, 0, 0, 0.06); border-radius: 12px;padding: 8px 16px 8px 8px;display:flex;align-items:center;gap:30px;"><img src="' +
          searchResults[i].image +
          '" height="100px" width="100px"/><p class="heading-topic">' +
          searchResults[i].profession +"<br>"+searchResults[i].tag+
          '</p></div><p></a>';
      }
         if(e<6){
      document.getElementById("test").innerHTML = totalDiv;
        }
        else
        {
            debugger
    $('#test').append(totalDiv)
        }
        if(searchResults.length==0)
        {
            document.getElementById("test").innerHTML = "<div style='color:red'>"+"No Results Found"+"</div>";

        }
    };
    xhttp.open(
      "GET",
      url,
      true
    );
    xhttp.send();


}else {
      document.getElementById("blog12").innerHTML = "No Results Found!";
    }

}

function showCheckBoxes()
{
    document.getElementById("checkBoxes").style.display="block";
}
function test()
{
    console.log(document.getElementById("dropdown").value)
}
var count=0;
function hitcount()
{
    debugger


    count+=6
        console.log("Hello"+count);
        searchBlog1(count)

}