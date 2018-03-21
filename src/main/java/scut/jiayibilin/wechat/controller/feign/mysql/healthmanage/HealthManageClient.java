package scut.jiayibilin.wechat.controller.feign.mysql.healthmanage;

import org.springframework.web.bind.annotation.RequestBody;
import scut.jiayibilin.wechat.entity.*;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.List;

/**
 * Created by jie on 2017/8/30.
 */
@FeignClient(name="eureka-server-mysql",url="http://mrxiej.ngrok.wendal.cn/api-mysql")
public interface HealthManageClient {
    /*
    * 调用远程服务下的保存健康信息表接口
    * */
    @RequestMapping(value="/healthmanage/savehealthtable" ,method= RequestMethod.POST)
    String SaveHealthTable(HealthCheckEntity healCheckEntity);
    /*
    * 调用远程服务下的查看健康信息表接口
    * */
    @RequestMapping(value="/healthmanage/gethealthtable" ,method= RequestMethod.GET)
    HealthCheckEntity GetHealthTable(@RequestParam("wechat_id")String wechat_id);

    /*
    * 调用远程服务下的保存生化检查表接口
    * */
    @RequestMapping(value="/healthmanage/savebiologytable" ,method= RequestMethod.POST)
    String SaveBiologyTable(BiologyCheckEntity biologyCheckEntity);

    /*
    * 调用远程服务下的查看生化检查表接口
    * */
    @RequestMapping(value = "/healthmanage/getbiologyinfo", method = RequestMethod.GET)
    BiologyCheckEntity GetBiologyInfo(@RequestParam("wechat_id") String wechat_id);

    /*
    * 调用远程服务下的保存血压心率表接口
    * */
    @RequestMapping(value="/healthmanage/savebloodpressuretable" ,method= RequestMethod.POST)
    String SaveBloodPressureTable(BloodPressureEntity bloodPressureEntity);

    /*
    * 调用远程服务下的查找血压心率记录接口
    * @Param  timearea:时间区间（一周，一个月）
    * @Param  time:具体时间（晨起，睡前）
    * */
    @RequestMapping(value="/healthmanage/getbloodpressuretable" ,method= RequestMethod.GET)
    List<BloodPressureEntity> GetBloodPressureTable(@RequestParam("wechat_id")String wechat_id,@RequestParam("timearea") int timearea, @RequestParam("time") String time);

    /*
    * 调用远程服务下的保存心电图接口
    * */
    @RequestMapping(value="/healthmanage/savecardiogramtable" ,method= RequestMethod.POST)
    String SaveCardiogramTable(CardiogramEntity cardiogramEntity);

    /*
* 调用远程服务下的查找心电图接口
* */
    @RequestMapping(value="/healthmanage/findcardiogramtable" ,method= RequestMethod.GET)
    List<CardiogramEntity> FindCardiograph(@RequestParam("wechat_id") String wechat_id);

    /*
    *调用远程服务下的删除心电图接口
     */
    @RequestMapping(value = "/healthmanage/deletecardiogram", method = RequestMethod.POST)
    String deleteCardiogram(@RequestParam("id") int id);
    /*
    * 调用远程服务下的生成风险评估报告接口
    * */
    @RequestMapping(value="/healthmanage/report/generate" ,method= RequestMethod.POST)
    String GenerateReport(String wechat_id);

    /*
    * 调用远程服务下的查找所有风险评估报告接口
    * */
    @RequestMapping(value="/healthmanage/report/getall" ,method= RequestMethod.GET)
    List<RiskReportEntity> GetAllReport(String wechat_id);

    /*
    * 调用远程服务下的查找最新的风险评估报告接口
    * */
    @RequestMapping(value="/healthmanage/getnewestreport" ,method= RequestMethod.GET)
    RiskReportEntity GetNewestReport(String wechat_id);

    /*
    * 调用远程服务下的根据服务次数查找风险评估报告接口
    * */
    @RequestMapping(value="/healthmanage/report/getbycount" ,method= RequestMethod.GET)
    RiskReportEntity GetByCountReport(@RequestParam("wechat_id") String wechat_id,@RequestParam("count") int count);

    /*
    * 调用远程服务下的查找风险评估人数接口
    * */
    @RequestMapping(value="/healthmanage/getreportnumber" ,method= RequestMethod.GET)
    Integer GetReportNumber();

    /*
    * 调用远程服务下的查找具体患者未读消息接口
    * */
    @RequestMapping(value="/healthmanage/message/getunread" ,method= RequestMethod.GET)
    List<MessageListEntity> GetMessageUnread(@RequestParam("wechat_id") String wechat_id);

    /*
* 调用远程服务下的查找具体患者自定义消息接口
* */
    @RequestMapping(value="/healthmanage/definemessage/get" ,method= RequestMethod.GET)
    List<DefinitionMessageEntity> getDefineMsg(@RequestParam("wechat_id") String wechat_id);
    /*
    * 调用远程服务下的查找具体患者未读模板消息数量接口
    * */
    @RequestMapping(value="/healthmanage/message/unread/getnumber" ,method= RequestMethod.GET)
    int GetMessageUnreadNumber(@RequestParam("wechat_id") String wechat_id);
    /*
    * 调用远程服务下的查找具体患者未读自定义消息数量接口
    * */
    @RequestMapping(value="/healthmanage/definemessage/unread/getnumber" ,method= RequestMethod.GET)
    int getDefineMsgUnreadNumber(@RequestParam("wechat_id") String wechat_id);
    /*
    * 调用远程服务下的将具体消息设置为已读接口
    * */
    @RequestMapping(value="/healthmanage/message/setread" ,method= RequestMethod.POST)
    String SetMessageRead(@RequestParam("id") int id);
    /*
    * 调用远程服务下的将自定义消息设置为已读接口
    * */
    @RequestMapping(value="/healthmanage/definemessage/setread" ,method= RequestMethod.POST)
    String setDefineMsgRead(@RequestParam("id") int id);
    /*
    * 调用远程服务下的查看患者是否有血压心率记录接口
    * return 如果有返回evening 或者 morning 否则 返回nothing
    * */
    @RequestMapping(value="/healthmanage/bloodpressuretable/find" ,method= RequestMethod.GET)
    String Findbloodhistory(@RequestParam("wechat_id") String wechat_id);

    /*
    * 定时器定时生成消息
    * */
    @RequestMapping(value="/healthmanage/message/generate" ,method= RequestMethod.POST)
    void MessageGenerate();

    /*
    * 根据模板消息id获取消息内容
    * */
    @RequestMapping(value="/healthmanage/message/getbyid" ,method= RequestMethod.GET)
    MessageRemindEntity MessageGetById(@RequestParam("id") int id);
    /*
    * 根据自定义消息id获取消息内容
    * */
    @RequestMapping(value="/healthmanage/definemessage/getbyid" ,method= RequestMethod.GET)
    DefinitionMessageEntity getDefineMsgById(@RequestParam("id") int id);
    /*
* 根据自定义消息id删除消息内容
* */
    @RequestMapping(value="/healthmanage/definemessage/delete" ,method= RequestMethod.GET)
    String deleteDefineMsgById(@RequestParam("id") int id);
        /*
    *医生发送模板消息
     */
    @RequestMapping(value = "/healthmanage/savemessageremind", method = RequestMethod.POST)
    String saveMessageRemind(@RequestBody MessageRemindEntity messageRemindEntity);

    @RequestMapping(value = "/healthmanage/definemessage",method = RequestMethod.POST)
    String defineMessage(@RequestBody DefinitionMessageEntity definitionMessageEntity);
}
