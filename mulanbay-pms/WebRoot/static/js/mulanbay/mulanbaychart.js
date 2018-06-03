/**
 * Created by fenghong on 2017/2/7.
 */
var colorList = ['#2f4554', '#61a0a8', '#d48265', '#91c7ae','#749f83',
    '#ca8622', '#bda29a','#6e7074', '#546570','#c23531',  '#c4ccd3'];
function createChart(data) {
    createChartEnhanced(data,'container');
}
function createChartEnhanced(data,containId) {
    var myChart = echarts.init(document.getElementById(containId));

    // 指定图表的配置项和数据
    var option = data;

    // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(option,true);
}

function createBarData(data){
    createBarDataEnhanced(data,'container');
}
//柱状图
function createBarDataEnhanced(data,containId){
    var series =new Array();
    for(var i=0;i<data.ydata.length;i++){
        var serie = {
            name:data.ydata[i].name,
            type:'bar',
            data:data.ydata[i].data,
            itemStyle: {
                normal: {
                    label: {
                        show: true,
                        position: 'top',
                        formatter: '{c}'
                    }
                }
            },
            markPoint : {
                data : [
                    {type : 'max', name: '最大值'},
                    {type : 'min', name: '最小值'}
                ]
            },
            markLine : {
                data : [
                    {type : 'average', name: '平均值'}
                ]
            }
        };
        series.push(serie);
    }
    var option = {
        title : {
            text: data.title,
            subtext: data.subTitle
        },
        tooltip : {
            trigger: 'axis'
        },
        label:{
            normal:{
                show: true,
                position: 'top'}
        },
        legend: {
            data:data.legendData
        },
        toolbox: {
            show : true,
            feature : {
                dataView : {show: true, readOnly: false},
                magicType : {show: true, type: ['line', 'bar']},
                restore : {show: true},
                saveAsImage : {show: true}
            }
        },
        color: colorList,
        calculable : true,
        xAxis : [
            {
                type : 'category',
                data : data.xdata
            }
        ],
        yAxis : [
            {
                type : 'value'
            }
        ],
        series : series
    };
    createChartEnhanced(option,containId);
}

function createLineData(data){
    createLineDataEnhanced(data,'container');
}

//折线图
function createLineDataEnhanced(data,containId){
    var series =new Array();
    for(var i=0;i<data.ydata.length;i++){
        var serie = {
            name:data.ydata[i].name,
            type:'line',
            dataView : {show: true, readOnly: false},
            itemStyle : { normal: {label : {show: true}}},
            data:data.ydata[i].data,
            markPoint: {
                data: [
                    {type: 'max', name: '最大值'},
                    {type: 'min', name: '最小值'}
                ]
            },
            markLine: {
                data: [
                    {type: 'average', name: '平均值'},
                    [{
                        symbol: 'none',
                        x: '90%',
                        yAxis: 'max'
                    }, {
                        symbol: 'circle',
                        label: {
                            normal: {
                                position: 'start',
                                formatter: '最大值'
                            }
                        },
                        type: 'max',
                        name: '最高点'
                    }]
                ]
            }
        };
        series.push(serie);
    }
    var option = {
        title: {
            text: data.title,
            subtext: data.subTitle
        },
        tooltip: {
            trigger: 'axis'
        },
        legend: {
            data:data.legendData
        },
        toolbox: {
            show: true,
            feature: {
                dataZoom: {
                    yAxisIndex: 'none'
                },
                dataView : {show: true, readOnly: false},
                magicType: {type: ['line', 'bar']},
                restore: {},
                saveAsImage: {}
            }
        },
        color: colorList,
        xAxis:  {
            type: 'category',
            boundaryGap: false,
            data: data.xdata
        },
        yAxis: {
            type: 'value',
            axisLabel: {
                formatter: '{value}'
            }
        },
        series: series
    };
    createChartEnhanced(option,containId);
}

function createPieData(data){
    createPieDataEnhanced(data,'container');
}

