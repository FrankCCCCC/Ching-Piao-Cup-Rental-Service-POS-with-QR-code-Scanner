var url = "https://docs.google.com/spreadsheets/d/1V_6nQifHFwepeTE5t0ptMLE7QwLyumC0yNs45iOeCJo/edit#gid=0"; //貼你的試算表共用連結，權限設可編輯
var ss = SpreadsheetApp.openByUrl(url);
var sheetT = ss.getSheetByName("單次借還記錄");
var sheetP = ss.getSheetByName("人別");
var sheetL = ss.getSheetByName("借出紀錄");
var sheetX = ss.getSheetByName("工作表3");
var loop_times = 30; //設定迴圈測試次數
var nData = 2;
var top=2;
var left = 2;
var leftP = 4;
var cP=3;
var cL = 12;

function test(){
  var n = 132468305273;
  for(var i=1; i<=300; i++){
    if(i%5==0){sheetX.getRange(i,1).setValue(n+777);}
    else if(i%5==1){sheetX.getRange(i,1).setValue(n-7542);}
    else if(i%5==2){sheetX.getRange(i,1).setValue(n+542);}
    else if(i%5==3){sheetX.getRange(i,1).setValue((i*10+n)*2);}
    else if(i%5==4){sheetX.getRange(i,1).setValue(n+542-i*100);}
  }
  var rowN = sheetX.getLastRow();
  for(var i=1; i<=rowN; i++){
    if(sheetX.getRange(i, 1).getValue() == sheetX.getRange(59, 1).getValue()){sheetX.getRange(59, 1).setBackground("green");}
    if(sheetX.getRange(i, 1).getValue() == sheetX.getRange(59, 1).getValue()){sheetX.getRange(111, 1).setBackground("green");}
    if(sheetX.getRange(i, 1).getValue() == sheetX.getRange(59, 1).getValue()){sheetX.getRange(123, 1).setBackground("green");}
    if(sheetX.getRange(i, 1).getValue() == sheetX.getRange(59, 1).getValue()){sheetX.getRange(202, 1).setBackground("green");}
    if(sheetX.getRange(i, 1).getValue() == sheetX.getRange(59, 1).getValue()){sheetX.getRange(253, 1).setBackground("green");}
  }
}

function debugL(){
  doPost( {parameter:{action:"Lend", phone:"0908293039", id:"75018849023", nCup:1, nBox:3, nSpoon:1, nFork:1, nChopstick:0}});
}

function debugL1(){
  doPost( {parameter:{action:"Lend", phone:"0975032589", id:"87929234023", nCup:0, nBox:3, nSpoon:4, nFork:4, nChopstick:9}});
}

function debugL2(){
  doPost( {parameter:{action:"Lend", phone:"0936248338", id:"32641088438", nCup:5, nBox:2, nSpoon:7, nFork:8, nChopstick:2}});
}

function debugL3(){
  doPost( {parameter:{action:"Lend", phone:"0936248338", id:"32641088438", nCup:5, nBox:2, nSpoon:7, nFork:8, nChopstick:2}});
}

function debugL4(){
  doPost( {parameter:{action:"Lend", phone:"8+9", id:"8+9", nCup:5, nBox:2, nSpoon:7, nFork:8, nChopstick:2}});
}

function debugR(){
  doPost( {parameter:{action:"Return", phone:"0908293039", id:"75018849023", nCup:1, nBox:3, nSpoon:1, nFork:1, nChopstick:0}});
}

function debugR1(){
  doPost( {parameter:{action:"Return", phone:"", id:"75018849023", nCup:0, nBox:0, nSpoon:0, nFork:0, nChopstick:0}});
}

function debugLL(){
  doPost( {parameter:{action:"LendLog"}});
}

function debugLLT(){
  doPost( {parameter:{action:"LendLogTxt"}});
}

function debugFA(){
  doPost( {parameter:{action:"FindArray", phone:"", id:"5555555555"}});
}

function debugFA1(){
  doPost( {parameter:{action:"FindArray", phone:"", id:"4444232"}});
}

