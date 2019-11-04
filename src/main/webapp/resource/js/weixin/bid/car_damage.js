
define('main', function (using) {
    var layer = using('ui/layer');
    var core = using('util/core');
    var error_prompt = $('#error_bg');
    $('#bid_money').on('input',function(){
        var bid_money=$('#bid_money');
        var bid_money_length=bid_money.val().length;
        if(bid_money_length>9){
            bid_money.val(bid_money.val().substring(0,9));
        }
    });
    fn_bid_sub ();
    function fn_bid_sub (){
        var bid_money;
        var auction_id=$('#auction_id').val();
        var check_car_id=$('#check_car_id').val();

        var bid_sub_bg=$('#bid_sub_bg');
        $('#bid_sub_show').on('click',function(){
            bid_money=parseInt($('#bid_money').val());
            if(bid_money==''){
                error_prompt.show().children().text('请输入竞拍价格！');
                errpr_hide();
                return;
            }
            if(bid_money<1000){
                error_prompt.show().children().text('竞拍价格大于1000！');
                errpr_hide();
                return;
            }
            if(!checkNum(bid_money)){
                error_prompt.show().children().text('请输入正整数！');
                errpr_hide();
                return;
            }
            if(bid_money%100>0){
                error_prompt.show().children().text('请输入100的倍数！');
                errpr_hide();
                return;
            }
            $('#show_money').text(bid_money);
            bid_sub_bg.show();
        });
        $('#bid_cancel').on('click',function(){
            bid_sub_bg.hide();
        });
        var ladding=false;
        $('#bid_sure').on('click',function(){
            if(ladding==true){
                return;
            }
            ladding=true
            $.ajax({
                url:'/wx/auction/history/detail/bid.xhtml',
                type:'post',
                dataType:'json',
                data:{
                    auction_id:auction_id,
                    check_car_id:check_car_id,
                    price:bid_money
                },
                success:function(res){
                    ladding=false;
                    if(res.data.replyCode==0){
                        $('#bid_sub_bg').hide();
                        error_prompt.show().children().text('竞拍成功！');
                        errpr_hide()
                    }else{
                        $('#bid_sub_bg').hide();
                        error_prompt.show().children().text(res.data.message);
                        errpr_hide();
                    }
                },
                error:function(){
                    $('#bid_sub_bg').hide();
                    error_prompt.show().children().text('系统故障，竞拍失败！');
                    errpr_hide();
                    ladding=false;
                }
            })
        });
    }

    is_over ();
    function is_over (){
        var over_num=$('#isover_num').val();
        if(over_num==1){
            $('.bid_input').hide();
            $('.bid_finish').show();
        }else{
            $('.bid_input').show();
            $('.bid_finish').hide();
        }
    }
//设定遮罩层
    function errpr_hide() {
        setTimeout(function () {
            $('#error_bg').hide();
        }, 1000);
    }

    //验证正整数
    function checkNum(num) {
        var re = /^[1-9]\d*$/;
        return re.test(num);
    }
});