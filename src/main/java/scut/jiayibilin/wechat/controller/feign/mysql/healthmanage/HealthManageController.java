package scut.jiayibilin.wechat.controller.feign.mysql.healthmanage;

import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import scut.jiayibilin.wechat.entity.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by jie on 2017/8/30.
 */
@CrossOrigin
@RestController
@RequestMapping(value="/healthmanage")
public class HealthManageController {

    @Autowired
    private JsonResult jsonResult;
    @Autowired
    private   HealthManageClient healthManageClient;
    @Autowired
    WxMpService wxMpService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    /*
    * 2.3 保存或者更新健康检查信息
    * */
    @CrossOrigin(allowCredentials="true", allowedHeaders="*", methods={RequestMethod.GET,
            RequestMethod.POST, RequestMethod.DELETE, RequestMethod.OPTIONS,
            RequestMethod.HEAD, RequestMethod.PUT, RequestMethod.PATCH}, origins="*")
    @RequestMapping(value="/healthcheck/saveorupdate",method = RequestMethod.POST)
    public JsonResult SaveHealthCheck(HttpServletRequest request){
        String result="error";
        String wechat_id=request.getParameter("wechat_id");
        float height=Float.valueOf(request.getParameter("height")).floatValue();
        float weight=Float.valueOf(request.getParameter("weight")).floatValue();
        String diabetes=request.getParameter("diabetes");
        String chd=request.getParameter("chd");
        String stroke=request.getParameter("stroke");
        String hypertension=request.getParameter("hypertension");
        String other_history=request.getParameter("other_history");
        String family_history=request.getParameter("family_history");
        String smoke=request.getParameter("smoke");
        String smoking=request.getParameter("smoking");
        String drink=request.getParameter("drink");
        String drinking=request.getParameter("drinking");
        HealthCheckEntity healthCheckEntity=new HealthCheckEntity(wechat_id,height,weight,diabetes,chd,stroke,hypertension,other_history,family_history,smoke,smoking,drink,drinking);
        try{
        result=healthManageClient.SaveHealthTable(healthCheckEntity);
        }
        catch(Exception e){
            jsonResult.setErrorcode("10020");
            jsonResult.setMessage("there is an exception while saving healthcheck . exception:"+e.getMessage());
            jsonResult.setData(null);
            this.logger.error("保存健康检查信息时出现异常"+e.getMessage());
            return jsonResult;
        }
        if(result.equals("success")){
            jsonResult.setErrorcode("0");
            jsonResult.setMessage("saving healthchecktable success");
            jsonResult.setData(null);
            this.logger.info("成功保存健康检查信息");
            return jsonResult;
        }else{
            jsonResult.setErrorcode("10021");
            jsonResult.setMessage("saving healthchecktable error in database");
            jsonResult.setData(null);
            this.logger.error("保存健康检查信息时数据库服务发生错误");
            return jsonResult;
        }
    }
    /*
    *2.4 保存或者更新生化检查信息
    * */
    @CrossOrigin(allowCredentials="true", allowedHeaders="*", methods={RequestMethod.GET,
            RequestMethod.POST, RequestMethod.DELETE, RequestMethod.OPTIONS,
            RequestMethod.HEAD, RequestMethod.PUT, RequestMethod.PATCH}, origins="*")
    @RequestMapping(value="/biologycheck/saveorupdate",method = RequestMethod.POST)
    public JsonResult SaveBiologyCheck(HttpServletRequest request){
        String result="error";
        String wechat_id=request.getParameter("wechat_id");
        String tch=request.getParameter("tch");
        String fbg=request.getParameter("fbg");
        String tg=request.getParameter("tg");
        String hdl=request.getParameter("hdl");
        String ldl=request.getParameter("ldl");
        String tch_time= request.getParameter("tch_time");
        String fbg_time=request.getParameter("fbg_time");
        String tg_time=request.getParameter("tg_time");
        String hdl_time=request.getParameter("hdl_time");
        String ldl_time=request.getParameter("ldl_time");
        BiologyCheckEntity biologyCheckEntity=new BiologyCheckEntity( wechat_id,  tch,  tch_time,  fbg,  fbg_time,  tg,  tg_time,  hdl,  hdl_time,  ldl,  ldl_time);
        try{
           result=healthManageClient.SaveBiologyTable(biologyCheckEntity);
        }catch(Exception e){
            jsonResult.setErrorcode("10022");
            jsonResult.setMessage("there is an exception while saving biologycheck . exception:"+e.getMessage());
            jsonResult.setData(null);
            this.logger.error("保存生化检查时出现异常"+e.getMessage());
            return jsonResult;
        }
        if(result.equals("success")){
            jsonResult.setErrorcode("0");
            jsonResult.setMessage("saving biologychecktable success");
            jsonResult.setData(null);
            this.logger.info("成功保存生化检查信息");
            return jsonResult;
        }else{
            jsonResult.setErrorcode("10023");
            jsonResult.setMessage("saving biologychecktable error in database");
            jsonResult.setData(null);
            this.logger.error("保存生化检查信息时数据库服务发生错误");
            return jsonResult;
        }
    }

