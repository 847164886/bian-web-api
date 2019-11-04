define('main', function () {
    var height=document.body.clientHeight;
    var width=document.body.clientWidth;

    $('.close').on('click',function(){
        $('.mask').hide();
    });
    $('#show_suggestion').on('click',function(){
        $('.mask').show();
        var marginTop=(height-$('.price-suggest').height())/2-20;
        $('.price-suggest').css({'margin-top':marginTop});
    });
    $('#sex_sel').on('click','em',function(){
        $(this).parent().children().removeClass('sel');
        $(this).addClass('sel');
    });

    function err_show(){
        $('.error').show();
        var marginTop=(height-$('.error-box span').height())/2;
        var marginLeft=(width-$('.error-box span').width())/2;
        $('.error-box').css({'margin-top':marginTop,'margin-left':marginLeft});
        setTimeout(function(){
            $('.error').hide();
        },2000)
    }

    //城市选择
    var provinceId='';
    var cityId='';
    var province_name='';
    var city_name='';
    var ping_height=$(window).height();
    $('#city').on('click',function(){
        fn_car_address();
    });

    //ajax获取省份
    $.ajax({
        url:'/m/area/province.xhtml',
        type:'get',
        dataType:'json',
        success:function(res5){
            var domString05 = '';
            $.each(res5.provinces, function (index, item) {
                domString05 += '<p class="sheng-item" data-provinceid="' + item.provinceId + '" data-provinceName="' + item.province + '">' + item.province + '</p>';
            });
            $('#province_choose_content').empty().append(domString05);
        }
    });

    ////测试
    //var domString05='';
    //domString05+='<p>上海</p>'+
    //    '<p>北京</p>'+
    //    '<p>天津</p>'+
    //    '<p>江苏</p>';
    //$('#province_choose_content').empty().append(domString05);

    //提交按钮
    $('#commit').on('click',function(){
        var userName=$('#userName').val();
        var userMobile=$('#userMobile').val();
        var carType=$('#carType').val();
        var userSex='1';
        if($('girls').hasClass('sel')){
            userSex=1;
        }else{
            userSex=2;
        }
        var userCity=$('#city').text();
        var userPrice=$('#userPrice').val();
        //为空判断
        if(!userName){
            $('#err_msg').text('请输入您的姓名!');
            err_show();
            return
        }
        if(!userMobile){
            $('#err_msg').text('请输入您的手机号码!');
            err_show();
            return
        }
        if(userCity=='请选择城市'){
            $('#err_msg').text('请选择城市!');
            err_show();
            return
        }
        //手机号码验证
        if (!checkMobile(userMobile)){
            $('#err_msg').text('请输入正确的手机号码!');
            err_show();
            return
        }
        $.ajax({
            url:'',
            type:'post',
            dataType:'json',
            data:{

            },
            success:function(res){

            }

        })
    });


    function fn_car_address(){
        $('#choose_city').css('height',ping_height);
        $('body').css('height',ping_height);
        $('body,html').css('overflow','hidden');
        var province_id='';
        $('#choose_city').show();
        $('#province_choose').show();
        $('#province_choose_content').on('click','p',function(){
            province_name=$(this).text();
            province_id=$(this).data('provinceid');
            provinceId=province_id;
            $('#province_choose').hide();
            $('#city_choose').show();

            ////测试
            //var domString06 = '';
            //domString06 += '<p>上海</p>'+
            //    '<p>北京</p>'+
            //    '<p>天津</p>'+
            //    '<p>南京</p>';
            //
            //$('#city_choose_content').empty().append(domString06);




            $.ajax({
                url:'/m/area/city.xhtml',
                type:'get',
                dataType:'json',
                data:{
                    province_id:province_id
                },
                success:function(res6){
                    var domString06 = '';
                    $.each(res6.citys, function (index, item) {
                        domString06 += '<p class="shi-item" data-city_id="'+item.cityId+'" data-city_name="'+item.city+'">'+item.city+'</p>';
                    });
                    $('#city_choose_content').empty().append(domString06);
                }
            })
        });
        $('#city_choose_content').on('click','p',function(){
            city_name=$(this).text();
            cityId=$(this).data('city_id');
            if(city_name==province_name){
                $('#city').text(city_name);
            }else{
                $('#city').text(province_name+'-'+city_name);
            }
            if($('#city').text().length>0){
                $('.city_input').text('');
            }
            $('#choose_city').hide();
            $('#province_choose').hide();
            $('#city_choose').hide();
            $('body').css('height','auto');
            $('body,html').css('overflow','initial');
        });
    }

    //省的关闭按钮
    $('#sheng_close').on('click',function(){
        $('#choose_city').hide();
        $('body').css('height','auto');
        $('body,html').css('overflow','initial');
    });
    //城市的关闭按钮
    $('#city_close').on('click',function(){
        $('#province_choose').show();
        $('#city_choose').hide();
    });

    //验证手机号码
    function checkMobile(phone) {
        var re = /^(13|14|15|17|18)[0-9]{9}$/;
        return re.test(phone);
    }

});