package scut.jiayibilin.wechat.controller.feign.mysql.doctor;


import scut.jiayibilin.wechat.entity.*;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by jie on 2017/8/8.
 */
@FeignClient(name="microservice-jiayibilin-mySql",url="http://mrxiej.ngrok.wendal.cn/api-mysql")
public interface DoctorClient {
    /*
    * 调用远程服务下通过医生phone返回医生实体接口
    * */
    @RequestMapping(value="/doctor/findbyphone",method = RequestMethod.GET)
    DoctorEntity findbydoctorphone(@RequestParam("phone") String phone);
    /*
    * 调用远程服务下返回全部医生接口
    * */
    @RequestMapping(value="/doctor/findall",method = RequestMethod.GET)
    List<DoctorEntity> findalldoctor();
    /*
    * 返回所有服务包
    * */
    @RequestMapping(value="/doctor/service",method = RequestMethod.GET)
    List<DoctorServiceEntity> findDoctorService(@RequestParam("phone") String phone);
    /*
    *医生群发消息
     */
    @RequestMapping(value = "/doctor/groupsending", method = RequestMethod.POST)
    List<PatientGroupReceivingEntity> doctorGroupSending(DoctorGroupSendingEntity doctorGroupSendingEntity);

}
