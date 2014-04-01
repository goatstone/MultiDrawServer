
$(document).ready(function(){

	//	alert(document.getElementById('ping_form'));
    $("#ping_button").bind("click",function(){
        $.ajax({
            url: "/ping",
            method: "get"
        }).done(function() {
//                $( this ).addClass( "done" );
            });
    })

}) 