var resultData;
var mongoData;
var pastDataDetails;
var fileContent;
function uploadFile(){
    var fileUploaded = document.getElementById("file");
    if ('files' in fileUploaded) {
        if(fileUploaded.files.length>0){
            for (var i = 0; i < fileUploaded.files.length; i++) {
                var uploadFile = fileUploaded.files[i];
                if (uploadFile!=null) {
                    var fd = new FormData();
                    fd.append( 'xmlFile', uploadFile );
                    $.ajax({
                        url: 'http://localhost:8000/vehicle/readxml',
                        data: fd,
                        processData: false,
                        contentType: false,
                        type: 'POST',
                        success: function(data){
                        resultData = data;
                        localStorage.setItem("data", JSON.stringify(data.data));
                        redirectPage(resultData);
                        }
                    });
                }
            }
        }
        else{
            alert('Could not upload the file.');
        }
    }
    else{
        alert('Please select file for upload.');
    }

}

function redirectPage(resultData){
    window.location.href = "DetailsPage.html";
}

function addTableValues(){
resultData = localStorage.getItem("data");
var result = JSON.parse(resultData);
var keys = [];
    for(var k in result[0]) keys.push(k);
    var labelNames=jsonstr;
    var tHead = document.getElementById('tHead');
    for(var i=0; i<keys.length; i++){
        var td = document.createElement('td');
        for(var j=0; j<labelNames.length; j++){
        if(labelNames[j].key == keys[i]){
            var text = document.createTextNode(labelNames[j].value);
        td.appendChild(text);
        tHead.appendChild(td);
        }
        }
    }
    var tBody = document.getElementById('tBody');
    for(var t=0; t<result.length; t++){
        var data = [];
        data.push(result[t]);
        var kdata = [];
        var individualData = data[0];
        for(var keyName in individualData) {
            if(keyName != 'fileContent'){
                if(keyName == 'timestampGeneration' || keyName == 'timestampUpload'){
                    var date = new Date(individualData[keyName]);
                    kdata.push(date.toString());
                } else if(keyName == 'wheels'){
                    var wheelsData ='';
                    var wheelList = individualData[keyName][0];
                    var materialList = wheelList.materialList;
                    wheelsData += materialList.length+" ";
                    wheelsData += materialList[0].material+" ";
                    var positionList = wheelList.positionList;
                    for(var i =0; i<positionList.length; i++){
                        wheelsData += positionList[i].position+" ";
                    }
                    kdata.push(wheelsData);
                } else{
                kdata.push(individualData[keyName]);
                }
            } else{
                fileContent = individualData[keyName];
            }
        }
        var tr = document.createElement('tr');
        for(var i=0; i<kdata.length; i++){
            if(kdata[i]!=null){
            if(kdata[i].endsWith(".xml")){
            var td = document.createElement('td');
            var text = document.createTextNode(kdata[i]);
            var filename = kdata[i];
            var link = document.createElement("a");
            link.setAttribute("attr", filename);
            link.onclick = function() {
            var name = this.getAttribute("attr");
            processModal();
            }
            link.appendChild(text);
            td.appendChild(link);
            tr.appendChild(td);
            tBody.appendChild(tr);
            }else{
                var td = document.createElement('td');
                var text = document.createTextNode(kdata[i]);
                td.appendChild(text);
                tr.appendChild(td);
                tBody.appendChild(tr);
            }
        }}
    }
    var vehicleNames = [], output = [], flags = [];
    for(var k in result) vehicleNames.push(result[k].vehicleName);
    var a = [], b = [], prev;
    vehicleNames.sort();
    for ( var i = 0; i < vehicleNames.length; i++ ) {
        if ( vehicleNames[i] !== prev ) {
            a.push(vehicleNames[i]);
            b.push(1);
        } else {
            b[b.length-1]++;
        }
        prev = vehicleNames[i];
    }
    for( var i=0; i<a.length; i++){
        var divSummary = document.getElementById('summary');
        var str = "Count of "+a[i]+" : "+b[i];
        var p = document.createElement('p');
        var text = document.createTextNode(str);
        p.appendChild(text);
        divSummary.appendChild(p);
    }
}

function fetchFile(){
    $.ajax({
        url: 'http://localhost:8000/vehicle/getXmlData',
        data: null,
        processData: false,
        contentType: false,
        type: 'GET',
        success: function(data){
            localStorage.setItem("pastData", JSON.stringify(data.data));
            redirectHistoryPage();
        }
    });
}

function redirectHistoryPage(){
    window.location.href = "HistoryPage.html";
}

