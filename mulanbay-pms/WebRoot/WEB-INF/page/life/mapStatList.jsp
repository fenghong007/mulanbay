<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>人生经历记录统计</title>
	<%@ include file="/static/include/header.jsp"%>
	<script type="text/javascript" src="/static/js/echarts/echarts-all-3.js"></script>
	<script type="text/javascript" src="/static/js/echarts/china.js"></script>
	<script type="text/javascript" src="/static/js/echarts/world.js"></script>

</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div region="center" border="true" title="购买统计"
		style="border-left:0px;border-right:0px; height: 188px">
		<div class="easyui-layout" id="subWrap"
			style="width:100%;height:100%;background:#0A3DA4;"
			data-options="fit:true,border:false">
			<div region="north" border="true" title="查询条件" split="true"
				style="overflow:auto;height:110px;" data-options="selected:false"
				icon="icon-edit" class="yhbackground">
				<form id="search-window" method="post">
					<table cellpadding="5" class="tableForm" style="width: 100%;">
						<tr>
						<tr>
							<td>所呆天数:</td>
							<td><input id="minDays" class="easyui-numberbox"
									   name="minDays" style="width:100px;"
									   data-options="min:0,precision:0">--
								<input id="maxDays" class="easyui-numberbox"
									   name="maxDays" style="width:100px;"
									   data-options="min:0,precision:0">
							</td>
							</td>
							<td>起始日期:</td>
							<td><input class="easyui-datebox" name="startDate"
									   data-options="showSeconds:false" style="width:110px">--
								<input class="easyui-datebox" name="endDate"
									   data-options="showSeconds:false" style="width:110px"></td>
							</td>
							<td>类型:</td>
							<td>
								<select class="easyui-combobox" name="types"
										multiple="true" multiline="true" multivalue="true" separator="," style="width:150px;height:23px">
									<option value="LIVE">生活</option>
									<option value="WORK">工作</option>
									<option value="TRAVEL">旅行</option>
									<option value="STUDY">读书</option>
								</select>
							</td>
						</tr>
						</tr>
						<tr>
							<td>地图类型:</td>
							<td><select class="easyui-combobox" name="mapType" id="mapType"
										style="width:100px;">
								<option value="CHINA">中国</option>
								<option value="WORLD">世界</option>
								<option value="LOCATION">地点(中国)</option>
							</select></td>
							<td>统计类型:</td>
							<td><select class="easyui-combobox" name="statType"
										style="width:110px;">
								<option value="DAYS">天数</option>
								<option value="COUNT">次数</option>
							</select></td>
							<td align="center" colspan="2">
								<a href="javascript:void(0)" class="easyui-linkbutton"
								   icon="icon-search" onclick="getJsonData()">查询</a> <a
									href="javascript:void(0)" class="easyui-linkbutton"
									icon="icon-cancel" onclick="resetSearchWindow()">重置</a>
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
		getJsonData();
    });
	function createMapChart(data) {
        var myChart = echarts.init(document.getElementById('container'));

        // 指定图表的配置项和数据
        var option = data;

        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
	}
	function getJsonData() {
		var vurl = '../lifeExperience/mapStat';
		var para = form2JsonEnhanced("search-window");
		doAjax(para, vurl, 'POST',false, function(data) {
		    if(para.mapType=='LOCATION'){
                createLocationMap(data);
            }else{
                createMapChart(data);
            }
		});
	}
	function createLocationMap(data){
        var geoCoordMap = data.geoCoordMapData;

        var convertData = function (data) {
            var res = [];
            for (var i = 0; i < data.length; i++) {
                var geoCoord = geoCoordMap[data[i].name];
                if (geoCoord) {
                    res.push({
                        name: data[i].name,
                        value: geoCoord.concat(data[i].value)
                    });
                }
            }
            return res;
        };

        option = {
            backgroundColor: '#404a59',
            title: {
                text: '人生去过的地方',
                subtext: '----',
                sublink: '----',
                x:'center',
                textStyle: {
                    color: '#fff'
                }
            },
            tooltip: {
                trigger: 'item',
                formatter: function (params) {
                    return params.name + ' : ' + params.value[2];
                }
            },
            legend: {
                orient: 'vertical',
                y: 'bottom',
                x:'right',
                data:['次数'],
                textStyle: {
                    color: '#fff'
                }
            },
            visualMap: {
                min: 0,
                max: data.max,
                calculable: true,
                inRange: {
                    color: ['#50a3ba', '#eac736', '#d94e5d']
                },
                textStyle: {
                    color: '#fff'
                }
            },
            geo: {
                map: 'china',
                label: {
                    emphasis: {
                        show: false
                    }
                },
                itemStyle: {
                    normal: {
                        areaColor: '#323c48',
                        borderColor: '#111'
                    },
                    emphasis: {
                        areaColor: '#2a333d'
                    }
                }
            },
            series: [
                {
                    name: 'pm2.5',
                    type: 'scatter',
                    coordinateSystem: 'geo',
                    data: convertData(data.dataList),
                    symbolSize: 12,
                    label: {
                        normal: {
                            show: false
                        },
                        emphasis: {
                            show: false
                        }
                    },
                    itemStyle: {
                        emphasis: {
                            borderColor: '#fff',
                            borderWidth: 1
                        }
                    }
                }
            ]
        };
        var myChart = echarts.init(document.getElementById('container'));

        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
	}
</script>
</html>