    /*
* 根据患者查找生化检查表
* */
    @CrossOrigin(allowCredentials="true", allowedHeaders="*", methods={RequestMethod.GET,
            RequestMethod.POST, RequestMethod.DELETE, RequestMethod.OPTIONS,
            RequestMethod.HEAD, RequestMethod.PUT, RequestMethod.PATCH}, origins="*")
    @RequestMapping(value="/biologycheck/get",method = RequestMethod.GET)
    public JsonResult GetBiologyCheck(@RequestParam("wechat_id")String wechat_id){
        BiologyCheckEntity result=null;
        try{
            result=healthManageClient.GetBiologyInfo(wechat_id);
        }catch(Exception e){
            jsonResult.setErrorcode("1");
            jsonResult.setMessage("there is an exception while getting biology check.exception:"+e.getMessage());
            this.logger.error("获取生化检查表失败");
            jsonResult.setData(null);
        }
        jsonResult.setData(result);
        jsonResult.setErrorcode("0");
        jsonResult.setMessage("get biology check success");
        this.logger.info("成功获取生化检查表");
        return jsonResult;
    }

    /*
    *2.5 保存血压心率信息
    * */
    @CrossOrigin(allowCredentials="true", allowedHeaders="*", methods={RequestMethod.GET,
            RequestMethod.POST, RequestMethod.DELETE, RequestMethod.OPTIONS,
            RequestMethod.HEAD, RequestMethod.PUT, RequestMethod.PATCH}, origins="*")
    @RequestMapping(value="/bloodcheck/saveorupdate",method = RequestMethod.POST)
    public JsonResult SaveBloodCheck(HttpServletRequest request){
        String result="error";
        String wechat_id=request.getParameter("wechat_id");
        String date=request.getParameter("date");
        String time=request.getParameter("time");
        if(time.contains("晨起")){
            time="morning";
        }else{
            time="evening";
        }
        String systolic_pressure=String.format("%.2f", Double.parseDouble(request.getParameter("systolic_pressure")));
        String diastolic_pressure=String.format("%.2f",Double.parseDouble(request.getParameter("diastolic_pressure")));
        String rhr=String.format("%.2f",Double.parseDouble(request.getParameter("rhr")));
        BloodPressureEntity bloodPressureEntity=new BloodPressureEntity( wechat_id,  date,  time,  systolic_pressure,  diastolic_pressure,  rhr);
        try{
            result=healthManageClient.SaveBloodPressureTable(bloodPressureEntity);
        }catch(Exception e){
            jsonResult.setErrorcode("10024");
            jsonResult.setMessage("there is an exception while saving bloodcheck . exception:"+e.getMessage());
            jsonResult.setData(null);
            this.logger.error("保存血压心率时出现异常"+e.getMessage());
            return jsonResult;
        }
        if(result.equals("success")){
            jsonResult.setErrorcode("0");
            jsonResult.setMessage("saving bloodchecktable success");
            jsonResult.setData(null);
            this.logger.info("成功保存血压心率信息");
            return jsonResult;
        }else{
            jsonResult.setErrorcode("10025");
            jsonResult.setMessage("saving bloodchecktable error in database");
            jsonResult.setData(null);
            this.logger.error("保存血压心率信息时数据库服务发生错误");
            return jsonResult;
        }
    }
    /*
    *2.6 查找血压心率历史记录
    * @Param  timearea:时间区间（一周，一个月）
    * @Param  time:具体时间（晨起，睡前）
    * */
    @CrossOrigin(allowCredentials="true", allowedHeaders="*", methods={RequestMethod.GET,
            RequestMethod.POST, RequestMethod.DELETE, RequestMethod.OPTIONS,
            RequestMethod.HEAD, RequestMethod.PUT, RequestMethod.PATCH}, origins="*")
    @RequestMapping(value="/bloodcheck/get",method = RequestMethod.GET)
    public JsonResult FindBloodHistory(String wechat_id,String timearea,String time){
        List<BloodPressureEntity> bloodPressurelist=null;
        int timeareaint=0;
        if(timearea.equals("week")){
            timeareaint=7;
        }else{
            timeareaint=30;
        }
        try{
            bloodPressurelist=healthManageClient.GetBloodPressureTable(wechat_id,timeareaint,time);
        }catch(Exception e){
            jsonResult.setErrorcode("10026");
            jsonResult.setMessage("there is an exception while getting bloodcheck history . exception:"+e.getMessage());
            jsonResult.setData(null);
            this.logger.error("查找血压心率历史记录时出现异常");
            return jsonResult;
        }
        jsonResult.setErrorcode("0");
        jsonResult.setMessage("get bloodcheck history success");
        jsonResult.setData(bloodPressurelist);
        this.logger.info("成功获取血压心率历史记录");
        return jsonResult;
    }