function debugFA2(){
  doPost( {parameter:{action:"FindArray", id:"75018849023"}});
}

function debugFA3(){
  doPost( {parameter:{action:"FindArray", id:"5555555555"}});
}

function debugFA4(){
  doPost( {parameter:{action:"FindArray", phone:"", id:""}});
}

function debugFA5(){
  doPost( {parameter:{action:"FindArray"}});
}

function debugFA6(){
  doPost( {parameter:{action:"FindArray", phone:""}});
}

function debugFA7(){
  doPost( {parameter:{action:"FindArray", id:""}});
}

function debugFAP(){
  doPost( {parameter:{action:"FindArray", phone:"9856348"}});
}

function debugG(){
  doGet( {parameter:{action:"Lend", phone:"0908293039", contain:"123456789012,234567890123", sheetUrl:url, sheetTag:sheetP}});
}

function debugGetTest(){
  doGet( {parameter:{sheetUrl:url, sheetTag:sheetP}});
}

function doGet(e){
  if(e.parameter.action == "Lend"){ return Lend(e); }
  else if(e.parameter.action == "Return"){ return Return(e); }
  else if(e.parameter.action == "LendLog"){ return LendLog(e); }
  else if(e.parameter.action == "LendLogTxt"){ return LendLogTxt(e); }
  else if(e.parameter.action == "FindArray"){ return FindArray(e); }
  else if(e.parameter.action == "FindJson"){ return FindJson(e); }
  else { return GetTest(e); };
}


function doPost(e){
  if(e.parameter.action == "Lend"){ return Lend(e); }
  else if(e.parameter.action == "Return"){ return Return(e); }
  else if(e.parameter.action == "LendLog"){ return LendLog(e); }
  else if(e.parameter.action == "LendLogTxt"){ return LendLogTxt(e); }
  else if(e.parameter.action == "FindArray"){ return FindArray(e); }
  else if(e.parameter.action == "FindJson"){ return FindJson(e); }
  else if(e.parameter.action == "Test"){ return GetTest(e); }
}

function GetTest(e){
  return ContentService.createTextOutput("Sucessful and liver isn't alive finally.");
}

