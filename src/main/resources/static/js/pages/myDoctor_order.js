$(function() {
    var wechat_id = getItem('wechat_id');
    var serviceIdList = getUrlParam('serviceIdList');
    var phone =getItem('phone');
    var serviceArray = serviceIdList.split(",");
    // 取消
    $('#cancel').on('click', function() {
        window.location.href = 'myDoctor.html';
    });

    //  确定
    $('#confirm').on('click', function() {

        getBoughtServices();
        // 获取已购买的服务包
        function getBoughtServices() {
            $.ajax({
                url: 'http://mrxiej.ngrok.wendal.cn/api-wechat/patientinfo/service/get',
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
                        $.alert('加载失败,请重新进入',function () {
                            window.history.back();
                        });
                    } else {
                        var data = result.data;
                        var flag = false;
                        if (data && data.length > 0) {
                            $.each(data, function(index, service) {
                                if(service.indent_status==1){
                                    for(var i=0;i<serviceArray.length;i++){
                                    if(service.service_id == parseInt(serviceArray[i])){
                                        flag = true;
                                        // $.alert('您已经购买过'+ service.name + '，请重新选择', '提示',function () {
                                        //     window.location.href = 'myDoctor.html';
                                        // });
                                        $.alert({
                                            title: '提示',
                                            text: '您已经购买过'+ service.name + '，请重新选择',
                                            onOK: function () {
                                                window.location.href = 'myDoctor.html';
                                            }
                                        });

                                        break;
                                    }
                                    }
                                }
                                if(flag){
                                    return false;
                                }
                            });
                        }
                        if(!flag){
                            buyServices();
                        }
                    }
                },
                error: function(xhr, status, error) {
                    $.alert("请检查网络是否通畅",function () {
                        window.history.back();
                    });
                },
                complete: function(xhr, status) {
                    $.hideLoading();
                }
            });
        }


        function buyServices() {
            var checkProcotol = $('#checkProcotol').is(':checked');
            if (checkProcotol) {
                $.ajax({
                    url: 'http://mrxiej.ngrok.wendal.cn/api-wechat/patientinfo/service/buy',
                    type: 'POST',
                    timeout: 5000,
                    data: {
                        wechat_id: wechat_id,
                        servicelist: serviceIdList,
                        phone:phone
                    },
                    beforeSend: function() {
                        $.showLoading('请稍等');
                    },
                    success: function(result, status, xhr) {
                        if (result.errorcode != '0') {
                            if(result.errorcode=='1'){
                                $.alert('您已经有自己的私人医生，无法购买其他医生的服务', '提示',function () {
                                    window.location.href = 'orderService.html';
                                });
                            }else if(result.errorcode=='2'){
                                $.alert('您还没有完善信息,请选择菜单栏个人服务完善个人信息后再购买服务', '提示',function () {
                                    window.location.href = "javascript:WeixinJSBridge.call('closeWindow')";
                                });
                            }
                            else{
                                $.alert('购买失败，请稍候再试', '提示',function () {
                                    window.location.href = 'orderService.html';
                                });
                            }
                        } else {
                            $.alert('购买成功', '提示',function () {
                                window.location.href = 'orderService.html';
                            });
                        }

                    },
                    error: function(xhr, status, error) {
                        $.alert('网络慢', '提示',function () {
                            window.history.back();
                        });
                    },
                    complete: function(xhr, status) {
                        $.hideLoading();
                    }
                });
            } else {
                $.alert('请您阅读并同意服务协议', '提示');
            }
        }

    });

    getServiceOrder();


    // 获取服务订单
    function getServiceOrder() {
        $.ajax({
            url: 'http://mrxiej.ngrok.wendal.cn/api-wechat/patientinfo/service/getbylist',
            type: 'GET',
            timeout: 5000,
            data: {
                servicelist: serviceIdList
            },
            beforeSend: function() {
                $.showLoading();
            },
            success: function(result, status, xhr) {
                if (!result || result.errorcode != '0') {
                    $.alert('加载失败，请重新进入',function () {
                        window.history.back();
                    });
                } else {
                    var data = result.data;
                    if (data && data.length > 0) {
                        var orders = 0;
                        var expense = 0;
                        $.each(data, function(index, service) {
                            addOrder(service);
                            orders++;
                            expense += parseInt(service.price);
                        });
                        $('#totalOrders').html(orders);
                        $('#totalExpense').html(expense);
                    }
                }
            },
            error: function(xhr, status, error) {
                $.alert('请检查您的网络是否通畅',function () {
                    window.history.back();
                })
            },
            complete: function(xhr, status) {
                $.hideLoading();
            }
        });
    }

    // 添加订单
    function addOrder(service) {
        var str = '<div class="weui-media-box weui-media-box_text">' +
            '<p hidden="hidden" data-id="' + service.id + '"></p>' +
            '<div class="flex-r service-title">' +
            '<div class="title-left">' +
            '<p class="title-ellipsis service-name">' + service.name + '</p>' +
            '</div>' +
            '<div class="title-right">' +
            '<p class="text-red text-right text-ellipsis">' +
            '<span class="service-price">' + service.price + '</span>元' +
            '</p>' +
            '</div>' +
            '</div>' +
            '<div class="flex-r service-content">' +
            '<p>总次数: <span class="service-count">' + service.count + '</span>次</p>' +
            '<p>' +
            '期限: <span class="service-duration">' + service.duration + '</span>天' +
            '</p>' +
            '</div>' +
            '<div class="flex-r service-content">' +
            '<p class="text-default">适用人群: <span class="service-kind">' +
            service.kind + '</span></p>' +
            '</div>' +
            '</div>';
        $('#orderList').append(str);
    }


});