    /*
    * 2.7 保存心电图
    * */
    @CrossOrigin(allowCredentials="true", allowedHeaders="*", methods={RequestMethod.GET,
            RequestMethod.POST, RequestMethod.DELETE, RequestMethod.OPTIONS,
            RequestMethod.HEAD, RequestMethod.PUT, RequestMethod.PATCH}, origins="*")
    @RequestMapping(value="/cardiogram/save",method = RequestMethod.POST)
    public JsonResult SaveCardiogram(HttpServletRequest request){
        String result="error";
        String wechat_id=request.getParameter("wechat_id");
        String cardiogram=request.getParameter("cardiogram");
        String date=request.getParameter("date");
        String remark=request.getParameter("remark");
        CardiogramEntity cardiogramEntity=new CardiogramEntity(wechat_id, cardiogram, date, remark);
        try{
            result=healthManageClient.SaveCardiogramTable(cardiogramEntity);
        }catch(Exception e){
            jsonResult.setErrorcode("10027");
            jsonResult.setMessage("there is an exception while saving cardiogram . exception:"+e.getMessage());
            jsonResult.setData(null);
            this.logger.error("保存心电表时出现异常");
            return jsonResult;
        }
        if(result.equals("success")){
            jsonResult.setErrorcode("0");
            jsonResult.setMessage("saving cardiogramtable success");
            jsonResult.setData(null);
            this.logger.info("成功保存心电表信息");
            return jsonResult;
        }else{
            jsonResult.setErrorcode("10028");
            jsonResult.setMessage("saving cardiogramktable error in database");
            jsonResult.setData(null);
            this.logger.error("保存心电表时数据库服务发生错误");
            return jsonResult;
        }
    }

    /*
* 根据患者查找心电图
* */
    @CrossOrigin(allowCredentials="true", allowedHeaders="*", methods={RequestMethod.GET,
            RequestMethod.POST, RequestMethod.DELETE, RequestMethod.OPTIONS,
            RequestMethod.HEAD, RequestMethod.PUT, RequestMethod.PATCH}, origins="*")
    @RequestMapping(value="/cardiograph/get",method = RequestMethod.GET)
    public JsonResult GetCardiograph(@RequestParam("wechat_id")String wechat_id){
        List<CardiogramEntity> result=null;
        try{
            result=healthManageClient.FindCardiograph(wechat_id);
            jsonResult.setData(result);
            jsonResult.setErrorcode("0");
            jsonResult.setMessage("get cardiograph success");
            this.logger.info("成功获取心电图");

        }catch(Exception e){
            jsonResult.setErrorcode("1");
            jsonResult.setMessage("there is an exception while getting cardiograph.exception:"+e.getMessage());
            this.logger.error("获取心电图失败");
            jsonResult.setData(null);
        }
        return jsonResult;
    }

    /*
    *  删除心电图
    * */
    @CrossOrigin(allowCredentials="true", allowedHeaders="*", methods={RequestMethod.GET,
            RequestMethod.POST, RequestMethod.DELETE, RequestMethod.OPTIONS,
            RequestMethod.HEAD, RequestMethod.PUT, RequestMethod.PATCH}, origins="*")
    @RequestMapping(value="/cardiograph/delete",method = RequestMethod.POST)
    public JsonResult deleteCardiogram(@RequestParam("id") Integer id){
        String result="error";
        try{
            result = healthManageClient.deleteCardiogram(id);
        }catch(Exception e){
            jsonResult.setErrorcode("1");
            jsonResult.setMessage("there is an exception while saving cardiogram . exception:"+e.getMessage());
            jsonResult.setData(null);
            this.logger.error("保存心电表时出现异常");
            return jsonResult;
        }
        if(result.equals("success")){
            jsonResult.setErrorcode("0");
            jsonResult.setMessage("delete cardiogram success");
            jsonResult.setData(null);
            this.logger.info("成功删除心电图信息");
            return jsonResult;
        }else{
            jsonResult.setErrorcode("1");
            jsonResult.setMessage("delete cardiogram error in database");
            jsonResult.setData(null);
            this.logger.error("删除心电图时数据库服务发生错误");
            return jsonResult;
        }
    }