function Lend(e){
  var phone = e.parameter.phone;
  var id = e.parameter.id;
  var nCup = parseInt(e.parameter.nCup); //nCup= +nCup;
  var nBox = parseInt(e.parameter.nBox); //nBox= +nBox;
  var nSpoon = parseInt(e.parameter.nSpoon); //nSpoon= +nSpoon;
  var nFork = parseInt(e.parameter.nFork); //nFork= +nFork;
  var nChopstick = parseInt(e.parameter.nChopstick); //nChopstick= +nChopstick;

  var scanPhone = [{}];
  var scanId = [{}];
  var newRowP, newRowT, newRowL;
  var rowPValues = [{}];
  var rowTValues = new Array();
  var timeLRPhoneId = new Array();
  var time = Utilities.formatDate(new Date(), "GMT+8", "yyyy-MM-dd'  'HH:mm:ss' '");

  scanPhone = sheetP.getRange(top+1, 1, sheetP.getLastRow(), 1).getValues();
  scanId = sheetP.getRange(top+1, 2, sheetP.getLastRow(), 1).getValues();

  newRowP=0;

  if((phone == "" || phone == null) ){
    sheetX.getRange(2, 1).setValue("0");
    if(id != "" && id != null){
      sheetX.getRange(2, 1).setValue("88");
      for(var i=0; i<scanId.length; i++){
        if(id == scanId[i][0]){
          newRowP=i+3;
          phone = "";
          sheetX.getRange(2, 1).setValue("1");
          break;
        }
      }
      if(newRowP == 0) return ContentService.createTextOutput("ID Not Found");
    }else {
      sheetX.getRange(2, 1).setValue("2");
      sheetX.getRange(1, 1).setValue("No Data Input");
      return ContentService.createTextOutput("No Data Input");
    }
  }else{
    if(id != "" && id != null){
      for(var i=0; i<scanPhone.length; i++){
       if(phone == scanPhone[i][0] && id == scanId[i][0]){
         newRowP=i+3;
         sheetX.getRange(2, 1).setValue("3");
         break;
       }
      }
      if(newRowP==0){
        for(var i=0; i<scanPhone.length; i++){
          if(phone == scanPhone[i][0]){
            newRowP=i+3;
            sheetX.getRange(2, 1).setValue("4");
            break;
          }
        }
        if(newRowP!=0 && sheetP.getRange(newRowP, 2).getValue() == ""){ sheetP.getRange(newRowP, 2).setValue(id);}
      }
      if(newRowP==0){
        for(var i=0; i<scanId.length; i++){
          if(id == scanId[i][0]){
            newRowP=i+3;
            sheetX.getRange(2, 1).setValue("1");
            break;
          }
        }
        if(newRowP!=0 && sheetP.getRange(newRowP, 1).getValue() == ""){ sheetP.getRange(newRowP, 1).setValue(phone); }
      }
    }else{
      if(newRowP==0){
        for(var i=0; i<scanPhone.length; i++){
          if(phone == scanPhone[i][0]){
            newRowP=i+3;
            id = "";
            sheetX.getRange(2, 1).setValue("4");
            break;
          }
        }
      }
    }
  }

  /*
  for(var i=0; i<scanPhone.length; i++){
    if(phone == scanPhone[i][0]){
      if(id == scanId[i][0]){
        newRowP=i+3;
      }
    }
  }*/

  if(newRowP==0){
    newRowP = sheetP.getLastRow()+1;
    sheetP.getRange(newRowP, 1).setValue(phone);
    sheetP.getRange(newRowP, 2).setValue(id);
  }
  rowPValues = sheetP.getRange(newRowP, 3, 1, 12).getValues();

  rowPValues[0][left+1-3] += nCup;
  rowPValues[0][left+2-3] += nCup;
  rowPValues[0][left+1*nData+1-3] += nBox;
  rowPValues[0][left+1*nData+2-3] += nBox;
  rowPValues[0][left+2*nData+1-3] += nSpoon;
  rowPValues[0][left+2*nData+2-3] += nSpoon;
  rowPValues[0][left+3*nData+1-3] += nFork;
  rowPValues[0][left+3*nData+2-3] += nFork;
  rowPValues[0][left+4*nData+1-3] += nChopstick;
  rowPValues[0][left+4*nData+2-3] += nChopstick;
  sheetP.getRange(newRowP, 3, 1, 12).setValues(rowPValues);

  sheetP.getRange(newRowP, 1).setBackground("red");
  if(nCup>0){
    sheetP.getRange(newRowP, left+2).setBackground("red");
  }
  if(nBox>0){
    sheetP.getRange(newRowP, left+1*nData+2).setBackground("red");
  }
  if(nSpoon>0){
    sheetP.getRange(newRowP, left+2*nData+2).setBackground("red");
  }
  if(nFork>0){
    sheetP.getRange(newRowP, left+3*nData+2).setBackground("red");
  }
  if(nChopstick>0){
    sheetP.getRange(newRowP, left+4*nData+2).setBackground("red");
  }

  newRowT = sheetT.getLastRow()+1;

  timeLRPhoneId.push(["\'" + time ,"Lend","\'" + phone ,"\'" + id]);
  rowTValues.push([nCup,nBox , nSpoon , nFork , nChopstick]);

  sheetT.getRange(newRowT, 1, 1, 4).setValues(timeLRPhoneId);
  sheetT.getRange(newRowT, 5, 1, 5).setValues(rowTValues);
  sheetT.getRange(newRowT, 1, 1, 9).setBackground("red");

  newRowL = sheetL.getLastRow()+1;
  sheetL.getRange(newRowL, 1, 1, 4).setValues(timeLRPhoneId);
  sheetL.getRange(newRowL, 5, 1, 5).setValues(rowTValues);
  sheetL.getRange(newRowL, 1, 1, 9).setBackground("red");

  return ContentService.createTextOutput("Lend Successfully");
}

