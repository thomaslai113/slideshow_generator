/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

document.getElementById("h1").InnerHtml = slideShow.getTitle() + "\n";
var firstOne = createNode(ss.getImage(),ss.getCaption());
document.getElementById(img).src = "file:///" + currentNode.image;
document.getElementById(p1).InnerHtml = currentNode.slide.caption;
var nodetemp = firstOne;
for (i = 1; i < ss.size(); i++)
{
    var nodular = createNodes(ss.get(i).getImage(),ss.getCaption());
    setNext(firstOne, nodular);
    nodetemp = nodular;
}
document.getElementById(butt2).disable = true;
var currentNode = firstOne;

function createNodes(im,ca){
        var nodex = {
        prev: null,
                image: im,
                caption: ca,
                next: null
        };
    return nodex;
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
    document.getElementById(img).src = "file:///" + currentNode.image;
    document.getElementById(p1).InnerHtml = currentNode.slide.caption;
}
function prevSlide() {
    currentNode = currentNode.prev;
    if (currentNode.prev == 'null')
        document.getElementById(butt2).disable = true;
    else
        document.getElementById(butt2).disable = false;
    document.getElementById(butt).disable = false;
    document.getElementById(img).src = "file:///" + currentNode.image;
    document.getElementById(p1).InnerHtml = currentNode.caption;
}