    /*
    *2.8 生成风险评估报告
    * */
    @CrossOrigin(allowCredentials="true", allowedHeaders="*", methods={RequestMethod.GET,
            RequestMethod.POST, RequestMethod.DELETE, RequestMethod.OPTIONS,
            RequestMethod.HEAD, RequestMethod.PUT, RequestMethod.PATCH}, origins="*")
    @RequestMapping(value="/report/generate",method = RequestMethod.POST)
    public JsonResult GenerateReport(@RequestParam("wechat_id") String wechat_id){
        String result="error";
        try {
            result=healthManageClient.GenerateReport(wechat_id);
        }catch(Exception e){
            jsonResult.setErrorcode("1");
            jsonResult.setMessage("there is an exception while generating report. exception:"+e.getMessage());
            jsonResult.setData(null);
        }
        if(result.equals("success")){
            jsonResult.setErrorcode("0");
            jsonResult.setMessage("genereta report success");
            jsonResult.setData(null);
            this.logger.info("成功生成风险评估报告");
        }else if(result.equals("time")){
            jsonResult.setErrorcode("2");
            jsonResult.setMessage("genereta report error");
            jsonResult.setData(null);
        }else if(result.equals("blood")){
            jsonResult.setErrorcode("3");
            jsonResult.setMessage("genereta report error");
            jsonResult.setData(null);
        }else if(result.equals("age")){
            jsonResult.setErrorcode("4");
            jsonResult.setMessage("genereta report error");
            jsonResult.setData(null);
        }else if(result.equals("health")){
            jsonResult.setErrorcode("5");
            jsonResult.setMessage("genereta report error");
            jsonResult.setData(null);
        } else if(result.equals("biology")){
            jsonResult.setErrorcode("6");
            jsonResult.setMessage("genereta report error");
            jsonResult.setData(null);
        } else{
            jsonResult.setErrorcode("1");
            jsonResult.setMessage("there is an exception while generating report. ");
            jsonResult.setData(null);
        }
        return jsonResult;
    }

    /*
    *2.9 查找所有的风险评估报告
    * */
    @CrossOrigin(allowCredentials="true", allowedHeaders="*", methods={RequestMethod.GET,
            RequestMethod.POST, RequestMethod.DELETE, RequestMethod.OPTIONS,
            RequestMethod.HEAD, RequestMethod.PUT, RequestMethod.PATCH}, origins="*")
    @RequestMapping(value="/report/getall",method = RequestMethod.GET)
    public JsonResult GetAllReport(@RequestParam("wechat_id")String wechat_id){
        List<RiskReportEntity> result=null;
        try{
            result=healthManageClient.GetAllReport(wechat_id);
        }catch(Exception e){
            jsonResult.setErrorcode("1");
            jsonResult.setMessage("there is an exception while get all report.exception:"+e.getMessage());
            jsonResult.setData(null);
        }
            jsonResult.setData(result);
            jsonResult.setErrorcode("0");
            jsonResult.setMessage("get all riskreport success");
        return jsonResult;
    }

    /*
    *3.0 查找最新的风险评估报告
    * */
    @CrossOrigin(allowCredentials="true", allowedHeaders="*", methods={RequestMethod.GET,
            RequestMethod.POST, RequestMethod.DELETE, RequestMethod.OPTIONS,
            RequestMethod.HEAD, RequestMethod.PUT, RequestMethod.PATCH}, origins="*")
    @RequestMapping(value="/report/getnewest",method = RequestMethod.GET)
    public JsonResult GetNewestReport(@RequestParam("wechat_id")String wechat_id){
        try{
            RiskReportEntity riskReportEntity=healthManageClient.GetNewestReport(wechat_id);
            if(riskReportEntity!=null){
                jsonResult.setErrorcode("0");
                jsonResult.setMessage("get newest riskreport success");
                jsonResult.setData(riskReportEntity);
            }else{
                jsonResult.setErrorcode("1");
                jsonResult.setMessage("get newest riskreport error");
                jsonResult.setData(null);
            }
        }catch (Exception e){
            jsonResult.setData(null);
            jsonResult.setErrorcode("1");
            jsonResult.setMessage("there is an exception while getnewest report exception:"+e.getMessage());
        }
        return jsonResult;
    }