function addPastHistoryValues(){
    resultData = localStorage.getItem("pastData");
    var result = JSON.parse(resultData);
    var keys = [];
    for(var k in result[0]){
        if((k == 'uploadDate')||(k == 'filename'))
            keys.push(k);
    }
    var labelNames=jsonstr;
    var tHead = document.getElementById('tHead');
    for(var i=0; i<keys.length; i++){
        var td = document.createElement('td');
        for(var j=0; j<labelNames.length; j++){
        if(labelNames[j].key == keys[i]){
            var text = document.createTextNode(labelNames[j].value);
        td.appendChild(text);
        tHead.appendChild(td);
        }
        }
    }
    var tBody = document.getElementById('tBody');
    for(var t=result.length-1; t>=0; t--){
        var data = [];
        data.push(result[t]);
        var kdata = [];
        var individualData = data[0];
        for(var keyName in individualData) {
            if((keyName == 'uploadDate')||(keyName == 'filename'))
            kdata.push(individualData[keyName]);
        }
        var tr = document.createElement('tr');
        for(var i=0; i<kdata.length; i++){
            var td = document.createElement('td');
            if(i==0){
                var text = document.createTextNode(kdata[i]);
                var filename = kdata[i];
                var link = document.createElement("a");
                link.setAttribute("href", "PastDataDetails.html");
                link.setAttribute("attr", filename);
                link.onclick = function() {
                    var name = this.getAttribute("attr");
                    localStorage.setItem("anchorLinkFileName", name);
                    addHistoryDetails();
                }
                link.appendChild(text);
                td.appendChild(link);
                tr.appendChild(td);
                tBody.appendChild(tr);
            }
            else{
                var text = document.createTextNode(kdata[i]);
                td.appendChild(text);
                tr.appendChild(td);
                tBody.appendChild(tr);
            }
        }
    }
}

function addHistoryDetails(){
    var fd = new FormData();
    var anchorLinkFileName = localStorage.getItem("anchorLinkFileName");
    fd.append( 'filename', anchorLinkFileName);
    $.ajax({
        url: 'http://localhost:8000/vehicle/getPastDataDetails',
        data: fd,
        processData: false,
        contentType: false,
        type: 'POST',
        success: function(data){
            pastDataDetails = data;
            localStorage.removeItem("pastDataDetails");
            localStorage.setItem("pastDataDetails", JSON.stringify(data.data));
            addPastTableValues();
        }
    });
}

function addPastTableValues(){
var resultPastData = localStorage.getItem("pastDataDetails");
var result = JSON.parse(resultPastData);
var keys = [];
    for(var k in result[0]) keys.push(k);
    var labelNames=jsonstr;
    var tHead = document.getElementById('tHead');
    for(var i=0; i<keys.length; i++){
        var td = document.createElement('td');
        for(var j=0; j<labelNames.length; j++){
        if(labelNames[j].key == keys[i]){
            var text = document.createTextNode(labelNames[j].value);
        td.appendChild(text);
        tHead.appendChild(td);
        }
        }
    }
    var tBody = document.getElementById('tBody');
    for(var t=0; t<result.length; t++){
        var data = [];
        data.push(result[t]);
        var kdata = [];
        var individualData = data[0];
        for(var keyName in individualData) {
        if(keyName != '_id'){
            if(keyName != 'fileContent'){
                if(keyName == 'timestampGeneration' || keyName == 'timestampUpload' || keyName == 'timeOfUpload' || keyName == 'timeOfReportGeneration'){
                var date = new Date(individualData[keyName]);
                kdata.push(date.toString());
                }
                else{
                kdata.push(individualData[keyName]);
                }
            }else{
                fileContent = individualData[keyName];
            }
        }
        }
        var tr = document.createElement('tr');
        for(var i=0; i<kdata.length; i++){
            if(kdata[i]!=null){
            if(kdata[i].endsWith(".xml")){
               var td = document.createElement('td');
               var text = document.createTextNode(kdata[i]);
               var filename = kdata[i];
               var link = document.createElement("a");
               link.setAttribute("attr", filename);
               link.onclick = function() {
                  var name = this.getAttribute("attr");
                  processModal();
               }
               link.appendChild(text);
               td.appendChild(link);
               tr.appendChild(td);
               tBody.appendChild(tr);
            }else{
            var td = document.createElement('td');
            var text = document.createTextNode(kdata[i]);
            td.appendChild(text);
            tr.appendChild(td);
            tBody.appendChild(tr);
            }
            }
        }
    }
    var vehicleNames = [], output = [], flags = [];
    for(var k in result) vehicleNames.push(result[k].vehicleName);
    var a = [], b = [], prev;
    vehicleNames.sort();
    for ( var i = 0; i < vehicleNames.length; i++ ) {
        if ( vehicleNames[i] !== prev ) {
            a.push(vehicleNames[i]);
            b.push(1);
        } else {
            b[b.length-1]++;
        }
        prev = vehicleNames[i];
    }
    for( var i=0; i<a.length; i++){
        var divSummary = document.getElementById('summary');
        var str = "Count of "+a[i]+" : "+b[i];
        var p = document.createElement('p');
        var text = document.createTextNode(str);
        p.appendChild(text);
        divSummary.appendChild(p);
    }
}

function processModal(){
   var modal = document.getElementById('myModal');
   var modalContent = document.getElementById('modalContent');
   var children = modalContent.childNodes;
   for (var i=children.length - 1; i>1; i--) {
    modalContent.removeChild(children[i]);
   }
   var p = document.createElement('p');
   var text = document.createTextNode(fileContent);
   var pre = document.createElement('pre');
   pre.appendChild(text);
   p.appendChild(pre);
   modalContent.appendChild(p);
   modal.style.display = "block";
   var span = document.getElementsByClassName("close")[0];
   span.onclick = function() {
    modal.style.display = "none";
   }
   window.onclick = function(event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
   }
}
