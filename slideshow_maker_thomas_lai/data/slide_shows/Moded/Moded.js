document.getElementById("sus").innerHTML = "Moded";
var firstOne = createNodes("I+have+a+.gif+from+this+series+but+have+no+_9a1a0d488fecbfc3c3efee38bc745ab1.gif","yooooo");
var nodetemp = firstOne;
        nodular = createNodes("I+have+a+.gif+from+this+series+but+have+no+_9a1a0d488fecbfc3c3efee38bc745ab1.gif","yooooo");
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

document.getElementById("butt").disabled = true;
var currentNode = firstOne;
var nodular;
var timer=null;
document.getElementById("img").src = "img/"+currentNode.image;
document.getElementById("p1").innerHTML = currentNode.caption;
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
        document.getElementById("butt2").disabled = true;
    else
        document.getElementById("butt2").disabled = false;
    document.getElementById("butt").disabled = false;
    document.getElementById("img").src = "img/" +currentNode.image;
    document.getElementById("p1").innerHTML = currentNode.caption;
}
function react() {
    var imgHolder = document.getElementById("imgButt");
        if(imgHolder.src.match("play")){
               imgHolder.src = "img/pause.jpg"
               timer = setInterval(nextSlide,5500);
}
        else{
              imgHolder.src = "img/play.jpg";
              clearInterval(timer);
} 
    }
function prevSlide() {
    currentNode = currentNode.prev;
    if (currentNode.prev == 'null')
        document.getElementById("butt").disabled = true;
    else
        document.getElementById("butt").disabled = false;
    document.getElementById("butt2").disabled = false;
    document.getElementById("img").src = "img/" +currentNode.image;
    document.getElementById("p1").innerHTML = currentNode.caption;
}