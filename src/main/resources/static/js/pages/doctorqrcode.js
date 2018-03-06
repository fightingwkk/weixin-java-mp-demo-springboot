$(function() {
    function getUrlParam(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
        var r = window.location.search.substr(1).match(reg);
        if(r != null) {
            return unescape(r[2]);
        }
        return null;
    }
    var phone = getUrlParam('phone');
    var wechat_id = getUrlParam('wechat_id');
    setItem('wechat_id', wechat_id);
    setItem('phone', phone);
    $(function() {
        $('header').on('click', '.top-left', function() {
            window.history.back();
        });
    });
    getDoctorInfo();
    // 获取医生信息
    function getDoctorInfo() {
        $.ajax({
            url: 'http://mrxiej.ngrok.wendal.cn/api-wechat/doctor/get',
            type: 'GET',
            timeout: 5000,
            data: {
                phone: phone
            },
            beforeSend: function() {
                $.showLoading();
            },
            success: function(result, status, xhr) {
                if (result.errorcode != '0') {
                    $.alert({
                        title: '提示',
                        text: '加载失败，请过一段时间再尝试，给您带来的不遍我们深感抱歉.',
                        onOK: function () {
                            javascript:WeixinJSBridge.call('closeWindow');
                        }
                    });
                } else {
                    $.alert('想拥有您自己的私人医生需要购买医生提供的服务','提示');
                    var data = result.data;
                    console.log(data)
                    if (data) {
                        if(data.head_pic!=null){
                            $('#portait').attr('src', data.head_pic);
                        }
                        $('#personName').html(data.name);
                        $('#personDepartment').html(data.department);
                        $('#personPost').html(data.title);
                        $('#personHospital').html(data.hospital);
                        $('#personAdept').html(data.adept);
                        $('#personExperience').html(data.experience);
                        $('#personQrcode').attr('src', data.qrcode_pic);
                    }
                }
            },
            error: function(xhr, status, error) {
                $.alert('网络不通畅');
                javascript:WeixinJSBridge.call('closeWindow');
            },
            complete: function(xhr, status) {
                $.hideLoading();
            }
        });
    }


});