function Return(e){
  var phone = e.parameter.phone;
  var id = e.parameter.id;
  var nCup = e.parameter.nCup;
  var nBox = e.parameter.nBox;
  var nSpoon = e.parameter.nSpoon;
  var nFork = e.parameter.nFork;
  var nChopstick = e.parameter.nChopstick;

  var scanPhone = [{}];
  var scanId = [{}];
  var  timeLRPhoneId = new Array();
  var rowTValues = new Array();
  var rowPValues = [{}];
  var newRowP, newRowT;
  var f = 0;
  var time = Utilities.formatDate(new Date(), "GMT+8", "yyyy-MM-dd'  'HH:mm:ss' '");

  scanPhone = sheetP.getRange(top+1, 1, sheetP.getLastRow(), 1).getValues();
  scanId = sheetP.getRange(top+1, 2, sheetP.getLastRow(), 1).getValues();

  newRowP=0;

  if((phone == "" || phone == null) ){
    sheetX.getRange(2, 1).setValue("0");
    if(id != "" && id != null){
      sheetX.getRange(2, 1).setValue("88");
      for(var i=0; i<scanId.length; i++){
        if(id == scanId[i][0]){
          newRowP=i+3;
          sheetX.getRange(2, 1).setValue("1");
          break;
        }
      }
    }else {
      sheetX.getRange(2, 1).setValue("2");
      sheetX.getRange(1, 1).setValue("No Data Input");
      return ContentService.createTextOutput("No Data Input");
    }
  }else{
    if(id != "" && id != null){
      for(var i=0; i<scanPhone.length; i++){
       if(phone == scanPhone[i][0] && id == scanId[i][0]){
         newRowP=i+3;
         sheetX.getRange(2, 1).setValue("3");
         break;
       }
      }
    }else{
      if(newRowP==0){
        for(var i=0; i<scanPhone.length; i++){
          if(phone == scanPhone[i][0]){
            newRowP=i+3;
            sheetX.getRange(2, 1).setValue("4");
            break;
          }
        }
      }
    }
  }
  /*
  for(var i=0; i<scanPhone.length; i++){
    if(phone == scanPhone[i][0]){
      if(id == scanId[i][0]){
        newRowP=i+3;
      }
    }
  }*/
  rowPValues = sheetP.getRange(newRowP, 3, 1, 12).getValues();

  if(rowPValues[0][left+2-3]==0 && rowPValues[0][left+1*nData+2-3]==0 && rowPValues[0][left+2*nData+2-3] == 0 && rowPValues[0][left+3*nData+2-3] == 0 && rowPValues[0][left+4*nData+2-3] == 0){
    return ContentService.createTextOutput("Already Return All Goods");
  }else{
    rowPValues[0][left+2-3] = rowPValues[0][left+2-3] - nCup;
    if(rowPValues[0][left+2-3] == 0){ sheetP.getRange(newRowP, left+2).setBackground("green"); f = f + 1;}

    rowPValues[0][left+1*nData+2-3] = rowPValues[0][left+1*nData+2-3] - nBox;
    if(rowPValues[0][left+1*nData+2-3] == 0){ sheetP.getRange(newRowP, left+1*nData+2).setBackground("green"); f = f + 1;}

    rowPValues[0][left+2*nData+2-3] = rowPValues[0][left+2*nData+2-3] - nSpoon;
    if(rowPValues[0][left+2*nData+2-3] == 0){ sheetP.getRange(newRowP, left+2*nData+2).setBackground("green"); f = f + 1;}

    rowPValues[0][left+3*nData+2-3] = rowPValues[0][left+3*nData+2-3] - nFork;
    if(rowPValues[0][left+3*nData+2-3] == 0){ sheetP.getRange(newRowP, left+3*nData+2).setBackground("green"); f = f + 1;}

    rowPValues[0][left+4*nData+2-3] = rowPValues[0][left+4*nData+2-3] - nChopstick;
    if(rowPValues[0][left+4*nData+2-3] == 0){ sheetP.getRange(newRowP, left+4*nData+2).setBackground("green"); f = f + 1;}
  }
  if(f == 5){ sheetP.getRange(newRowP, 1).setBackground("green"); }
  sheetP.getRange(newRowP, 3, 1, 12).setValues(rowPValues);

  newRowT = sheetT.getLastRow()+1;
  timeLRPhoneId.push(["\'" + time ,"Return","\'" + phone ,"\'" + id]);
  rowTValues.push([nCup,nBox , nSpoon , nFork , nChopstick]);

  sheetT.getRange(newRowT, 1, 1, 4).setValues(timeLRPhoneId);
  sheetT.getRange(newRowT, 5, 1, 5).setValues(rowTValues);
  sheetT.getRange(newRowT, 1, 1, 9).setBackground("red");
  sheetT.getRange(newRowT, 1, 1, 9).setBackground("green");

  return ContentService.createTextOutput("Return Successfully");
}

