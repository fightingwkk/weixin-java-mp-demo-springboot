package scut.jiayibilin.wechat.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import org.springframework.stereotype.Component;


/**
 * java后台极光推送：使用Java SDK
 */
@Component
@SuppressWarnings({ "deprecation", "restriction" })
public class JiguangPush {
    private static final Logger log = LoggerFactory.getLogger(JiguangPush.class);
    private static String masterSecret = "6c70826eeaa1326007bbe728";
    private static String appKey = "94508b31c1b3efabb95dcf69";


    /**
     * 极光推送
     * @param alias 推送用户
     * @param alert 推送内容
     */
    public void jiguangPush(String alias, String alert){
        log.info("对phone:" + alias + "的用户推送信息");
        PushResult result = push(String.valueOf(alias),alert);
        if(result != null && result.isResultOK()){
            log.info("针对phone:" + alias + "的信息推送成功！");
        }else{
            log.info("针对phone:" + alias + "的信息推送失败！");
        }
    }

    /**
     * 极光推送方法(采用java SDK)
     * @param alias 推送用户
     * @param alert 推送内容
     * @return PushResult
     */
    public static PushResult push(String alias,String alert){
        ClientConfig clientConfig = ClientConfig.getInstance();
        //创建JPushClient(极光推送的实例)
        JPushClient jpushClient = new JPushClient(masterSecret, appKey, null, clientConfig);
        //推送的关键,构造一个payload
        PushPayload payload = buildPushObject_android_ios_alias_alert(alias,alert);
        try {
            return jpushClient.sendPush(payload);
        } catch (APIConnectionException e) {
            log.error("Connection error. Should retry later. ", e);
            return null;
        } catch (APIRequestException e) {
            log.error("Error response from JPush server. Should review and fix it. ", e);
            log.info("HTTP Status: " + e.getStatus());
            log.info("Error Code: " + e.getErrorCode());
            log.info("Error Message: " + e.getErrorMessage());
            log.info("Msg ID: " + e.getMsgId());
            return null;
        }
    }

    /**
     * 生成极光推送对象PushPayload（采用java SDK）
     * @param alias 推送用户
     * @param alert 推送内容
     * @return PushPayload
     */
    public static PushPayload buildPushObject_android_ios_alias_alert(String alias,String alert){
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())//指定android和iOS平台的用户
                .setAudience(Audience.alias(alias))//你项目中的用户
                .setNotification(Notification.newBuilder()//发送内容
                        .addPlatformNotification(AndroidNotification.newBuilder()
                                .addExtra("type", "infomation")//附加字段
                                .setAlert(alert)//通知内容
                                .build())
                        .addPlatformNotification(IosNotification.newBuilder()
                                .addExtra("type", "infomation")
                                .setAlert(alert)
                                .build())
                        .build())
                .setOptions(Options.newBuilder()
                        .setApnsProduction(false)//true-推送生产环境 false-推送开发环境（测试使用参数）
                        .setTimeToLive(90)//消息在JPush服务器的失效时间（测试使用参数）
                        .build())
                .build();
    }

}
