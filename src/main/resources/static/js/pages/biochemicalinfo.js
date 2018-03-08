$(function () {
    var wechat_id = getItem('wechat_id');

    getBiochemicalinfo();

    // 获取生化检查数据
    function getBiochemicalinfo() {
        $.ajax({
            url: 'http://mrxiej.ngrok.wendal.cn/api-wechat/healthmanage/biologycheck/get',
            type: 'GET',
            timeout: 5000,
            data: {
                wechat_id: wechat_id
            },
            beforeSend: function () {
                $.showLoading();
            },
            success: function (result, status, xhr) {
                if (!result || result.errorcode != '0') {
                    $.alert("数据加载失败，请重新进入", function () {
                        window.history.back();
                    });
                } else if (result.data != null) {
                    var data = result.data;
                    loadData(data);
                    //隐藏输入框
                    $.hideLoading();
                } else {
                    $.alert("没有生化检查信息，请输入信息", function () {
                        window.location.href = "biochemicalExam.html";
                    });
                    $.hideLoading();
                }
            },
            error: function (xhr, status, error) {
                $.alert('网络连接不通畅');
            },
        });
    }

    function loadData(data) {

        if (data.tch != undefined) {
            if (data.tch_time != undefined) {
                $('#tch').text(data.tch);
                $('#tch_time').text(data.tch_time);
                var tch = parseFloat(data.tch);
                if (tch < 3) {
                    $('#tch-low').addClass('visiblegx').removeClass('unvisiblegx');
                }else if (tch >= 3 && tch <= 5.2) {
                    $('#tch-normal').addClass('visiblegx').removeClass('unvisiblegx');
                } else {
                    $('#tch-height').addClass('visiblegx').removeClass('unvisiblegx');
                }
            }
        } else {
            $('#tch').next().addClass('unvisiblegx');
        }
        ;
        if (data.fbg != undefined) {
            if (data.fbg_time != undefined) {
                $('#fbg').text(data.fbg);
                $('#fbg_time').text(data.fbg_time);
                var fbg = parseFloat(data.fbg);
                if (fbg < 3.9) {
                    $('#fbg-low').addClass('visiblegx').removeClass('unvisiblegx');
                } else if (fbg >= 3.9 && fbg <= 6.1) {
                    $('#fbg-normal').addClass('visiblegx').removeClass('unvisiblegx');
                } else {
                    $('#fbg-height').addClass('visiblegx').removeClass('unvisiblegx');
                }
            }
        } else {
            $('#fbg').next().addClass('unvisiblegx');
        }
        ;
        if (data.tg != undefined) {
            if (data.tg_time != undefined) {
                $('#tg').text(data.tg);
                $('#tg_time').text(data.tg_time);
                var tg = parseFloat(data.tg);
                if (tg < 0.56) {
                    $('#tg-low').addClass('visiblegx').removeClass('unvisiblegx');
                } else if (tg >= 0.56 && tg <= 1.70) {
                    $('#tg-normal').addClass('visiblegx').removeClass('unvisiblegx');
                } else {
                    $('#tg-height').addClass('visiblegx').removeClass('unvisiblegx');
                }
            }
        } else {
            $('#tg').next().addClass('unvisiblegx');
        }
        ;
        if (data.hdl != undefined) {
            if (data.hdl_time != undefined) {
                $('#hdl').text(data.hdl);
                $('#hdl_time').text(data.hdl_time);
                var hdl = parseFloat(data.hdl);
                if (hdl < 0.70) {
                    $('#hdl-low').addClass('visiblegx').removeClass('unvisiblegx');
                } else if(hdl >=0.70 && hdl <= 2.00){
                    $('#hdl-normal').addClass('visiblegx').removeClass('unvisiblegx');
                }else {
                    $('#hdl-height').addClass('visiblegx').removeClass('unvisiblegx');
                }
            }
        } else {
            $('#hdl').next().addClass('unvisiblegx');
        }
        ;
        if (data.ldl != undefined) {
            if (data.ldl_time != undefined) {
                $('#ldl').text(data.ldl);
                $('#ldl_time').text(data.ldl_time);
                var ldl = parseFloat(data.ldl);
                if (ldl < 2.10) {
                    $('#ldl-low').addClass('visiblegx').removeClass('unvisiblegx');
                } else if(ldl >= 2.10 && ldl <= 3.12) {
                    $('#ldl-normal').addClass('visiblegx').removeClass('unvisiblegx');
                }else {
                    $('#hdl-height').addClass('visiblegx').removeClass('unvisiblegx');
                }
            }
        } else {
            $('#ldl').next().addClass('unvisiblegx');
        }
        ;
    }

    //点击重新测量跳转
    $('.reset').on('click', function () {
        window.location.href = "biochemicalExam.html";
    });
    //点击头部返回
    $('header').on('click', '.top-left', function () {
        window.location.href = "healthDoc.html";
    });

});