/*
 * identify browser
 */
	HM_IE = (document.all) ? true : false;


/*
 * draggable
 */
function draggableDialog(dialogId,dialogHandleId){
	//$('#'+dialogId).Draggable( //Draggable name in jquery.interface.1.1.4      and     draggable name in jquery.ui.draggable.1.2.6
	$('#'+dialogId).draggable(
		{
			zIndex: 	20,
			ghosting:	false,
			opacity: 	1,
			handle:	'#'+dialogHandleId
		}
	);	
}
function closeDialog(dialogId){
	$("#"+dialogId).hide();
}
function showDialog(dialogId,screenPosition,ajaxLoadUrl){
	var dlg = $('#'+dialogId);

	//draggableDialog(dialogId,dialogId+"_handle");

	dlg.show();
	var isCentered=false;
	if(ajaxLoadUrl!==null && ajaxLoadUrl!==undefined && ajaxLoadUrl!==""){
		try{
		var container=$("#"+dialogId+"_content_ajax");
		//alert(container.attr("id"));
		if( container!==null && container!==undefined){// && container.attr("class")==="ajax"){
			container.html("<img src='aipconfig/dialog/loading.gif' alt='لطفا صبر کنید...'/>");
			$.get(ajaxLoadUrl,function(data){
				//alert(data);	
				container.html(data);
				if( "center" === screenPosition){
					moveCenterDialog(dlg);
					isCentered=true;	
				}
				//it is because of caching data in client
				container.addClass("load");
			});
		}
		}catch(e){
			alert("aipdialog.js:showDialog:ajaxLoadUrl::::"+e);
		}
	}
	if( "center" === screenPosition && isCentered==false){
		moveCenterDialog(dlg);	
	}
}


function moveCenterDialog(dlg){
/*
 * find screen middle
 */
	var WindowLeftEdge = (HM_IE) ? document.body.scrollLeft   : window.pageXOffset;
	var WindowTopEdge  = (HM_IE) ? document.body.scrollTop    : window.pageYOffset;
	var WindowWidth    = (HM_IE) ? document.body.clientWidth  : window.innerWidth;
	var WindowHeight   = (HM_IE) ? document.body.clientHeight : window.innerHeight;
	//alert(WindowLeftEdge+","+WindowTopEdge+","+WindowWidth+","+WindowHeight+","+dlg.width()+","+dlg.height());
	var dlgCenterTop = WindowTopEdge+WindowHeight/2-dlg.height()/2;// dialog height=300
	var dlgCenterLeft = WindowLeftEdge+WindowWidth/2-dlg.width()/2;// dialog height=300
	if(dlgCenterTop<0)dlgCenterTop=0;
	if(dlgCenterLeft<0)dlgCenterLeft=0;
	//alert(dlgCenterTop+","+dlgCenterLeft);
	dlg.css("top",dlgCenterTop+"px");
	dlg.css("left",dlgCenterLeft+"px");
}












//	dlg.Resizable({
//				minWidth: 50,
//				minHeight: 50,
//				/*maxWidth: 400,
//				maxHeight: 400,
//				minTop: 50,
//				minLeft: 50,
//				maxRight: 700,
//				maxBottom: 500,
//				dragHandle: true,
//				onDrag: function(x, y)
//				{
//					this.style.backgroundPosition = '-' + (x - 50) + 'px -' + (y - 50) + 'px';
//				},*/
//				handlers: {
//					se: '#dialog2_resizeSE',
//				},
//				onResize : function(size, position) {
//					//this.style.backgroundPosition = '-' + (position.left - 50) + 'px -' + (position.top - 50) + 'px';
//					//this.style.backgroundPosition = '-' + (position.left - 50) + 'px -' + (position.top - 50) + 'px';
//					this.style.backgroundPosition.height=position.height;
//					this.style.backgroundPosition.width=position.width;
//				}
//			}
//
//	);