function FindArray(e){
  var phone = e.parameter.phone;
  var id = e.parameter.id;
  var nCup = 0;
  var nBox = 0;
  var nSpoon = 0;
  var nFork = 0;
  var nChopstick = 0;

  var scanPhone = [{}];
  var scanId = [{}];
  var newRowP, newRowT;
  var reArray = "";

  var re = phone;

  scanPhone = sheetP.getRange(top+1, 1, sheetP.getLastRow(), 1).getValues();
  scanId = sheetP.getRange(top+1, 2, sheetP.getLastRow(), 1).getValues();

  newRowP=0;

  if((phone == "" || phone == null) ){
    sheetX.getRange(2, 1).setValue("0");
    if(id != "" && id != null){
      sheetX.getRange(2, 1).setValue("88");
      for(var i=0; i<scanId.length; i++){
        if(id == scanId[i][0]){
          newRowP=i+3;
          sheetX.getRange(2, 1).setValue("1");
          break;
        }
      }
    }else {
      sheetX.getRange(2, 1).setValue("2");
      sheetX.getRange(1, 1).setValue("No Data Input");
      return ContentService.createTextOutput("No Data Input");
    }
  }else{
    if(id != "" && id != null){
      for(var i=0; i<scanPhone.length; i++){
       if(phone == scanPhone[i][0] && id == scanId[i][0]){
         newRowP=i+3;
         sheetX.getRange(2, 1).setValue("3");
         break;
       }
      }
    }else{
      if(newRowP==0){
        for(var i=0; i<scanPhone.length; i++){
          if(phone == scanPhone[i][0]){
            newRowP=i+3;
            sheetX.getRange(2, 1).setValue("4");
            break;
          }
        }
      }
    }
  }

  if(newRowP!=0){
    phone = sheetP.getRange(newRowP, 1).getValue();
    id = sheetP.getRange(newRowP, 2).getValue();
    nCup = sheetP.getRange(newRowP, left+2).getValue();
    nBox = sheetP.getRange(newRowP, left+nData*1+2).getValue();
    nSpoon = sheetP.getRange(newRowP, left+nData*2+2).getValue();
    nFork = sheetP.getRange(newRowP, left+nData*3+2).getValue();
    nChopstick = sheetP.getRange(newRowP, left+nData*4+2).getValue();
    reArray = phone + "," + id + "," + nCup + "," + nBox + "," + nSpoon + "," + nFork + "," + nChopstick;
  }else reArray = "Not Found";

  var re = phone;
  if(phone == "") re = true;
  else re = false;
  sheetX.getRange(1, 1).setValue(reArray);
  return ContentService.createTextOutput(reArray);
}

