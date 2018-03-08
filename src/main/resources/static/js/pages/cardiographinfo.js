$(function() {
    var wechat_id = getItem('wechat_id');

    //获取心电图
    function getCardiographinfo() {
        $.ajax({
            url: 'http://mrxiej.ngrok.wendal.cn/api-wechat/healthmanage/cardiograph/get',
            type: 'GET',
            timeout: 5000,
            data: {
              wechat_id : wechat_id
            },
            beforeSend: function() {
                $.showLoading();
            },
            success: function(result, status, xhr) {
                if (!result || result.errorcode != '0' || !result.data) {
                    $.alert({
                        title: '提示',
                        text: '加载心电图失败.',
                        onOK: function () {
                            window.history.back();
                        }
                    });
                } else {
                    var data = result.data;
                    console.log(result);
                    if (data) {
                        $('.date').html('测量时间：'+ data.date);
                        $('.remark').html('备注：'+ data.remark);
                        $('.cardiographpic').attr('src', data.cardiogram);
                    }
                }
            },
            error: function(xhr, status, error) {
                $.alert("请检查您的网络是否通畅");
                javascript:WeixinJSBridge.call('closeWindow');
            },
            complete: function(xhr, status) {
                $.hideLoading();
            }
        });
    }
    getCardiographinfo();
    //点击头部返回
    $('header').on('click', '.top-left', function () {
        window.location.href = "healthDoc.html";
    });
    //点击添加按钮跳转
    $('.add').on('click', function () {
        window.location.href = "cardiograph.html";
    });
});