// 饼图
function createPieDataEnhanced(data,containId){
    var series =new Array();
    for(var i=0;i<data.detailData.length;i++){
        var serie = {
            name: data.detailData[i].name,
            type: 'pie',
            radius : '55%',
            center: ['50%', '60%'],
            data:data.detailData[i].data,
            itemStyle: {
                emphasis: {
                    shadowBlur: 10,
                    shadowOffsetX: 0,
                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                },
                normal:{
                    label:{
                        show: true,
                        formatter: '{b} : {c} ({d}%)'
                    },
                    labelLine :{show:true}
                }
            }
        };
        series.push(serie);
    }
    var option = {
        title : {
            text: data.title,
            subtext: data.subTitle,
            x:'center'
        },
        color: colorList,
        toolbox: {
            show: false,
            feature: {
                dataZoom: {
                    yAxisIndex: 'none'
                },
                dataView : {show: true, readOnly: false},
                magicType: {type: ['line', 'bar']},
                restore: {},
                saveAsImage: {}
            }
        },
        tooltip : {
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c} ({d}%)"
        },
        legend: {
            orient: 'vertical',
            left: 'left',
            data: data.xdata
        },
        series : series
    };
    createChartEnhanced(option,containId);
}

function createDoublePieData(data){
    createDoublePieDataEnhanced(data,'container');
}


// 双饼图
function createDoublePieDataEnhanced(data,containId){
    var option = {
        title : {
            text: data.title,
            subtext: data.subTitle,
            x:'center'
        },
        color: colorList,
        toolbox: {
            show: false,
            feature: {
                dataZoom: {
                    yAxisIndex: 'none'
                },
                dataView : {show: true, readOnly: false},
                magicType: {type: ['line', 'bar']},
                restore: {},
                saveAsImage: {}
            }
        },
        tooltip : {
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c} ({d}%)"
        },
        legend: {
            orient: 'vertical',
            left: 'left',
            data: data.xdata
        },
        series: [
            {
                name:data.detailData[0].name,
                type:'pie',
                selectedMode: 'single',
                radius: [0, '30%'],

                label: {
                    normal: {
                        position: 'inner'
                    }
                },
                labelLine: {
                    normal: {
                        show: false
                    }
                },
                data:data.detailData[0].data,
                itemStyle: {
                    emphasis: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                    },
                    normal:{
                        label:{
                            show: true,
                            formatter: '{b} : {c} ({d}%)'
                        },
                        labelLine :{show:true}
                    }
                }
            },
            {
                name:data.detailData[1].name,
                type:'pie',
                radius: ['40%', '55%'],
                data:data.detailData[1].data,
                itemStyle: {
                    emphasis: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                    },
                    normal:{
                        label:{
                            show: true,
                            formatter: '{b} : {c} ({d}%)'
                        },
                        labelLine :{show:true}
                    }
                }
            }
        ]
    };
    createChartEnhanced(option,containId);
}

function createCalanderData(data){
    createCalanderDataEnhanced(data,'container');
}