    /*
    * 3.1 查找已经进行过风险评估的总人数
    * */
    @CrossOrigin(allowCredentials="true", allowedHeaders="*", methods={RequestMethod.GET,
            RequestMethod.POST, RequestMethod.DELETE, RequestMethod.OPTIONS,
            RequestMethod.HEAD, RequestMethod.PUT, RequestMethod.PATCH}, origins="*")
    @RequestMapping(value="/report/getnumber",method = RequestMethod.GET)
    public JsonResult GetReportNumber(){
        int result=0;
        try{
            result=healthManageClient.GetReportNumber();
            jsonResult.setErrorcode("0");
            jsonResult.setMessage("get report people number success");
            jsonResult.setData(result);
        }catch (Exception e){
            jsonResult.setErrorcode("1");
            jsonResult.setMessage("there is an exception while get report people number.exception:"
            +e.getMessage());
            jsonResult.setData(0);
        }
        return jsonResult;
    }

    /*
    * 3.2 获取具体患者的未读消息
    * */
    @CrossOrigin(allowCredentials="true", allowedHeaders="*", methods={RequestMethod.GET,
            RequestMethod.POST, RequestMethod.DELETE, RequestMethod.OPTIONS,
            RequestMethod.HEAD, RequestMethod.PUT, RequestMethod.PATCH}, origins="*")
    @RequestMapping(value="/messageremind/getunread",method = RequestMethod.GET)
    public JsonResult GetMessage(@RequestParam("wechat_id") String wechat_id){
        try{
        List<MessageListEntity> list=healthManageClient.GetMessageUnread(wechat_id);
        jsonResult.setData(list);
        jsonResult.setMessage("get message unread success");
        jsonResult.setErrorcode("0");
        this.logger.info("成功获取患者未读消息");
        }catch (Exception e){
            jsonResult.setData(null);
            jsonResult.setMessage("get message unread error");
            jsonResult.setErrorcode("1");
            this.logger.error("获取患者未读消息失败");
        }
        return jsonResult;
    }

    /*
* 获取具体患者的未读消息
* */
    @CrossOrigin(allowCredentials="true", allowedHeaders="*", methods={RequestMethod.GET,
            RequestMethod.POST, RequestMethod.DELETE, RequestMethod.OPTIONS,
            RequestMethod.HEAD, RequestMethod.PUT, RequestMethod.PATCH}, origins="*")
    @RequestMapping(value="/definemessage/get",method = RequestMethod.GET)
    public JsonResult getDefineMsg(@RequestParam("wechat_id") String wechat_id){
        try{
            List<DefinitionMessageEntity> list=healthManageClient.getDefineMsg(wechat_id);
            jsonResult.setData(list);
            jsonResult.setMessage("get define message success");
            jsonResult.setErrorcode("0");
            this.logger.info("成功获取患者自定义消息");
        }catch (Exception e){
            jsonResult.setData(null);
            jsonResult.setMessage("get define message error");
            jsonResult.setErrorcode("1");
            this.logger.error("获取患者自定义消息失败");
        }
        return jsonResult;
    }

