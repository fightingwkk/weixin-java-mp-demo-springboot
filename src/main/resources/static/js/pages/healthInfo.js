$(function() {
    var wechat_id = getItem('wechat_id');
    // 阻止键盘弹出
    $('.weui-cells').on('click', 'a', function(event) {
        document.activeElement.blur();
    });

    $(function() {
        $('header').on('click', '.top-left', function() {
            window.history.back();
        });
    });


    $("#diabetics,#coronaryHeart,#stroke,#hypertention,#familyHistory").picker({
        title: "请选择",
        cols: [{
            textAlign: 'center',
            values: ['', '有', '无']
        }]
    });

    $("#smoke,#drink").picker({
        title: "请选择",
        cols: [{
            textAlign: 'center',
            values: ['', '是', '否']
        }]
    });

    // BMI计算
    function bmi() {
        var height = parseFloat($('#height').val()) / 100;
        var weight = parseFloat($('#weight').val());
        if (height && weight) {
            var value = (weight / height / height).toFixed(2);
            $('#bmi').val(value);
            if(value < 18.5){
                $('#value').val('过轻');
                $('#value').css('color','#03B2D8');
            }else if(value >= 18.5 && value <= 23.9){
                $('#value').html('正常');
                $('#value').css('color','#0FA420');

            }else if(value > 23.9 && value <= 27.9){
                $('#value').val('偏胖');
                $('#value').css('color','#FFAA25');
            }else if(value > 27.9){
                $('#value').val('肥胖');
                $('#value').css('color','#D6071F');
            }
        }
    }

    $('#height,#weight').on('change', function() {
        bmi();
    });

    //隐藏输入框
    if($('#familyHistory').val() == '无' || $('#familyHistory').val() == ''){
        $('#familyHistoryDetailDiv').css('display', 'none');
    }
    if($('#smoke').val() == '否' || $('#smoke').val() == ''){
        $('#smokeAccountDiv').css('display', 'none');
    }
    if($('#drink').val() == '否' || $('#drink').val() == ''){
        $('#drinkAccountDiv').css('display', 'none');
    }

    // 家族病史
    $('#familyHistory').on('change', function() {
        if ($(this).val() == '有') {
            $('#familyHistoryDetail').removeAttr('disabled');
            $('#familyHistoryDetailDiv').css('display', 'block');
        } else {
            $('#familyHistoryDetail').attr('disabled', 'true');
            $('#familyHistoryDetailDiv').css('display', 'none');
        }
    });

    // 抽烟
    $('#smoke').on('change', function() {
        if ($(this).val() == '是') {
            $('#smokeAccount').removeAttr('disabled');
            $('#smokeAccount').val("");
            $('#smokeAccountDiv').css('display', 'block');
        } else {
            $('#smokeAccount').attr('disabled', 'true');
            $('#smokeAccount').val(0);
            $('#smokeAccountDiv').css('display', 'none');
        }
    });

    // 饮酒
    $('#drink').on('change', function() {
        if ($(this).val() == '是') {
            $('#drinkAccount').removeAttr('disabled');
            $('#drinkAccount').val("");
            $('#drinkAccountDiv').css('display', 'block');
        } else {
            $('#drinkAccount').attr('disabled', 'true');
            $('#drinkAccount').val(0);
            $('#drinkAccountDiv').css('display', 'none');
        }
    });

    // 保存数据
    $('#submitBtn').on('click', function() {
        var info = getInfo();
        if (info) {
            saveHealthInfo(info);
        } else {
            $.alert('请将所有内容填写完整！')
        }
    });

    getHealthInfo();

    // 获取健康档案数据
    function getHealthInfo() {
        $.ajax({
            url: 'http://mrxiej.ngrok.wendal.cn/api-wechat/healthmanage/healthcheck/get',
            type: 'GET',
            timeout: 5000,
            data: {
                wechat_id: wechat_id
            },
            beforeSend: function() {
                $.showLoading();
            },
            success: function(result, status, xhr) {
                if (!result || result.errorcode != '0') {
                    $.alert("数据加载失败，请重新进入", function() {
                        window.history.back();
                    });
                } else if(result.data !=null){
                    var data = result.data;
                    setInfo(data);
                    $.hideLoading();
                }else{
                    $.hideLoading();
                }
            },
            error: function(xhr, status, error) {
                    $.alert('网络连接不通畅');
            },
        });
    }


    // 保存健康档案数据
    function saveHealthInfo(info) {
        $.ajax({
            url: 'http://mrxiej.ngrok.wendal.cn/api-wechat/healthmanage/healthcheck/saveorupdate',
            type: 'POST',
            timeout: 5000,
            data: {
                wechat_id: wechat_id,
                height: info.height,
                weight: info.weight,
                diabetes: info.diabetes,
                chd: info.chd,
                stroke: info.stroke,
                hypertension: info.hypertension,
                other_history: info.other_history,
                family_history: info.family_history,
                smoke: info.smoke,
                smoking: info.smoking,
                drink: info.drink,
                drinking: info.drinking
            },
            beforeSend: function() {
                $.showLoading();
            },
            success: function(result, status, xhr) {
                if (!result || result.errorcode != '0') {
                    window.location.href = 'msg_error.html';
                } else {
                    setTimeout(function () {
                        $.alert({
                            title: '提示',
                            text: '成功保存个人健康信息',
                            onOK: function () {
                                window.history.back();
                            }
                        });
                    },0)

                }
            },
            error: function(xhr, status, error) {
               setTimeout(function () {
                   $.alert('网络连接不通畅',function () {
                   })
               });
            },
            complete: function(xhr, status) {
                $.hideLoading();
            }
        });
    }

    // 获取输入信息
    function getInfo() {
        var height = $('#height').val();
        var weight = $('#weight').val();
        var bmi = $('#bmi').val();
        var diabetics = $('#diabetics').val();
        var coronaryHeart = $('#coronaryHeart').val();
        var stroke = $('#stroke').val();
        var hypertention = $('#hypertention').val();
        var otherHistory = $('#otherHistory').val();
        var familyHistory = $('#familyHistory').val();
        var familyHistoryDetail = $('#familyHistoryDetail').val();
        var smoke = $('#smoke').val();
        var smokeAccount = $('#smokeAccount').val();
        var drink = $('#drink').val();
        var drinkAccount = $('#drinkAccount').val();
        if (!otherHistory) {
            otherHistory = '无';
        }
        var familyContent = familyHistory;
        if (familyHistory == '有') {
            familyContent = familyHistoryDetail;
        }

        if (height && weight && bmi && diabetics && coronaryHeart && stroke &&
            hypertention && otherHistory && familyContent && smoke &&
            smokeAccount && drink && drinkAccount) {
            var info = {
                height: height,
                weight: weight,
                diabetes: diabetics,
                chd: coronaryHeart,
                stroke: stroke,
                hypertension: hypertention,
                other_history: otherHistory,
                family_history: familyContent,
                smoke: smoke,
                smoking: smokeAccount,
                drink: drink,
                drinking: drinkAccount
            };
            return info;
        } else {
            return null;
        }
    }

    // 设置健康档案信息
    function setInfo(info) {
        $('#height').val(info.height);
        $('#weight').val(info.weight);
        $('#diabetics').val(info.diabetes);
        $('#coronaryHeart').val(info.chd);
        $('#stroke').val(info.stroke);
        $('#hypertention').val(info.hypertension);
        $('#otherHistory').val(info.other_history);
        $('#smoke').val(info.smoke);
        $('#smokeAccount').val(info.smoking);
        $('#drink').val(info.drink);
        $('#drinkAccount').val(info.drinking);
        var height = parseInt(info.height) / 100;
        var weight = parseInt(info.weight)
        $('#bmi').val((weight / height / height).toFixed(2));
        var familyContent = info.family_history;
        if (familyContent == '无') {
            $('#familyHistory').val(familyContent);
        } else {
            $('#familyHistory').val("有");
            $('#familyHistoryDetail').val(familyContent);
            $('#familyHistoryDetail').removeAttr('disabled');
        }
        bmi();
    }
});
