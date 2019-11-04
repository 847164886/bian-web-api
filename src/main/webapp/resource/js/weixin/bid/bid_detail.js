
define('main', function (using) {
    var slide = using('ui/slide');
    var layer = using('ui/layer');
    var core = using('util/core');
    var error_prompt = $('#error_bg');
    var photoNums = 0;

    init();
    function init() {
        slide('#photo_slide', {
            continuousScroll: true,
            speed: 5000,
            lazyLoad: true,
            transitionType: 'cubic-bezier(0.22, 0.69, 0.72, 0.88)',
            firstCallback: function (i, sum, me) {
                photoNums = me.find('ul').children().length;
                setPhotoNum(1);
            },
            callback: function (i, sum, me) {
                setPhotoNum(i + 1);
            }
        });
    }
    function setPhotoNum(index) {
        $('.num_pics').text(index + '/' + photoNums);
    }
    $('#bid_money').on('input',function(){
        var bid_money=$('#bid_money');
        var bid_money_length=bid_money.val().length;
        if(bid_money_length>9){
            bid_money.val(bid_money.val().substring(0,9));
        }
    });
    //倒计时、
    setInterval(function(){
        fn_countdown ();
    },100);
    function fn_countdown (){
        var cut_time=$('#countSeconds').val()/1000;
        var timestamp = Date.parse(new Date())/1000; //获取日期与时间
        var rest_time=cut_time-timestamp;
        var hours=parseInt(rest_time/3600);
        var hours_s=rest_time%3600;
        var minutes=parseInt(hours_s/60);
        var seconds=hours_s%60;
        if(rest_time<=1800){
            $('.countdown_time').children('div').addClass('time_over');
        }
        if(cut_time>timestamp){
            if(rest_time<=1800){
                $('.countdown_time').children('div').addClass('time_over');
            }
            if(hours<10){
                hours='0'+hours+'';
            }
            if(minutes<10){
                minutes='0'+minutes+'';
            }
            if(seconds<10){
                seconds='0'+seconds+'';
            }
            $('#hours').text(hours);
            $('#minutes').text(minutes);
            $('#seconds').text(seconds);
            $('.countdown_box').show();

        }else if(cut_time<=timestamp){
            $('#hours').text('00');
            $('#minutes').text('00');
            $('#seconds').text('00');
            $('.countdown_box').hide();
            return;
        }

    };
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
    $('#app_in_close').on('click',function(){
        $('.app_download').hide();
    })

    if($('#isVideo').val()){
        $('#show_video').show();
    }

    //播放视频
    $('#show_video').on('click',function(){
        $('#photo_slide').hide();
        $('#video').show();
        $('#video video').attr('autoplay','autoplay');
        $('#back_img').show();
    });
    $('#back_img').on('click',function(){
        $('#video').hide();
        $('#photo_slide').show();
        $('#back_img').hide();
    });


    var shareLink=window.location.href;
    var shareTitle='';
    var imgUrl='';
    var regist=$('#regist').text();
    var mileage=$('#mileage').text();
    var desc='初登日期：'+regist+'\n'+'行驶里程：'+mileage+'公里';
    var car_name_text=$('#carMsg').val();
    var imgUrlSrc=$('#photo_slide').find('img').eq(-1).attr('src');
    if(car_name_text==''||car_name_text==null||car_name_text==undefined){
        shareTitle='更多好车尽在车城！';
        imgUrl='http://img.che.com/wx/cs_icon.jpg';
    }else{
        shareTitle=car_name_text;
        imgUrl=imgUrlSrc;
    }
    $.ajax({
        url: '/csjpapp/share/weixin/getToken',
        type: 'get',
        dataType: 'json',
        data: {
            url: window.location.href
        },
        success: function (res) {
            // 微信配置
            wx.config({
                debug: false,
                appId: "wx4c6779f0a76feb07",
                timestamp: res.times,
                nonceStr: res.randStr,
                signature: res.sign,
                jsApiList: ['onMenuShareTimeline', 'onMenuShareAppMessage'] // 功能列表，我们要使用JS-SDK的什么功能
            });
        }
    });

    wx.ready(function () {
        // 获取“分享到朋友圈”按钮点击状态及自定义分享内容接口

        wx.onMenuShareTimeline({
            title: shareTitle, // 分享标题
            link:shareLink,
            imgUrl:imgUrl // 分享图标
        });
        // 获取“分享给朋友”按钮点击状态及自定义分享内容接口
        wx.onMenuShareAppMessage({
            title: shareTitle, // 分享标题
            desc: desc, // 分享描述
            link:shareLink,
            imgUrl:imgUrl, // 分享图标
            type: 'link' // 分享类型,music、video或link，不填默认为link
        });
    });
});