// 日历图
function createCalanderDataEnhanced(data,containId){

    var graphData = data.graphData;
    var links={};
    if(graphData){
        links = graphData.map(function (item, idx) {
            return {
                source: idx,
                target: idx + 1
            };
        });
        links.pop();
    }

    var minValue = data.minValue;
    var maxValue = data.maxValue;
    //最大值最小值分成3分
    var peace = (maxValue-minValue)/5;


    // 获取每个格子的点的大小，数值为2,4,6,8,10
    function getSymbolSize(value) {
        if(1==1){
            //算法不好，强行写死
            return 10;
        }
        if(peace==0){
            //最小和最大值相等
            return 10;
        }
        var v = ((value[1]-minValue)/peace)*2;
        if(v==0){
            return 2;
        }
        return v;
    }

    var seriesData =new Array();
    var serie={
        type: 'graph',
        edgeSymbol: ['none', 'arrow'],
        coordinateSystem: 'calendar',
        links: links,
        symbolSize: 15,
        calendarIndex: 0,
        itemStyle: {
            normal: {
                color: 'green',
                shadowBlue: 9,
                shadowOffsetX: 1.5,
                shadowOffsetY: 3,
                shadowColor: '#555'
            }
        },
        lineStyle: {
            normal: {
                color: '#D10E00',
                width: 1,
                opacity: 1
            }
        },
        data: data.graphData,
        z: 20
    };
    seriesData.push(serie);
    serie={
        type: 'graph',
        edgeSymbol: ['none', 'arrow'],
        coordinateSystem: 'calendar',
        links: links,
        symbolSize: 15,
        calendarIndex: 1,
        itemStyle: {
            normal: {
                color: 'green',
                shadowBlue: 9,
                shadowOffsetX: 1.5,
                shadowOffsetY: 3,
                shadowColor: '#555'
            }
        },
        lineStyle: {
            normal: {
                color: '#D10E00',
                width: 1,
                opacity: 1
            }
        },
        data: data.graphData,
        z: 20
    };
    seriesData.push(serie);
    serie={
        name: data.legendData[0],
        type: 'scatter',
        coordinateSystem: 'calendar',
        data: data.series,
        symbolSize: function (val) {
            //return val[1] / data.compareSizeValue;
            return getSymbolSize(val);
        },
        itemStyle: {
            normal: {
                color: 'red'
            }
        }
    };
    seriesData.push(serie);
    serie={
        name: data.legendData[0],
        type: 'scatter',
        coordinateSystem: 'calendar',
        calendarIndex: 1,
        data: data.series,
        symbolSize: function (val) {
            //return val[1] / data.compareSizeValue;
            return getSymbolSize(val);
        },
        itemStyle: {
            normal: {
                color: 'red'
            }
        }
    };
    seriesData.push(serie);
    if(data.top&&data.top>0){
        serie={
            name: data.legendData[1],
            type: 'effectScatter',
            coordinateSystem: 'calendar',
            calendarIndex: 1,
            data: data.series.sort(function (a, b) {
                return b[1] - a[1];
            }).slice(0, data.top),
            symbolSize: function (val) {
                //return val[1] / data.compareSizeValue;
                return getSymbolSize(val);
            },
            showEffectOn: 'render',
            rippleEffect: {
                brushType: 'stroke'
            },
            hoverAnimation: true,
            itemStyle: {
                normal: {
                    color: '#f4e925',
                    shadowBlur: 10,
                    shadowColor: '#333'
                }
            },
            zlevel: 1
        };
        seriesData.push(serie);
        serie={
            name: data.legendData[1],
            type: 'effectScatter',
            coordinateSystem: 'calendar',
            data: data.series.sort(function (a, b) {
                return b[1] - a[1];
            }).slice(0, data.top),
            symbolSize: function (val) {
                //return val[1] / data.compareSizeValue;
                return getSymbolSize(val);
            },
            showEffectOn: 'render',
            rippleEffect: {
                brushType: 'stroke'
            },
            hoverAnimation: true,
            itemStyle: {
                normal: {
                    color: '#f4e925',
                    shadowBlur: 10,
                    shadowColor: '#333'
                }
            },
            zlevel: 1
        };
        seriesData.push(serie);
    }


    var option = {
        backgroundColor: '#404a59',

        title: {
            top: 10,
            text: data.title,
            subtext: data.subTitle,
            left: 'center',
            textStyle: {
                color: '#fff'
            }
        },
        tooltip : {
            trigger: 'item',
            formatter: "{a}<br/>{b}({c}"+data.unit+")"
        },
        legend: {
            top: '30',
            left: '100',
            data: data.legendData,
            textStyle: {
                color: '#fff'
            }
        },
        calendar: [{
            top: 80,
            left: 'center',
            range: data.rangeFirst,
            splitLine: {
                show: true,
                lineStyle: {
                    color: '#000',
                    width: 4,
                    type: 'solid'
                }
            },
            yearLabel: {
                formatter: '{start}  上半年',
                textStyle: {
                    color: '#fff'
                }
            },
            itemStyle: {
                normal: {
                    color: '#323c48',
                    borderWidth: 1,
                    borderColor: '#111'
                }
            }
        }, {
            top: 260,
            left: 'center',
            range: data.rangeSecond,
            splitLine: {
                show: true,
                lineStyle: {
                    color: '#000',
                    width: 4,
                    type: 'solid'
                }
            },
            yearLabel: {
                formatter: '{start}  下半年',
                textStyle: {
                    color: '#fff'
                }
            },
            itemStyle: {
                normal: {
                    color: '#323c48',
                    borderWidth: 1,
                    borderColor: '#111'
                }
            }
        }],
        series : seriesData
    };
    createChartEnhanced(option,containId);
}

