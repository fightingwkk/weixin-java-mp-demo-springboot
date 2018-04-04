$(function() {
    var wechat_id = getItem('wechat_id');
    setItem("healthService");

    var notification = getItem("notification");
    if(notification){
        removeItem("notification");
        location.reload();
    }

    // 删除按钮
    $('.top-right').on('click', 'a', function() {
        $(this).removeClass('visible').addClass('unvisible');
        $(this).siblings('img').removeClass('unvisible').addClass('visible');
        $('.cs-circle-check').css('display', 'flex');
    });

    // 删除消息
    $('.top-right').on('click', 'img', function() {
        var $checked = $('input[type="checkbox"]');
        var num = $checked.filter(':checked').length;
        if (num) {
            $.confirm({
                title: '删除',
                text: '确定要删除这' + num + '条消息吗？',
                onOK: function() {
                    //点击确定后的回调函数
                    $checked.filter(':checked').each(function() {
                        var $parent = $(this).closest('.weui-media-box');
                        var id = $parent.children('p[data-id]').attr('data-id').split("-");
                        if(id[0]=="data"){
                        deleteNotification(id[1]);
                        }else if(id[0]=="group"){
                         deleteGroup(id[1]);
                        }else{
                            deleteDefine(id[1]);
                        }
                    });
                    $('.cs-circle-check').css('display', 'none');
                },
                onCancel: function() {
                    //点击取消后的回调函数
                    $('.cs-circle-check').css('display', 'none');
                }
            });
        } else {
            $('.cs-circle-check').css('display', 'none');
        }
        $(this).removeClass('visible').addClass('unvisible');;
        $(this).siblings('a').removeClass('unvisible').addClass('visible');
    });

    // 查看消息详细内容
    $('#notificationList').on('click', '.weui-media-box__hd,.weui-media-box__bd', function() {
        var $parent = $(this).closest('a.weui-media-box');
        var $id = $parent.children().first();
        var id = $id.attr('data-id').split("-");
        var $noti = $parent.children('.weui-media-box__bd');
        var $title = $noti.find('[name="noti-title"]');
        var $dateTime = $noti.find('[name="noti-time"]');
        var $target = $noti.find('[name="noti-target"]');
        if(id[0]=="data"){
        setRead(id[1]);
        window.location.href = 'notification_detail.html?data-id=' + id[1];
        }else if(id[0]=="group"){
        setReadGroup(id[1]);
        window.location.href = 'notification_detail.html?group-id=' + id[1];
        }else{
            setDefineRead(id[1]);
            window.location.href = 'notification_detail.html?de-id=' + id[1];
        }
    });



    getGroupReceivingList();
    // 获取医生群发消息
    function getGroupReceivingList() {
        $.ajax({
            url: 'http://mrxiej.ngrok.wendal.cn/api-wechat/patientinfo/groupreceiving/list',
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
                    $.alert('群发消息加载失败!');
                } else {
                    var data = result.data;
                    parseGroup(data);
                }
                getDefineNotifications();
            },
            error: function(xhr, status, error) {
                $.alert('群发消息加载失败',function () {
                    window.history.back();
                });
            },
            complete: function(xhr, status) {
                $.hideLoading();
            }
        });
    }

    // 获取未读模板消息
    function getUnreadNotifications() {
        $.ajax({
            url: 'http://mrxiej.ngrok.wendal.cn/api-wechat/healthmanage/messageremind/getunread',
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
                    $.alert('模板消息加载失败!');
                } else {
                    var data = result.data;
                    parseUnread(data);
                }
            },
            error: function(xhr, status, error) {
                $.alert('模板消息加载失败',function () {
                    window.history.back();
                });
            },
            complete: function(xhr, status) {
                $.hideLoading();
            }
        });
    }

    // 获取自定义消息
    function getDefineNotifications() {
        $.ajax({
            url: 'http://mrxiej.ngrok.wendal.cn/api-wechat/healthmanage/definemessage/get',
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
                    $.alert('自定义消息加载失败!');
                } else {
                    var data = result.data;
                    console.log(data);
                    parseDefineMsg(data);
                    getUnreadNotifications();
                }
            },
            error: function(xhr, status, error) {
                $.alert('自定义消息加载失败',function () {
                    window.history.back();
                });
            },
            complete: function(xhr, status) {
                $.hideLoading();
            }
        });
    }


    // 解析未读消息
    function parseUnread(notifications) {
        if (notifications && notifications.length > 0) {
            $.each(notifications, function(index, notification) {
                addNotification(notification);
            });
        }
        var $unread = $('.red-dot');
        if ($unread.length > 0) {
            $('.notification-remind').removeClass('unvisible');
        }
    }
    // 解析自定义消息
    function parseDefineMsg(notifications) {
        if (notifications && notifications.length > 0) {
            $.each(notifications, function(index, notification) {
                addDefineMsg(notification);
            });
        }
        var $unread = $('.red-dot');
        if ($unread.length > 0) {
            $('.notification-remind').removeClass('unvisible');
        }
    }
    // 解析医生群发消息
    function parseGroup(notifications) {
        if (notifications && notifications.length > 0) {
            $.each(notifications, function(index, notification) {
                addGroup(notification);
            });
        }
        var $unread = $('.red-dot');
        if ($unread.length > 0) {
            $('.notification-remind').removeClass('unvisible');
        }
    }

    // 设置模板消息已读
    function setRead(id) {
        $.ajax({
            url: 'http://mrxiej.ngrok.wendal.cn/api-wechat/healthmanage/messageremind/setread',
            type: 'POST',
            timeout: 5000,
            data: {
                id: id
            },
            beforeSend: function() {
                $.showLoading();
            },
            success: function(result, status, xhr) {

            },
            error: function(xhr, status, error) {
            },
            complete: function(xhr, status) {
                $.hideLoading();
            }
        });
    }
    // 设置自定义消息已读
    function setDefineRead(id) {
        $.ajax({
            url: 'http://mrxiej.ngrok.wendal.cn/api-wechat/healthmanage/definemessage/setread',
            type: 'POST',
            timeout: 5000,
            data: {
                id: id
            },
            beforeSend: function() {
                $.showLoading();
            },
            success: function(result, status, xhr) {

            },
            error: function(xhr, status, error) {
            },
            complete: function(xhr, status) {
                $.hideLoading();
            }
        });
    }
    // 设置医生群发消息已读
    function setReadGroup(id) {
        $.ajax({
            url: 'http://mrxiej.ngrok.wendal.cn/api-wechat/patientinfo/groupreceiving/setread',
            type: 'POST',
            timeout: 5000,
            data: {
                id: id
            },
            beforeSend: function() {
                $.showLoading();
            },
            success: function(result, status, xhr) {

            },
            error: function(xhr, status, error) {
            },
            complete: function(xhr, status) {
                $.hideLoading();
            }
        });
    }
