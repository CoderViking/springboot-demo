jQuery(function($) {
    $('#login').on('click',function () {
        const account = $('#account').val();
        const password = $('#password').val();
        $.ajax({
            type: "post",
            url:"/api/table",
            data:{
                account:account,
                password:password
            },
            async:true,
            dataType:"json",
            success:function(res){
                console.log("success:"+res);
            },
            error:function(err){
                console.log("error:"+err);
            }
        });
    });
});