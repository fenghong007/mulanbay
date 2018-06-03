function getPercentWithColor(num1,num2) {
    var p = getPercent(num1,num2);
    if(p<=50){
        return '<font color="red">'+p+'%</font>';;
    }else if(p<80){
        return '<font color="purple">'+p+'%</font>';;
    }else if(p<95){
        return '<font color="#daa520">'+p+'%</font>';;
    }else if(p<98){
        return '<font color="green">'+p+'%</font>';;
    }

}

function getValueCompareResult(value,planValue,compareType){
    if(planValue==null||planValue==''||planValue==undefined){
        return value;
    }
    if(value==0&&planValue==0){
        return '--';
    }
    if(compareType=='MORE'){
        if(value >= planValue){
            return '<font color="green">'+value+'</font><img src="../static/image/tick.png"></img>';
        }else {
            return '<font color="red">'+value+'&nbsp;('+getPercentWithColor(value,planValue)+')</font>';
        }
    }else{
        if(value <= planValue){
            return '<font color="green">'+value+'</font><img src="../static/image/tick.png"></img>';
        }else {
            return '<font color="red">'+value+'&nbsp;('+getOverString(value,planValue)+')</font>';
        }
    }

}

function getOverString(value,planValue){
    var vv = (value+0.0-planValue)/planValue;
    if(vv<1){
        return '超出'+(vv*100).toFixed(0)+'%';
    }else{
        return '超出'+vv.toFixed(1)+'倍';
    }
}

function getCompareYearString(year) {
    if(year==null||year==''||year==undefined){
        return '';
    }
    var current = getYear(0);
    if(year==current){
        return '';
    }else{
        return '&nbsp;<font color="#db7093">('+year+')</font>'
    }
}