// 删除医生群发消息
    function deleteGroupMessage(id) {
        $.ajax({
            url: 'http://mrxiej.ngrok.wendal.cn/api-wechat/patientinfo/groupreceiving/delete',
            type: 'POST',
            timeout: 5000,
            data: {
                id: id
            },
            beforeSend: function() {
                $.showLoading();
            },
            success: function(result, status, xhr) {

            },
            error: function(xhr, status, error) {
            },
            complete: function(xhr, status) {
                $.hideLoading();
            }
        });
    }

    // 删除自定义消息
    function deleteDefineMsg(id) {
        $.ajax({
            url: 'http://mrxiej.ngrok.wendal.cn/api-wechat/healthmanage/definemessage/delete',
            type: 'POST',
            timeout: 5000,
            data: {
                id: id
            },
            beforeSend: function() {
                $.showLoading();
            },
            success: function(result, status, xhr) {

            },
            error: function(xhr, status, error) {
            },
            complete: function(xhr, status) {
                $.hideLoading();
            }
        });
    }

    // 删除模板消息
    function deleteNotification(id) {
        setRead(id);
        var $parent = $('p[data-id="data-' + id + '"]').closest('.weui-media-box');
        $parent.remove();
        var $unread = $('.red-dot');
        if ($unread.length <= 0) {
            $('.notification-remind').addClass('unvisible');
        }
    }
    // 删除自定义消息
    function deleteDefine(id) {
        deleteDefineMsg(id);
        var $parent = $('p[data-id="de-' + id + '"]').closest('.weui-media-box');
        $parent.remove();
        var $unread = $('.red-dot');
        if ($unread.length <= 0) {
            $('.notification-remind').addClass('unvisible');
        }
    }
