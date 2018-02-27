$(document).ready(function () {
	$("input.AMSpaginationBTN").click(function(){
		$("input.AMSpaginationBTN").each(function(){
			if($(this).prop('disabled')==true){
				$(this).prop('disabled', false).trigger("create");
				$(this).button("refresh");
			}
		});
		$(this).prop('disabled', true).trigger("create");
		$(this).button("refresh");
		var page = $(this).attr('title');
		$("input#hiddenPage").attr("value",page);
			refreshGrid();
		return false;
 	});
	$("input.AMSpaginationMainBTN").click(function(){
		if($(this).attr("title")== "Previous"){
			if(parseInt($("input#hiddenPage").val())>=1){
				$("input#hiddenPage").attr("value", parseInt($("input#hiddenPage").val())-1);
					refreshGrid();
			}
		}
		if($(this).attr("title")== "Next"){
			$("input#hiddenPage").attr("value", parseInt($("input#hiddenPage").val())+1);
				refreshGrid();
		}
		if($(this).attr("title")== "First"){
			$("input#hiddenPage").attr("value", 0);
				refreshGrid();
		}
		if($(this).attr("title")== "Last"){
			$("input#hiddenPage").attr("value", $(this).attr("value"));
				refreshGrid();
		}
		return false;
 	});
});