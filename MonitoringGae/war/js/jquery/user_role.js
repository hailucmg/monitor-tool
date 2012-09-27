jQuery.noConflict();

jQuery(document).ready(function() {
	jQuery(".ckUserRole").live('click',function(){
		username = jQuery(this).attr('username');
		role = jQuery(this).attr('role');
		if(jQuery(this).is(":checked")){
			window.updateUserRole(username,role,true);
		}else{
			window.updateUserRole(username,role,false);
		}
	});
}
  
