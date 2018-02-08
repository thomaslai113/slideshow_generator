var req = null;
 var obj; 
try{req = new XMLHttpRequest();}catch(err){alert("Error");}
$.getJSON("../ENTER TITLE.json", function(data) {

 obj = data;
alert("loaded")
}); 
$(h1).InnerHtml = obj.title;var node1 = obj.slides[0];
alert(node1.caption);
var nodetemp = node1;
function createNodes(ra) {
    var nodex = {
        prev: null,
        slide: ra,
        next: null
    };
    return nodex
}
function setNext(first, second) {
    first.next = second;
    second.prev = first;
}
for (i = 1; i < obj.slides.length; i++)
{
    var nodular = createNodes(obj.slides[i]);
    setNext(nodetemp, nodular);
    nodetemp = nodular;
}
document.getElementById(butt2).disable = true;
var currentNode = node1;

document.getElementById(img).src = "file:///" + currentNode.slide.image_file_name;
document.getElementById(p1).InnerHtml = currentNode.slide.caption;
function nextSlide() {
    currentNode = currentNode.next;
    if (currentNode.next == 'null')
        document.getElementById(butt).disable = true;
    else
        document.getElementById(butt).disable = false;
    document.getElementById(butt2).disable = false;
    document.getElementById(img).src = "file:///" + currentNode.slide.image_file_name;
    document.getElementById(p1).InnerHtml = currentNode.slide.caption;
}
function prevSlide() {
    currentNode = currentNode.prev;
    if (currentNode.prev == 'null')
        document.getElementById(butt2).disable = true;
    else
        document.getElementById(butt2).disable = false;
    document.getElementById(butt).disable = false;
    document.getElementById(img).src = "file:///" + currentNode.slide.image_file_name;
    document.getElementById(p1).InnerHtml = currentNode.slide.caption;
}