function createCalanderPieData(data){
    createCalanderPieDataEnhanced(data,'container');
}

function createCalanderPieDataEnhanced(data,containerId) {
    var cellSize = [65, 65];
    var pieRadius = 25;
    var app = {};
    var dom = document.getElementById(containerId);
    var myChart = echarts.init(dom);
    var seriesData = data.seriesData;
    var scatterData = data.scatterData;

    function getPieSeries(scatterData, chart) {
        return echarts.util.map(scatterData, function (item, index) {
            var center = chart.convertToPixel('calendar', item);
            return {
                id: index + 'pie',
                type: 'pie',
                center: center,
                label: {
                    normal: {
                        formatter: '{c}',
                        position: 'inside'
                    }
                },
                radius: pieRadius,
                data: seriesData[item[0]]
            };
        });
    }

    function getPieSeriesUpdate(scatterData, chart) {
        return echarts.util.map(scatterData, function (item, index) {
            var center = chart.convertToPixel('calendar', item);
            return {
                id: index + 'pie',
                center: center
            };
        });
    }

    var option = {
        //backgroundColor: '#404a59',

        title: {
            show : false,
            top: 0,
            text: data.title,
            subtext: data.subTitle,
            left: 'left',
            textStyle: {
                color: '#fff'
            }
        },
        tooltip : {},
        legend: {
            data: data.legendData,
            bottom: 10
        },
        calendar: {
            top: 40,
            left: 'center',
            orient: 'vertical',
            cellSize: cellSize,
            yearLabel: {
                show: false,
                textStyle: {
                    fontSize: 30
                }
            },
            dayLabel: {
                margin: 20,
                firstDay: 1,
                nameMap: ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六']
            },
            monthLabel: {
                show: false
            },
            range: data.range
        },
        series: [{
            id: 'label',
            type: 'scatter',
            coordinateSystem: 'calendar',
            symbolSize: 1,
            label: {
                normal: {
                    show: true,
                    formatter: function (params) {
                        return echarts.format.formatTime('dd', params.value[0]);
                    },
                    offset: [-cellSize[0] / 2 + 10, -cellSize[1] / 2 + 10],
                    textStyle: {
                        color: '#000',
                        fontSize: 12
                    }
                }
            },
            data: data.scatterData
        }]
    };

    if (!app.inNode) {
        var pieInitialized;
        setTimeout(function () {
            pieInitialized = true;
            myChart.setOption({
                series: getPieSeries(scatterData, myChart)
            });
        }, 10);

        app.onresize = function () {
            if (pieInitialized) {
                myChart.setOption({
                    series: getPieSeriesUpdate(scatterData, myChart)
                });
            }
        };
    }
    myChart.setOption(option,true);
}

function createCalanderHeatMapData(data){
    createCalanderHeatMapDataEnhanced(data,'container');
}

// 日历热点图
function createCalanderHeatMapDataEnhanced(data,containId){
    var calendars = new Array();
    for(var i=0;i<data.years.length;i++){
        var c={
            top: 80+i*180,
            range: data.years[i],
            cellSize: ['auto', 20],
            right: 5
        };
        calendars.push(c);
    }
    var seriesData =new Array();
    for(var i=0;i<data.series.length;i++){
        var serie={
            type: 'heatmap',
            calendarIndex: i,
            coordinateSystem: 'calendar',
            data: data.series[i]
        };
        seriesData.push(serie);
    }

    var option = {
        title: {
            top: 0,
            left: 'center',
            text: data.title
        },
        tooltip: {
            position: 'top',
            formatter: function (p) {
                var format = echarts.format.formatTime('yyyy-MM-dd', p.data[0]);
                return format + ': ' + p.data[1]+data.unit;
            }
        },
        visualMap: {
            min: data.minValue,
            max: data.maxValue,
            calculable: true,
            orient: 'horizontal',
            left: 'center',
            top: 30
        },
        calendar: calendars,
        series: seriesData
    };

    createChartEnhanced(option,containId);
}

