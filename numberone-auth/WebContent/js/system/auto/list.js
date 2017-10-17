var pageii = null;
var grid = null;
$(function() {
	
	grid = lyGrid({
		pagId : 'paging',
		l_column : [ {
			colkey : "title",
			name : "名称",
		},{
			colkey : "SUBTITLE",
			name : "副标题",
		},{
			colkey : "source",
			name : "来源",
		},{
			colkey : "num",
			name : "查看次数",
		}, {
			colkey : "createTime",
			name : "创建时间",
			isSort:true,
			renderData : function(rowindex,data, rowdata, column) {
				return new Date(data).format("yyyy-MM-dd hh:mm:ss");
			}
		}],
		jsonUrl : rootPath + '/automatic/findByPage.shtml',
		checkbox : true,
		serNumber : true
	});
	$("#search").click("click", function() {// 绑定查询按扭
		var searchParams = $("#searchForm").serializeJson();// 初始化传参数
		grid.setOptions({
			data : searchParams
		});
	});
	$("#addAutoBean").click("click", function() {
		addAutoBean();
	});
	$("#editAutoBean").click("click", function() {
		editAutoBean();
	});
	$("#delAutoBean").click("click", function() {
		delAutoBean();
	});
});
function editAutoBean() {
	var cbox = grid.getSelectedCheckbox();
	if (cbox.length > 1 || cbox == "") {
		layer.msg("只能选中一个");
		return;
	}
	pageii = layer.open({
		title : "编辑",
		type : 2,
		area : [ "85%", "100%" ],
		content : rootPath + '/automatic/editUI.shtml?id=' + cbox
	});
}
function addAutoBean() {
	pageii = layer.open({
		title : "新增",
		type : 2,
		area : [ "85%", "100%" ],
		content : rootPath + '/automatic/addUI.shtml'
	});
}
function delAutoBean() {
	var cbox = grid.getSelectedCheckbox();
	if (cbox == "") {
		layer.msg("请选择删除项！！");
		return;
	}
	layer.confirm('是否删除？', function(index) {
		var url = rootPath + '/automatic/deleteEntity.shtml';
		var s = CommnUtil.ajax(url, {
			ids : cbox.join(",")
		}, "json");
		if (s == "success") {
			layer.msg('删除成功');
			grid.loadData();
		} else {
			layer.msg('删除失败');
		}
	});
}