    /*
    * 3.3 将具体的消息设置为已读
    * */
    @CrossOrigin(allowCredentials="true", allowedHeaders="*", methods={RequestMethod.GET,
            RequestMethod.POST, RequestMethod.DELETE, RequestMethod.OPTIONS,
            RequestMethod.HEAD, RequestMethod.PUT, RequestMethod.PATCH}, origins="*")
    @RequestMapping(value="/messageremind/setread",method = RequestMethod.POST)
    public JsonResult setMessageReaded(@RequestParam("id") int id){
        String result="error";
//        int message_id=Integer.valueOf(id);
        try{
            result=healthManageClient.SetMessageRead(id);
        }catch(Exception e){
            jsonResult.setErrorcode("1");
            jsonResult.setMessage("there is an exception while setting message read . exception:"+e.getMessage());
            jsonResult.setData(null);
        }
        if(result.equals("success")){
            jsonResult.setErrorcode("0");
            jsonResult.setMessage("setting message read success");
            jsonResult.setData(null);
            this.logger.info("成功设置消息为已读");
            return jsonResult;
        }else{
            jsonResult.setErrorcode("1");
            jsonResult.setMessage("setting message read error in database");
            jsonResult.setData(null);
            this.logger.error("设置消息为已读时数据库服务发生错误");
            return jsonResult;
        }
    }
    /*
    * 将具体的自定义消息设置为已读
    * */
    @CrossOrigin(allowCredentials="true", allowedHeaders="*", methods={RequestMethod.GET,
            RequestMethod.POST, RequestMethod.DELETE, RequestMethod.OPTIONS,
            RequestMethod.HEAD, RequestMethod.PUT, RequestMethod.PATCH}, origins="*")
    @RequestMapping(value="/definemessage/setread",method = RequestMethod.POST)
    public JsonResult setDefineMsgReaded(@RequestParam("id") int id){
        String result="error";
//        int message_id=Integer.valueOf(id);
        try{
            result=healthManageClient.setDefineMsgRead(id);
        }catch(Exception e){
            jsonResult.setErrorcode("1");
            jsonResult.setMessage("there is an exception while setting define message read . exception:"+e.getMessage());
            jsonResult.setData(null);
        }
        if(result.equals("success")){
            jsonResult.setErrorcode("0");
            jsonResult.setMessage("setting defien message read success");
            jsonResult.setData(null);
            this.logger.info("成功设置自定义消息为已读");
            return jsonResult;
        }else{
            jsonResult.setErrorcode("1");
            jsonResult.setMessage("setting define message read error in database");
            jsonResult.setData(null);
            this.logger.error("设置自定义消息为已读时数据库服务发生错误");
            return jsonResult;
        }
    }
    /*
* 获取具体患者的未读模板消息数量
* */
    @CrossOrigin(allowCredentials="true", allowedHeaders="*", methods={RequestMethod.GET,
            RequestMethod.POST, RequestMethod.DELETE, RequestMethod.OPTIONS,
            RequestMethod.HEAD, RequestMethod.PUT, RequestMethod.PATCH}, origins="*")
    @RequestMapping(value="/messageremind/unread/getnumber",method = RequestMethod.GET)
    public JsonResult GetMessageNumber(@RequestParam("wechat_id") String wechat_id){
        try{
            int result=healthManageClient.GetMessageUnreadNumber(wechat_id);
            jsonResult.setData(result);
            jsonResult.setMessage("get message unread number success");
            jsonResult.setErrorcode("0");
            this.logger.info("成功获取患者未读消息数量");
        }catch (Exception e){
            jsonResult.setData(0);
            jsonResult.setMessage("get message unread number error");
            jsonResult.setErrorcode("1");
            this.logger.error("获取患者未读消息数量失败");
        }
        return jsonResult;
    }
    /*
* 获取具体患者的未读自定义消息数量
* */
    @CrossOrigin(allowCredentials="true", allowedHeaders="*", methods={RequestMethod.GET,
            RequestMethod.POST, RequestMethod.DELETE, RequestMethod.OPTIONS,
            RequestMethod.HEAD, RequestMethod.PUT, RequestMethod.PATCH}, origins="*")
    @RequestMapping(value="/definemessage/unread/getnumber",method = RequestMethod.GET)
    public JsonResult getDefineMsgNumber(@RequestParam("wechat_id") String wechat_id){
        try{
            int result=healthManageClient.getDefineMsgUnreadNumber(wechat_id);
            jsonResult.setData(result);
            jsonResult.setMessage("get define message unread number success");
            jsonResult.setErrorcode("0");
            this.logger.info("成功获取患者未读自定义消息数量");
        }catch (Exception e){
            jsonResult.setData(0);
            jsonResult.setMessage("get define message unread number error");
            jsonResult.setErrorcode("1");
            this.logger.error("获取患者未读自定义消息数量失败");
        }
        return jsonResult;
    }

    /*
    * 3.4 查看健康信息表
    * */
    @CrossOrigin(allowCredentials="true", allowedHeaders="*", methods={RequestMethod.GET,
            RequestMethod.POST, RequestMethod.DELETE, RequestMethod.OPTIONS,
            RequestMethod.HEAD, RequestMethod.PUT, RequestMethod.PATCH}, origins="*")
    @RequestMapping(value="/healthcheck/get",method = RequestMethod.GET)
    public JsonResult GetHealthCheckTable(String wechat_id){
        HealthCheckEntity healthCheckEntity=null;
        try{
            healthCheckEntity=healthManageClient.GetHealthTable(wechat_id);
        }catch(Exception e){
            jsonResult.setErrorcode("1");
            jsonResult.setMessage("there is an exception while getting healthchecktable . exception:"+e.getMessage());
            jsonResult.setData(null);
            this.logger.error("查找患者健康信息时出现异常");
            return jsonResult;
        }
        jsonResult.setErrorcode("0");
        jsonResult.setMessage("get healthchecktable success");
        jsonResult.setData(healthCheckEntity);
        this.logger.info("成功获取患者健康信息");
        return jsonResult;
    }