function createScatterData(data){
    createScatterDataEnhanced(data,'container');
}

// 日历热点图
function createScatterDataEnhanced(data,containId){
    var seriesData =new Array();
    for(var i=0;i<data.seriesDatas.length;i++){
        var serie={
            name:data.seriesDatas[i].name,
            type:'scatter',
            data: data.seriesDatas[i].data,
            markArea: {
                silent: true,
                itemStyle: {
                    normal: {
                        color: 'transparent',
                        borderWidth: 1,
                        borderType: 'dashed'
                    }
                },
                data: [[{
                    name: data.seriesDatas[i].name+'分布区间',
                    xAxis: 'min',
                    yAxis: 'min'
                }, {
                    xAxis: 'max',
                    yAxis: 'max'
                }]]
            },
            markPoint : {
                data : [
                    {type : 'max', name: '最大值'},
                    {type : 'min', name: '最小值'}
                ]
            },
            markLine : {
                lineStyle: {
                    normal: {
                        type: 'solid'
                    }
                },
                data : [
                    {type : 'average', name: '平均值'},
                    { xAxis: data.seriesDatas[i].xAxisAverage }
                ]
            }
        };
        seriesData.push(serie);
    }

    var option = {
        title : {
            text: data.title,
            subtext: data.subTitle
        },
        grid: {
            left: '3%',
            right: '7%',
            bottom: '3%',
            containLabel: true
        },
        tooltip : {
            // trigger: 'axis',
            showDelay : 0,
            formatter : function (params) {
                if (params.value.length > 1) {
                    return params.seriesName + ' :<br/>'
                        + params.value[0] + data.xUnit
                        + params.value[1] + data.yUnit;
                }
                else {
                    return params.seriesName + ' :<br/>'
                        + params.name + ' : '
                        + params.value + data.yUnit;
                }
            },
            axisPointer:{
                show: true,
                type : 'cross',
                lineStyle: {
                    type : 'dashed',
                    width : 1
                }
            }
        },
        toolbox: {
            feature: {
                dataZoom: {},
                brush: {
                    type: ['rect', 'polygon', 'clear']
                }
            }
        },
        brush: {
        },
        legend: {
            data: data.legendData,
            left: 'center'
        },
        xAxis : [
            {
                type : 'value',
                scale:true,
                axisLabel : {
                    formatter: '{value} '+data.xUnit
                },
                splitLine: {
                    show: false
                }
            }
        ],
        yAxis : [
            {
                type : 'value',
                scale:true,
                axisLabel : {
                    formatter: '{value} '+data.yUnit
                },
                splitLine: {
                    show: false
                }
            }
        ],
        series : seriesData
    };

    createChartEnhanced(option,containId);
}

function createCompareCalanderData(data){
    createCompareCalanderDataEnhanced(data,'container');
}

