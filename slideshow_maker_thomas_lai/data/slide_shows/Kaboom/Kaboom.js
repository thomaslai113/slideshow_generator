document.getElementById("h1").InnerHtml = "Kaboom" + "\n";
var firstOne = createNodes("I+have+a+.gif+from+this+series+but+have+no+_9a1a0d488fecbfc3c3efee38bc745ab1.gif","");
var nodetemp = firstOne;
        nodular = createNodes("I+have+a+.gif+from+this+series+but+have+no+_9a1a0d488fecbfc3c3efee38bc745ab1.gif","");
    setNext(nodetemp, nodular);
    nodetemp = nodular;
        nodular = createNodes("ICP.jpg","Blurry");
    setNext(nodetemp, nodular);
    nodetemp = nodular;
        nodular = createNodes("Icon-Presentation.jpg","Straight");
    setNext(nodetemp, nodular);
    nodetemp = nodular;
        nodular = createNodes("SignalHillNewfoundland.jpg","");
    setNext(nodetemp, nodular);
    nodetemp = nodular;
        nodular = createNodes("GreatPlainsNebraska.jpg","");
    setNext(nodetemp, nodular);
    nodetemp = nodular;
        nodular = createNodes("Icon-Presentation.jpg","");
    setNext(nodetemp, nodular);
    nodetemp = nodular;
        nodular = createNodes("Icon-Presentation.jpg","");
    setNext(nodetemp, nodular);
    nodetemp = nodular;
        nodular = createNodes("Icon-Presentation.jpg","");
    setNext(nodetemp, nodular);
    nodetemp = nodular;

document.getElementById("butt2").disable = true;
var currentNode = firstOne;
var nodular;
document.getElementById("img").src = "img/"+currentNode.image;
document.getElementById("p1").InnerHtml = currentNode.caption;
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
        document.getElementById("butt").disable = true;
    else
        document.getElementById("butt").disable = false;
    document.getElementById("butt2").disable = false;
    document.getElementById("img").src = "img/" +currentNode.image;
    document.getElementById("p1").InnerHtml = currentNode.caption;
}
function prevSlide() {
    currentNode = currentNode.prev;
    if (currentNode.prev == 'null')
        document.getElementById("butt2").disable = true;
    else
        document.getElementById("butt2").disable = false;
    document.getElementById("butt").disable = false;
    document.getElementById("img").src = "img/" +currentNode.image;
    document.getElementById("p1").InnerHtml = currentNode.caption;
}