// 删除医生群发消息
    function deleteGroup(id) {
        deleteGroupMessage(id);
        var $parent = $('p[data-id="group-' + id + '"]').closest('.weui-media-box');
        $parent.remove();
        var $unread = $('.red-dot');
        if ($unread.length <= 0) {
            $('.notification-remind').addClass('unvisible');
        }

    }
    // 添加消息通知
    function addNotification(notification) {
        var str = '<a href="javascript:void(0);" class="weui-media-box weui-media-box_appmsg">' +
            '<p hidden="hidden" data-id="data-' + notification.id + '"></p>' +
            '<div class="cs-circle-check cs-check-hidden">' +
            '<input id="check-' + notification.id + '" class="check" name="" type="checkbox" />' +
            '<label for="check-' + notification.id + '" class=""></label>' +
            '</div>' +
            '<div class="weui-media-box__hd">' +
            '<img class="weui-media-box__thumb" src="../image/voice.png" alt="" />' ;
        if(notification.isread==0){
            str+='<span class="red-dot"></span>' +
                '</div>';
        }else{
            str+='<span class="red-dot" hidden="hidden"></span>' +
                '</div>';
        }
        str+= '<div class="weui-media-box__bd">' +
            '<div class="flex-r flex-align-center flex-justify-between">' +
            '<h4 class="weui-media-box__title">' +
            '标题：<span name="noti-title">' + notification.title + '</span>' +
            '</h4>' +
            '<p class="weui-media-box__desc" name="noti-time">' + notification.datetime.split(".")[0] +
            '</p>' +
            '</div>' +
            '<p class="weui-media-box__desc text-default">' +
            '目标：<span name="noti-target">' + notification.target + '</span>' +
            '</p>' +
            '</div>' +
            '</a>';
        $('#notificationList').append(str);
    }

    // 添加自定义消息通知
    function addDefineMsg(notification) {
        var str = '<a href="javascript:void(0);" class="weui-media-box weui-media-box_appmsg">' +
            '<p hidden="hidden" data-id="de-' + notification.id + '"></p>' +
            '<div class="cs-circle-check cs-check-hidden">' +
            '<input id="check-' + notification.id + '" class="check" name="" type="checkbox" />' +
            '<label for="check-' + notification.id + '" class=""></label>' +
            '</div>' +
            '<div class="weui-media-box__hd">' +
            '<img class="weui-media-box__thumb" src="../image/voice.png" alt="" />' ;
        if(notification.isread==0){
            str+='<span class="red-dot"></span>' +
                '</div>';
        }else{
            str+='<span class="red-dot" hidden="hidden"></span>' +
                '</div>';
        }
        str+= '<div class="weui-media-box__bd">' +
            '<div class="flex-r flex-align-center flex-justify-between">' +
            '<h4 class="weui-media-box__title">' +
            '标题：<span name="noti-title">' + notification.title + '</span>' +
            '</h4>' +
            '<p class="weui-media-box__desc" name="noti-time">' + notification.datetime.split(".")[0] +
            '</p>' +
            '</div>' +
            '<p class="weui-media-box__desc text-default">' +
            '内容：<span name="noti-target">' + notification.content + '</span>' +
            '</p>' +
            '</div>' +
            '</a>';
        $('#notificationList').append(str);
    }

    // 添加医生群发消息通知
    function addGroup(notification) {
        var str = '<a href="javascript:void(0);" class="weui-media-box weui-media-box_appmsg">' +
            '<p hidden="hidden" data-id="group-' + notification.id + '"></p>' +
            '<div class="cs-circle-check cs-check-hidden">' +
            '<input id="group-' + notification.id + '" class="check" name="" type="checkbox" />' +
            '<label for="group-' + notification.id + '" class=""></label>' +
            '</div>' +
            '<div class="weui-media-box__hd">' ;
        if(notification.head_pic != null && notification.head_pic != ""){
            str += '<img class="weui-media-box__thumb" src="'+notification.head_pic+'"  style="border-radius: 50%;width: 100%;height: 100%" alt="" />';
        }else{
            str += '<img class="weui-media-box__thumb" src="../image/touxiang1@2x.png"  style="border-radius: 50%;width: 100%;height: 100%" alt="" />';
        }
        if(notification.isread==0){
            str+='<span class="red-dot"></span>' +
                '</div>';
        }else{
            str+='<span class="red-dot" hidden="hidden"></span>' +
                '</div>';
        }
             str+= '<div class="weui-media-box__bd">' +
            '<div class="flex-r flex-align-center flex-justify-between">' +
            '<h4 class="weui-media-box__title">' +
            '<span name="noti-title">' + notification.name + '</span>' +
            '</h4>' +
            '<p class="weui-media-box__desc" name="noti-time">' + notification.datetime.split(".")[0] +
            '</p>' +
            '</div>' +
            '<p class="weui-media-box__desc text-default">' +
            '内容：<span name="noti-target">' + notification.content + '</span>' +
            '</p>' +
            '</div>' +
            '</a>';
        $('#notificationList').append(str);
    }

});
