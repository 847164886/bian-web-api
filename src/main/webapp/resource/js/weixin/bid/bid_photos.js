define('main', function (using) {
    var slide = using('ui/slide');
    var num = parseInt($('#photo_nun').val()); //显示第几张图片
    var b= slide('.ct-pics', {
        continuousScroll : true,
        speed : 5000,
        lazyLoad : true,
        autoSwipe : false,
        transitionType : 'cubic-bezier(0.22, 0.69, 0.72, 0.88)',
        firstCallback : function(i, sum, me) {
            me.find('.num').text('1 / ' + sum);
        },
        callback : function(i, sum, me) {
            me.find('.num').text(i + 1 + ' / ' + sum);
        }
    });
    b[0].goTo(num-1);
});