    /*
    *3.5 查找血压心率历史记录
    * */
    @CrossOrigin(allowCredentials="true", allowedHeaders="*", methods={RequestMethod.GET,
            RequestMethod.POST, RequestMethod.DELETE, RequestMethod.OPTIONS,
            RequestMethod.HEAD, RequestMethod.PUT, RequestMethod.PATCH}, origins="*")
    @RequestMapping(value="/bloodcheck/find",method = RequestMethod.GET)
    public JsonResult Findblood(String wechat_id){
        String result="nothing";
        try{
            result=healthManageClient.Findbloodhistory(wechat_id);
        }catch(Exception e){
            jsonResult.setErrorcode("10026");
            jsonResult.setMessage("there is an exception while finding bloodcheck history . exception:"+e.getMessage());
            jsonResult.setData(result);
            this.logger.error("查找患者是否有血压心率历史记录时出现异常");
            return jsonResult;
        }
        jsonResult.setErrorcode("0");
        jsonResult.setMessage("get bloodcheck history success");
        jsonResult.setData(result);
        this.logger.info("成功查询患者是否有血压心率历史");
        return jsonResult;
    }

    /*
    * 3.6 通过次数查找风险评估
    * */
    @CrossOrigin(allowCredentials="true", allowedHeaders="*", methods={RequestMethod.GET,
            RequestMethod.POST, RequestMethod.DELETE, RequestMethod.OPTIONS,
            RequestMethod.HEAD, RequestMethod.PUT, RequestMethod.PATCH}, origins="*")
    @RequestMapping(value="/report/getbycount",method = RequestMethod.GET)
    public JsonResult GetReportBycount(String wechat_id,int count){
        try{
            RiskReportEntity riskReportEntity=healthManageClient.GetByCountReport(wechat_id,count);
            jsonResult.setErrorcode("0");
            jsonResult.setMessage("get report by count success");
            jsonResult.setData(riskReportEntity);
        }catch (Exception e){
            jsonResult.setErrorcode("1");
            jsonResult.setMessage("there is an exception while get report by count.exception:"
                    +e.getMessage());
            jsonResult.setData(0);
        }
        return jsonResult;
    }



    /*
    * 根据模板消息id获取消息
    * */
    @CrossOrigin(allowCredentials="true", allowedHeaders="*", methods={RequestMethod.GET,
            RequestMethod.POST, RequestMethod.DELETE, RequestMethod.OPTIONS,
            RequestMethod.HEAD, RequestMethod.PUT, RequestMethod.PATCH}, origins="*")
    @RequestMapping(value="/messageremind/getbyid",method = RequestMethod.GET)
    public JsonResult GetMessagebyid(@RequestParam("id") int id){
        try{
            MessageRemindEntity result=healthManageClient.MessageGetById(id);
            jsonResult.setData(result);
            jsonResult.setMessage("get message by message_id success");
            jsonResult.setErrorcode("0");
            this.logger.info("成功获取消息内容");
        }catch (Exception e){
            jsonResult.setData(null);
            jsonResult.setMessage("get message by message_id error");
            jsonResult.setErrorcode("1");
            this.logger.error("获取消息内容失败");
        }
        return jsonResult;
    }
    /*
    * 根据自定义消息id获取消息
    * */
    @CrossOrigin(allowCredentials="true", allowedHeaders="*", methods={RequestMethod.GET,
            RequestMethod.POST, RequestMethod.DELETE, RequestMethod.OPTIONS,
            RequestMethod.HEAD, RequestMethod.PUT, RequestMethod.PATCH}, origins="*")
    @RequestMapping(value="/definemessage/getbyid",method = RequestMethod.GET)
    public JsonResult getDefineMsgById(@RequestParam("id") int id){
        try{
            DefinitionMessageEntity result=healthManageClient.getDefineMsgById(id);
            jsonResult.setData(result);
            jsonResult.setMessage("get define message by message_id success");
            jsonResult.setErrorcode("0");
            this.logger.info("成功获取自定义消息内容");
        }catch (Exception e){
            jsonResult.setData(null);
            jsonResult.setMessage("get define message by message_id error");
            jsonResult.setErrorcode("1");
            this.logger.error("获取自定义消息内容失败");
        }
        return jsonResult;
    }

