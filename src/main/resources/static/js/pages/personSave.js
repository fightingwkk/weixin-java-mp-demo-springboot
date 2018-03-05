$(function() {
    var wechat_id = getUrlParam('wechat_id');
    var headimg = getUrlParam('headimg');
    $('#portait').attr('src', headimg);
    var isUpdate = false;
    var saveUrl = 'http://mrxiej.ngrok.wendal.cn/api-wechat/patientinfo/save';
    var updateUrl = 'http://mrxiej.ngrok.wendal.cn/api-wechat/patientinfo/update';

    $.ajax({
        url: 'http://mrxiej.ngrok.wendal.cn/api-wechat/patientinfo/get',
        type: 'GET',
        data: {
            wechat_id: wechat_id
        },
        timeout: 30000,
        beforeSend: function() {
            $.showLoading();
        },
        success: function(result, status, xhr) {
            if (result.errorcode!="0") {
                if(result.errorcode!="10003") {
                    window.location.href = 'msg_error.html';
                }
                else{
                }
            } else {
                isUpdate = true;
                presult=result.data;
                $('#name').val(presult.name);
                $('#identify').val(presult.id_card);
                $('#sex').val(presult.sex);
                $('#age').val(presult.age);
                $('#telephone').val(presult.phone);
                $('#address').val(presult.address);
                $('#detail').val(presult.detailed_address);
            }
        },
        complete: function(status, xhr) {
            $.hideLoading();
        }
    });

    // 阻止键盘弹出
    $('#sex').on('click', function(event) {
        document.activeElement.blur();
    });

    $("#sex").picker({
        title: "请选择您的性别",
        cols: [{
            textAlign: 'center',
            values: ['男', '女']
        }]
    });

    // 阻止键盘弹出
    $('#address').on('click', function(event) {
        document.activeElement.blur();
    });

// 百度地图API功能
    var addressStr = "";
    var map = new BMap.Map("map");
    var point = new BMap.Point(116.331398,39.897445);
    map.centerAndZoom(point,12);
    var geolocation = new BMap.Geolocation();
    geolocation.getCurrentPosition(function(r){
        if(this.getStatus() == BMAP_STATUS_SUCCESS){
            var mk = new BMap.Marker(r.point);
            map.addOverlay(mk);
            map.panTo(r.point);
            // 根据坐标得到地址描述
            var myGeo = new BMap.Geocoder();
            myGeo.getLocation(r.point, function(result){
                if (result){
                    addressStr = result.addressComponents.province + " " + result.addressComponents.city + " " + result.addressComponents.district;
                    // $('#address').attr("data-code","330101");
                    // $('#address').attr("data-codes","330000,330100,330101");
                    $('#address').val(addressStr);
                }
            });
        }
        else {
            // alert('failed'+this.getStatus());
            alert('获取地理位置失败');
        }
    },{enableHighAccuracy: true})


    $('#address').cityPicker({
        title: "选择联系地址",
        showDistrict: true,
        //value: addressStr,
        inputReadOnly: false,
        onChange: function(picker, values, displayValues) {

        }
    });

    $('#submitBtn').on('click', function() {
        $.confirm({
            title: '保存',
            text: '确定要保存修改吗？',
            onOK: function() {
                var $name = $('#name');
                var $identify = $('#identify');
                var $sex = $('#sex');
                var $age = $('#age');
                var $telephone = $('#telephone');
                var $address = $('#address');
                var $detail = $('#detail');
                if($name.val() && $identify.val() && $sex.val() && $age.val() && $telephone.val() &&
                    $address.val() && $detail.val()) {
                    var person = {
                        wechat_id: wechat_id,
                        name: $name.val(),
                        id_card: $identify.val(),
                        sex: $sex.val(),
                        age: $age.val(),
                        phone: $telephone.val(),
                        address: $address.val(),
                        detailed_address: $detail.val(),
                        headpic: headimg
                    };

                    if (isUpdate) {
                        savePerson(updateUrl, person, function () {
                        });
                    } else {
                        savePerson(saveUrl, person, function () {
                        });
                    }
                }else{
                    $.alert("请将信息填写完整");
                }
            },
            onCancel: function() {
                //点击取消后的回调函数
            }
        });
    });

    /**
    * 保存数据
    * @para
    * url: 保存路径
    * person: 保存对象
    * calback: 回调函数
    *
    */
    function savePerson(url, person, calback) {
        $.ajax({
            url: url,
            type: 'POST',
            data: {
                wechat_id: person.wechat_id,
                name: person.name,
                id_card: person.id_card,
                sex: person.sex,
                age: person.age,
                phone: person.phone,
                address: person.address,
                detailed_address: person.detailed_address,
                headpic: person.headpic
            },
            success: function(result, status, xhr) {
                if (result.errorcode == "0") {
                  window.location.href = 'msg_success.html';
                }else{
                    window.location.href = 'msg_error.html';
                }
            },
            error: function (xhr, status, error) {
                setTimeout(function () {
                    $.alert("请检查您的网络是否通畅",function () {
                    })
                },0)
            },
            complete: function(status, xhr) {
                $.hideLoading();
            }
        });
    }
});
