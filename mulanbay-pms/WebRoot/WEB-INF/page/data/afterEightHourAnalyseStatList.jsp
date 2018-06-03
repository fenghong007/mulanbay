<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>八小时外统计</title>
	<%@ include file="/static/include/header.jsp"%>
	<script type="text/javascript" src="/static/js/echarts/echarts-all-3.js"></script>
	<script type="text/javascript" src="/static/js/mulanbay/mulanbaychart.js"></script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div region="center" border="true" title="八小时外统计"
		style="border-left:0px;border-right:0px; height: 188px">
		<div class="easyui-layout" id="subWrap"
			style="width:100%;height:100%;background:#0A3DA4;"
			data-options="fit:true,border:false">
			<div region="north" border="true" title="查询条件" split="true"
				style="overflow:auto;height:70px;" data-options="selected:false"
				icon="icon-edit" class="yhbackground">
				<form id="search-window" method="post">
					<table cellpadding="5" class="tableForm" style="width: 100%;">
						<tr>
							<td>起始日期:</td>
							<td><input class="easyui-datebox" name="startDate"
									   data-options="showSeconds:false" style="width:110px">--
								<input class="easyui-datebox" name="endDate"
									   data-options="showSeconds:false" style="width:110px"></td>
							</td>
							<td>统计类型:</td>
							<td align="center">
								<select class="easyui-combobox" name="type"
										style="width:110px;">
									<option value="MINUTES">花费时间</option>
									<option value="COUNT">次数</option>
								</select>
							</td>
							<td>数据分组:</td>
							<td align="center">
								<select class="easyui-combobox" name="dataGroup"
										style="width:60px;">
									<option value="DETAIL">明细</option>
									<option value="GROUP">组</option>
								</select>
							</td>
							<td>图标类型:</td>
							<td align="center">
								<select class="easyui-combobox" name="chartType"
										style="width:110px;">
									<option value="PIE">饼图</option>
									<option value="SCATTER">散点图</option>
								</select>
							</td>
							<td align="center" colspan="2">
								<a href="javascript:void(0)" class="easyui-linkbutton"
								   icon="icon-search" onclick="getJsonData()">统计</a> <a
									href="javascript:void(0)" class="easyui-linkbutton"
									icon="icon-cancel" onclick="resetAndInitSearchWindowYear()">重置</a>
							</td>
						</tr>
					</table>
				</form>
			</div>
			<div region="center" border="true" title="结果" split="true">
				<div id="container"
					style="min-width: 400px; height: 100%; margin: 0 auto"></div>
			</div>
		</div>
	</div>

</body>
<script type="text/javascript">
    $(function() {
        initSearchFormYear();
        getJsonData();
    });
	function getJsonData() {
		var vurl = '../dataAnalyse/afterEightHourAnalyseStat';
		var para = form2Json("search-window");
		doAjax(para, vurl, 'GET',false, function(data) {
            if(para.chartType=='PIE'){
                createPieData(data);
            }else{
                createMyScatterData(data);
            }
		});
	}
    // 散点图
    function createMyScatterData(data){
        var seriesData =new Array();
        for(var i=0;i<data.seriesDatas.length;i++){
            var serie={
                name:data.seriesDatas[i].name,
                type: 'scatter',
                itemStyle: itemStyle,
                data: data.seriesDatas[i].data
            };
            seriesData.push(serie);
        }

        var schema = [
            {name: 'week', index: 0, text: '星期'},
            {name: 'times', index: 1, text: '花费时间(小时)'},
            {name: 'count', index: 2, text: '次数'}
        ];

        function getWeekDesc(value) {
            if(value==1) return '星期一';
            if(value==2) return '星期二';
            if(value==3) return '星期三';
            if(value==4) return '星期四';
            if(value==5) return '星期五';
            if(value==6) return '星期六';
            if(value==7) return '星期日';
        }

        var itemStyle = {
            normal: {
                opacity: 0.8,
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowOffsetY: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
        };

        var option = {
            backgroundColor: '#404a59',
            color: ['#61a0a8', '#d48265', '#91c7ae','#749f83',
                '#ca8622', '#bda29a','#6e7074', '#546570','#c23531',  '#c4ccd3'],
            legend: {
                y: 'top',
                data: data.legendData,
                textStyle: {
                    color: '#fff',
                    fontSize: 16
                }
            },
            grid: {
                x: '10%',
                x2: 150,
                y: '18%',
                y2: '10%'
            },
            tooltip: {
                padding: 10,
                backgroundColor: '#222',
                borderColor: '#777',
                borderWidth: 1,
                formatter: function (obj) {
                    var value = obj.value;
                    return '<div style="border-bottom: 1px solid rgba(255,255,255,.3); font-size: 18px;padding-bottom: 7px;margin-bottom: 7px">'
                        + obj.seriesName + ' ' +getWeekDesc(value[0]) + ' '
                        + '</div>'
                        + schema[1].text + '：' + value[1] + '<br>'
                        + schema[2].text + '：' + value[2] + '<br>';
                }
            },
            xAxis: {
                type: 'value',
                name: '星期',
                nameGap: 16,
                nameTextStyle: {
                    color: '#fff',
                    fontSize: 14
                },
                max: 7,
                splitLine: {
                    show: false
                },
                axisLine: {
                    lineStyle: {
                        color: '#eee'
                    }
                }
            },
            yAxis: {
                type: 'value',
                name: '花费时间(小时)',
                nameLocation: 'end',
                nameGap: 20,
                nameTextStyle: {
                    color: '#fff',
                    fontSize: 16
                },
                axisLine: {
                    lineStyle: {
                        color: '#eee'
                    }
                },
                splitLine: {
                    show: false
                }
            },
            visualMap: {
                left: 'right',
                top: '10%',
                dimension: 2,
                min: data.minValue,
                max: data.maxValue,
                itemWidth: 30,
                itemHeight: 120,
                calculable: true,
                precision: 0.1,
                text: ['圆形大小：次数'],
                textGap: 30,
                textStyle: {
                    color: '#fff'
                },
                inRange: {
                    symbolSize: [10, 70]
                },
                outOfRange: {
                    symbolSize: [10, 70],
                    color: ['rgba(255,255,255,.2)']
                },
                controller: {
                    inRange: {
                        color: ['#c23531']
                    },
                    outOfRange: {
                        color: ['#444']
                    }
                }
            },
            series: seriesData
        };
        createChart(option);
    }
</script>
</html>
