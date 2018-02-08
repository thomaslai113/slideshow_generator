/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var req;
var obj;
try{req = new XMLHttpRequest();}catch(err){alert("Error");}
$.getJSON("file:///"+slideShow.getTitle()+".json",function(data){
    var obj = JSON.parse(data);
}); 
document.getElementById(h1).InnerHtml = obj.title;
var node1 = obj.slides[0];
alert(node1.caption);
var nodetemp = node1;

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