    /*
  *删除患者未读医生群发消息
   */
    @CrossOrigin(allowCredentials="true", allowedHeaders="*", methods={RequestMethod.GET,
            RequestMethod.POST, RequestMethod.DELETE, RequestMethod.OPTIONS,
            RequestMethod.HEAD, RequestMethod.PUT, RequestMethod.PATCH}, origins="*")
    @RequestMapping(value="/definemessage/delete",method = RequestMethod.POST)
    public JsonResult deleteDefineMsg(@RequestParam int id){
        try{
            String result = healthManageClient.deleteDefineMsgById(id);
            if(result.equals("success")){
                jsonResult.setErrorcode("0");
                jsonResult.setMessage("delete define message  success");
                jsonResult.setData(null);
                this.logger.info("成功删除患者的医生自定义消息");
            }else if(result.equals("error")){
                jsonResult.setErrorcode("1");
                jsonResult.setMessage("delete define message  error");
                jsonResult.setData(null);
                this.logger.info("删除患者的医生自定义消息失败,数据库异常");
            }

        }catch(Exception e){
            this.logger.error("删除患者的医生群发消息时发生异常"+e.getMessage());
            jsonResult.setErrorcode("10001");
            jsonResult.setMessage("there is an exception while  deleting doctor group message:"+e.getMessage());
            jsonResult.setData(null);
        }
        return jsonResult;
    }
    /*
 *医生发送模板消息
  */
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.GET,
            RequestMethod.POST, RequestMethod.DELETE, RequestMethod.OPTIONS,
            RequestMethod.HEAD, RequestMethod.PUT, RequestMethod.PATCH}, origins = "*")
    @RequestMapping(value = "/messageremind", method = RequestMethod.POST)
    public JsonResult messageRemind(@RequestBody MessageRemindEntity messageRemindEntity) {
        try {
            String result = healthManageClient.saveMessageRemind(messageRemindEntity);
            //微信后台推送
//            WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder().toUser(messageRemindEntity.getWechat_id()).templateId("lQrnQc4HKp-UUACD8ZvXdo7lWlh0cnokiWtFe9g2PuU").build();
//            templateMessage.getData().add(new WxMpTemplateData("商品编码", messageRemindEntity.getTarget(), "#000000"));
//            templateMessage.getData().add(new WxMpTemplateData("商品名称", messageRemindEntity.getRemark(), "#000000"));
//            wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
            if (result.equals("success")) {
                jsonResult.setErrorcode("0");
                jsonResult.setMessage("send messageremind success");
                jsonResult.setData(null);
            } else {
                jsonResult.setErrorcode("10001");
                jsonResult.setMessage("there is an error in mysql while sending message remind .");
                jsonResult.setData(null);
            }
        } catch (Exception e) {
            this.logger.error("医生发送模板消息时异常" + e.getMessage());
            jsonResult.setErrorcode("10001");
            jsonResult.setMessage("there is an exception while sending message remind . exception:" + e.getMessage());
            jsonResult.setData(null);
        }
        return jsonResult;
    }



    /*
*医生发送自定义消息
*/
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.GET,
            RequestMethod.POST, RequestMethod.DELETE, RequestMethod.OPTIONS,
            RequestMethod.HEAD, RequestMethod.PUT, RequestMethod.PATCH}, origins = "*")
    @RequestMapping(value = "/definemessage", method = RequestMethod.POST)
    public JsonResult defineMessage(@RequestBody DefinitionMessageEntity definitionMessageEntity) {
        try {
            String result = healthManageClient.defineMessage(definitionMessageEntity);
            //微信后台推送
//            WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder().toUser(definitionMessageEntity.getWechat_id()).templateId("lQrnQc4HKp-UUACD8ZvXdo7lWlh0cnokiWtFe9g2PuU").build();
//            templateMessage.getData().add(new WxMpTemplateData("keyword1", definitionMessageEntity.getTitle(), "#000000"));
//            templateMessage.getData().add(new WxMpTemplateData("keyword2", definitionMessageEntity.getContent(), "#000000"));
//            wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
            if (result.equals("success")) {
                jsonResult.setErrorcode("0");
                jsonResult.setMessage("send define message success");
                jsonResult.setData(null);
            } else {
                jsonResult.setErrorcode("10001");
                jsonResult.setMessage("there is an error in mysql while sending  define message .");
                jsonResult.setData(null);
            }
        } catch (Exception e) {
            this.logger.error("医生发送自定义消息时异常" + e.getMessage());
            jsonResult.setErrorcode("10001");
            jsonResult.setMessage("there is an exception while sending define message . exception:" + e.getMessage());
            jsonResult.setData(null);
        }
        return jsonResult;
    }

}