function LendLog(e){
  var lastRowT = sheetL.getLastRow();
  var times = sheetL.getRange(lastRowT-4, 1, lastRowT, 1).getValues();
  var LRs = sheetL.getRange(lastRowT-4, 2, lastRowT, 1).getValues();
  var phones = sheetL.getRange(lastRowT-4, 3, lastRowT, 1).getValues();
  var ids = sheetL.getRange(lastRowT-4, 4, lastRowT, 1).getValues();
  var cups = sheetL.getRange(lastRowT-4, 5, lastRowT, 1).getValues();
  var boxs = sheetL.getRange(lastRowT-4, 6, lastRowT, 1).getValues();
  var spoons = sheetL.getRange(lastRowT-4, 7, lastRowT, 1).getValues();
  var forks = sheetL.getRange(lastRowT-4, 8, lastRowT, 1).getValues();
  var chopsticks = sheetL.getRange(lastRowT-4, 9, lastRowT, 1).getValues();
  var orders = "";
  /*
  var jo = {};
  var orders = [];

  for(var i=0; i<5; i++){
    var time = {};
    var LR = {};
    var phone = {};
    var id = {};
    var cup = {};
    var box = {};
    var spoon = {};
    var fork = {};
    var chopstick = {};
    var order = [];

    time["time"] = times[i][0];
    LR["LR"] = LRs[i][0];
    phone["phone"] = phones[i][0];
    id["id"] = ids[i][0];
    cup["cup"] = cups[i][0];
    box["box"] = boxs[i][0];
    spoon["spoon"] = spoons[i][0];
    fork["fork"] = forks[i][0];
    chopstick["chopstick"] = chopsticks[i][0];

    order.push(time);
    order.push(LR);
    order.push(phone);
    order.push(id);
    order.push(cup);
    order.push(box);
    order.push(spoon);
    order.push(fork);
    order.push(chopstick);
    orders.push(order);
  }*/
  for(var i=0; i<5; i++){

    orders = orders +
      "{time:\'" + times[i][0] + "\'" +
      ",LR:\'" + LRs[i][0] + "\'" +
      ",phone:\'" + phones[i][0] +"\'" +
      ",id:\'" + ids[i][0] +"\'" +
      ",cup:" + cups[i][0] +
      ",box:" + boxs[i][0] +
      ",spoon:" + spoons[i][0] +
      ",fork:" + forks[i][0] +
      ",chopstick:" + chopsticks[i][0]
      +"}";
    if(i<4){ orders = orders + ",";}
  }
  //jo = "{" + orders + "}";
  //jo = orders;
  var result = JSON.stringify("[" + orders + "]");
  sheetX.getRange(1, 1).setValue(result);
  //return result;
  return ContentService.createTextOutput(result).setMimeType(ContentService.MimeType.JSON);
}

function LendLogTxt(e){
  var lastRowT = sheetL.getLastRow();
  var times = sheetL.getRange(lastRowT-4, 1, lastRowT, 1).getValues();
  var LRs = sheetL.getRange(lastRowT-4, 2, lastRowT, 1).getValues();
  var phones = sheetL.getRange(lastRowT-4, 3, lastRowT, 1).getValues();
  var ids = sheetL.getRange(lastRowT-4, 4, lastRowT, 1).getValues();
  var cups = sheetL.getRange(lastRowT-4, 5, lastRowT, 1).getValues();
  var boxs = sheetL.getRange(lastRowT-4, 6, lastRowT, 1).getValues();
  var spoons = sheetL.getRange(lastRowT-4, 7, lastRowT, 1).getValues();
  var forks = sheetL.getRange(lastRowT-4, 8, lastRowT, 1).getValues();
  var chopsticks = sheetL.getRange(lastRowT-4, 9, lastRowT, 1).getValues();
  var orders = "";

  for(var i=4; i>=0; i--){

    orders = orders +
      "借用時間: " + times[i][0] +
      "\n電話: " + phones[i][0] +
      "  ID: " + ids[i][0] + "\n";
    if(cups[i][0]>0) orders = orders + "杯子: " + cups[i][0] + "  ";
    if(boxs[i][0]>0) orders = orders + "餐盒: " + boxs[i][0] + "  ";
    if(spoons[i][0]>0) orders = orders + "湯匙: " + spoons[i][0] + "  ";
    if(forks[i][0]>0) orders = orders + "叉子: " + forks[i][0] + "  ";
    if(chopsticks[i][0]>0) orders = orders + "筷子: " + chopsticks[i][0] + "  ";

    if(i>0){ orders = orders + ",\n\n";}
  }
  sheetX.getRange(1, 1).setValue(orders);
  //return result;
  return ContentService.createTextOutput(orders);
}
