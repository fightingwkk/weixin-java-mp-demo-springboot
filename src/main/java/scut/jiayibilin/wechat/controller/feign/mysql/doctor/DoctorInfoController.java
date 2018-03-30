package scut.jiayibilin.wechat.controller.feign.mysql.doctor;

import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import scut.jiayibilin.wechat.controller.feign.mysql.healthmanage.HealthManageClient;
import scut.jiayibilin.wechat.controller.feign.mysql.patient.PatientClient;
import scut.jiayibilin.wechat.controller.wechat.WxMpTemplateMsgController;
import scut.jiayibilin.wechat.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by jie on 2017/8/18.
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/doctor")
public class DoctorInfoController {
    @Autowired
    private DoctorClient doctorClient;
    @Autowired
    WxMpService wxMpService;
    @Autowired
    private JsonResult jsonResult;

    /*打印日志*/
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /*
    * 1.8根据医生手机号码获取医生实体类
    * */
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.GET,
            RequestMethod.POST, RequestMethod.DELETE, RequestMethod.OPTIONS,
            RequestMethod.HEAD, RequestMethod.PUT, RequestMethod.PATCH}, origins = "*")
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public JsonResult GetDoctor(@RequestParam("phone") String phone) {
        DoctorEntity doctorEntity = null;
        try {
            doctorEntity = doctorClient.findbydoctorphone(phone);
            if (doctorEntity == null) {
                jsonResult.setErrorcode("10005");
                jsonResult.setMessage("doctor don't exist");
                jsonResult.setData(null);
                this.logger.info("根据手机号码获取医生信息时，所查询的医生不存在");
            } else {
                jsonResult.setErrorcode("0");
                jsonResult.setMessage("get patientinfo success");
                jsonResult.setData(doctorEntity);
                this.logger.info("成功根据手机号码获取医生信息");
            }
        } catch (Exception e) {
            jsonResult.setErrorcode("10007");
            jsonResult.setMessage("there is an exception while getting doctorinfo by phone\n" + e.getMessage());
            jsonResult.setData(null);
            this.logger.error("获取医生信息时发生异常", e.getMessage());
        }
        return jsonResult;
    }

    /*
    * 1.9获取医生列表
    * */
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.GET,
            RequestMethod.POST, RequestMethod.DELETE, RequestMethod.OPTIONS,
            RequestMethod.HEAD, RequestMethod.PUT, RequestMethod.PATCH}, origins = "*")
    @RequestMapping(value = "/getlist", method = RequestMethod.GET)
    public JsonResult GetDoctorList() {
        List<DoctorEntity> doctorlist = null;
        try {
            doctorlist = doctorClient.findalldoctor();
            jsonResult.setErrorcode("0");
            jsonResult.setMessage("get doctorlist success");
            jsonResult.setData(doctorlist);
            this.logger.info("成功获取医生列表信息");
        } catch (Exception e) {
            jsonResult.setErrorcode("10008");
            jsonResult.setMessage("there is an exception while getting doctorlist . exception:" + e.getMessage());
            jsonResult.setData(null);
            this.logger.error("获取医生列表时发生异常", e.getMessage());
        }
        return jsonResult;
    }

    /*
    * 2.0获取平台提供的所有服务包
    * */
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.GET,
            RequestMethod.POST, RequestMethod.DELETE, RequestMethod.OPTIONS,
            RequestMethod.HEAD, RequestMethod.PUT, RequestMethod.PATCH}, origins = "*")
    @RequestMapping(value = "/service/get", method = RequestMethod.GET)
    public JsonResult GetService(@RequestParam("phone") String phone) {
        List<DoctorServiceEntity> list = null;
        try {
            list = doctorClient.findDoctorService(phone);
            jsonResult.setErrorcode("0");
            jsonResult.setMessage("get servicelist success");
            jsonResult.setData(list);
            this.logger.info("成功获取所有服务");
        } catch (Exception e) {
            jsonResult.setErrorcode("10009");
            jsonResult.setMessage("there is an exception while getting servicelist . exception:" + e.getMessage());
            jsonResult.setData(null);
            this.logger.error("获取服务失败" + e.getMessage());
        }
        return jsonResult;
    }

    /*
    *医生群发消息到公众号
     */
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.GET,
            RequestMethod.POST, RequestMethod.DELETE, RequestMethod.OPTIONS,
            RequestMethod.HEAD, RequestMethod.PUT, RequestMethod.PATCH}, origins = "*")
    @RequestMapping(value = "/groupsending", method = RequestMethod.POST)
    public JsonResult doctorGroupSending(@RequestBody DoctorGroupSendingEntity doctorGroupSendingEntity) {
        try {
            List<PatientGroupReceivingEntity> patientGroupReceivingEntityList = doctorClient.doctorGroupSending(doctorGroupSendingEntity);
            //微信后台发送模板消息
//            for (PatientGroupReceivingEntity pgre : patientGroupReceivingEntityList) {
//                WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder().toUser(pgre.getWechat_id()).templateId("lQrnQc4HKp-UUACD8ZvXdo7lWlh0cnokiWtFe9g2PuU").build();
//                templateMessage.getData().add(new WxMpTemplateData("keyword1", pgre.getPatient_name(), "#000000"));
//                templateMessage.getData().add(new WxMpTemplateData("keyword2", pgre.getContent(), "#000000"));
//                wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
//            }
            if (patientGroupReceivingEntityList != null) {
                jsonResult.setErrorcode("0");
                jsonResult.setMessage("groupsending success");
                jsonResult.setData(null);
                this.logger.info("群发消息成功");
            } else {
                jsonResult.setErrorcode("10001");
                jsonResult.setMessage("there is an error in mysql while groupsending .");
                jsonResult.setData(null);
                this.logger.error("群发消息失败");
            }
        } catch (Exception e) {
            this.logger.error("医生群发消息时异常" + e.getMessage());
            jsonResult.setErrorcode("10001");
            jsonResult.setMessage("there is an exception while group sending . exception:" + e.getMessage());
            jsonResult.setData(null);
            this.logger.error("群发消息失败" + e.getMessage());
        }
        return jsonResult;
    }


}