function createCompareCalanderDataEnhanced(data,containId){

    var graphData = data.graphData;
    var links={};
    if(graphData){
        links = graphData.map(function (item, idx) {
            return {
                source: idx,
                target: idx + 1
            };
        });
        links.pop();
    }

    var minValue = data.minValue;
    var maxValue = data.maxValue;
    //最大值最小值分成3分
    var peace = (maxValue-minValue)/5;


    // 获取每个格子的点的大小，数值为2,4,6,8,10
    function getSymbolSize(value) {
        if(1==1){
            //算法不好，强行写死
            return 10;
        }
        if(peace==0){
            //最小和最大值相等
            return 10;
        }
        var v = ((value[1]-minValue)/peace)*2;
        if(v==0){
            return 2;
        }
        return v;
    }

    var seriesData =new Array();
    var serie={
        type: 'graph',
        edgeSymbol: ['none', 'arrow'],
        coordinateSystem: 'calendar',
        links: links,
        symbolSize: 15,
        calendarIndex: 0,
        itemStyle: {
            normal: {
                color: 'green',
                shadowBlue: 9,
                shadowOffsetX: 1.5,
                shadowOffsetY: 3,
                shadowColor: '#555'
            }
        },
        lineStyle: {
            normal: {
                color: '#D10E00',
                width: 1,
                opacity: 1
            }
        },
        data: data.graphData,
        z: 20
    };
    seriesData.push(serie);
    serie={
        type: 'graph',
        edgeSymbol: ['none', 'arrow'],
        coordinateSystem: 'calendar',
        links: links,
        symbolSize: 15,
        calendarIndex: 1,
        itemStyle: {
            normal: {
                color: 'green',
                shadowBlue: 9,
                shadowOffsetX: 1.5,
                shadowOffsetY: 3,
                shadowColor: '#555'
            }
        },
        lineStyle: {
            normal: {
                color: '#D10E00',
                width: 1,
                opacity: 1
            }
        },
        data: data.graphData,
        z: 20
    };
    seriesData.push(serie);
    serie={
        name: data.legendData[0],
        type: 'scatter',
        coordinateSystem: 'calendar',
        data: data.series,
        symbolSize: function (val) {
            //return val[1] / data.compareSizeValue;
            return getSymbolSize(val);
        },
        itemStyle: {
            normal: {
                color: 'red'
            }
        }
    };
    seriesData.push(serie);
    serie={
        name: data.legendData[0],
        type: 'scatter',
        coordinateSystem: 'calendar',
        calendarIndex: 1,
        data: data.series,
        symbolSize: function (val) {
            //return val[1] / data.compareSizeValue;
            return getSymbolSize(val);
        },
        itemStyle: {
            normal: {
                color: 'red'
            }
        }
    };
    seriesData.push(serie);
    if(data.series2&&data.series2.length>0){
        serie={
            name: data.legendData[1],
            type: 'effectScatter',
            coordinateSystem: 'calendar',
            calendarIndex: 1,
            data: data.series2,
            symbolSize: function (val) {
                //return val[1] / data.compareSizeValue;
                return getSymbolSize(val);
            },
            showEffectOn: 'render',
            rippleEffect: {
                brushType: 'stroke'
            },
            hoverAnimation: true,
            itemStyle: {
                normal: {
                    color: '#f4e925',
                    shadowBlur: 10,
                    shadowColor: '#333'
                }
            },
            zlevel: 1
        };
        seriesData.push(serie);
        serie={
            name: data.legendData[1],
            type: 'effectScatter',
            coordinateSystem: 'calendar',
            data: data.series2,
            symbolSize: function (val) {
                //return val[1] / data.compareSizeValue;
                return getSymbolSize(val);
            },
            showEffectOn: 'render',
            rippleEffect: {
                brushType: 'stroke'
            },
            hoverAnimation: true,
            itemStyle: {
                normal: {
                    color: '#f4e925',
                    shadowBlur: 10,
                    shadowColor: '#333'
                }
            },
            zlevel: 1
        };
        seriesData.push(serie);
    }


    var option = {
        backgroundColor: '#404a59',

        title: {
            top: 10,
            text: data.title,
            subtext: data.subTitle,
            left: 'center',
            textStyle: {
                color: '#fff'
            }
        },
        tooltip : {
            trigger: 'item',
            formatter: "{a}<br/>{b}({c}"+data.unit+")"
        },
        legend: {
            top: '30',
            left: '100',
            data: data.legendData,
            textStyle: {
                color: '#fff'
            }
        },
        calendar: [{
            top: 80,
            left: 'center',
            range: data.rangeFirst,
            splitLine: {
                show: true,
                lineStyle: {
                    color: '#000',
                    width: 4,
                    type: 'solid'
                }
            },
            yearLabel: {
                formatter: '{start}  上半年',
                textStyle: {
                    color: '#fff'
                }
            },
            itemStyle: {
                normal: {
                    color: '#323c48',
                    borderWidth: 1,
                    borderColor: '#111'
                }
            }
        }, {
            top: 260,
            left: 'center',
            range: data.rangeSecond,
            splitLine: {
                show: true,
                lineStyle: {
                    color: '#000',
                    width: 4,
                    type: 'solid'
                }
            },
            yearLabel: {
                formatter: '{start}  下半年',
                textStyle: {
                    color: '#fff'
                }
            },
            itemStyle: {
                normal: {
                    color: '#323c48',
                    borderWidth: 1,
                    borderColor: '#111'
                }
            }
        }],
        series : seriesData
    };
    createChartEnhanced(option,containId);
}