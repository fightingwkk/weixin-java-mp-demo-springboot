$(function() {
    var wechat_id = getItem('wechat_id');
    var count = getUrlParam('count');
    var prob=0;
    if(count=='new'){
        setTimeout(function () {
        },1000);
        getNewestReport();
    }else{
        getReport();
    }

    var radialObj = radialIndicator('#indicatorContainer', {
        // barColor: '#20a5ac',
        barColor: {0: '#20a5ac', 0.99: '#20a5ac', 1: '#ebb416', 4.99: '#ebb416', 5: '#ff621d', 9.99: '#ff621d', 10: '#ff2a2a', 100: '#ff2a2a'},
        barWidth: 15,
        minValue: 0,
        maxValue: 100,
        fontWeight: 'normal',
        fontSize: 20,
        roundCorner: true,
        radius:60,
        format: function () {
            if(prob==0)
            {return 0;}
            else{
            var xiaoshu=parseInt(prob*100-parseInt(prob)*100);
            return parseInt(prob)+"."+xiaoshu+"%";}
        }
    });

    // 获取指定的风险评估报告
    function getReport() {
        $.ajax({
            url: 'http://mrxiej.ngrok.wendal.cn/api-wechat/healthmanage/report/getbycount',
            type: 'GET',
            timeout: 5000,
            data: {
                wechat_id: wechat_id,
                count:count
            },
            beforeSend: function() {
                $.showLoading();
            },
            success: function(result, status, xhr) {
                if (result.errorcode != '0') {
                    setTimeout($.alert('数据加载失败',function () {
                        window.history.back();
                    }),0);
                } else {
                    var data = result.data;
                    parseReport(data)
                }
            },
            error: function(xhr, status, error) {
                setTimeout($.alert('网络慢',function () {
                    window.history.back();
                }),0);
            },
            complete: function(xhr, status) {
                $.hideLoading();
            }
        });
    }
    // 获取最新的风险评估报告
    function getNewestReport() {
        $.ajax({
            url: 'http://mrxiej.ngrok.wendal.cn/api-wechat/healthmanage/report/getnewest',
            type: 'GET',
            timeout: 5000,
            data: {
                wechat_id: wechat_id
            },
            beforeSend: function() {
                $.showLoading();
            },
            success: function(result, status, xhr) {
                if (result.errorcode != '0') {
                    setTimeout($.alert('获取报告数据失败，请稍后重试',function () {
                        window.history.back();
                    }),0);
                } else {
                    var data = result.data;
                    parseReport(data)
                }
            },
            error: function(xhr, status, error) {
                setTimeout($.alert('网络慢',function () {
                    window.history.back();
                }),0);
            },
            complete: function(xhr, status) {
                $.hideLoading();
            }
        });
    }

    // 解析数据
    function parseReport(data) {
        prob = parseFloat(data.prob);
        radialObj.value(prob);
        if (prob >= 10) {
            radialObj.option('barColor', '#ff2a2a');
            $('.text-default').css('color', '#ff2a2a');
        }else if (prob >= 5) {
            radialObj.option('barColor', '#ff621d');
            $('.text-default').css('color', '#ff621d');
        }else if (prob >= 1) {
            radialObj.option('barColor', '#ebb416');
            $('.text-default').css('color', '#ebb416');
        }
        $('#content').html(data.content);
    }

});
