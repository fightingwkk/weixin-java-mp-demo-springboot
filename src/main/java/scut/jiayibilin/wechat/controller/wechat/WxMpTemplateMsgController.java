package scut.jiayibilin.wechat.controller.wechat;


import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.WxMpTemplateMsgService;
import me.chanjar.weixin.mp.api.impl.WxMpTemplateMsgServiceImpl;
import me.chanjar.weixin.mp.bean.template.WxMpTemplate;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateIndustry;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="/wechat/templatemsg")
public class WxMpTemplateMsgController implements WxMpTemplateMsgService {
    @Autowired
    WxMpService wxMpService;
//    @Autowired
//    WxMpTemplateMsgServiceImpl wxMpTemplateMsgService;

    /**
     * 设置所属行业
     官方文档中暂未告知响应内容
     详情请见：http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1433751277&token=&lang=zh_CN
     */
    @Override
    @PostMapping(value = "/setindusttry")
    public boolean setIndustry(WxMpTemplateIndustry wxMpTemplateIndustry) throws WxErrorException {
        return wxMpService.getTemplateMsgService().setIndustry(wxMpTemplateIndustry);
    }

    /**
     * 获取设置的行业信息
     详情请见：http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1433751277&token=&lang=zh_CN
     * @return
     * @throws WxErrorException
     */
    @Override
    @GetMapping(value = "/getindustry")
    public WxMpTemplateIndustry getIndustry() throws WxErrorException {
        return wxMpService.getTemplateMsgService().getIndustry();
    }

    /**
     发送模板消息
     详情请见: http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1433751277&token=&lang=zh_CN
     * @param wxMpTemplateMessage
     * @return
     * @throws WxErrorException
     */
    @Override
    @PostMapping(value = "/sendtemplatemsg")
    public String sendTemplateMsg(WxMpTemplateMessage wxMpTemplateMessage) throws WxErrorException {
        return wxMpService.getTemplateMsgService().sendTemplateMsg(wxMpTemplateMessage);
    }


    /**
     * 获得模板ID
     从行业模板库选择模板到帐号后台，获得模板ID的过程可在MP中完成
     详情请见: http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1433751277&token=&lang=zh_CN
     接口地址格式：https://api.weixin.qq.com/cgi-bin/template/api_add_template?access_token=ACCESS_TOKEN
     * @param s
     * @return
     * @throws WxErrorException
     */
    @Override
    @PostMapping(value = "/addtemplate")
    public String addTemplate(String s) throws WxErrorException {
        return wxMpService.getTemplateMsgService().addTemplate(s);
    }

    /**
     * 获取模板列表
     获取已添加至帐号下所有模板列表，可在MP中查看模板列表信息，为方便第三方开发者，提供通过接口调用的方式来获取帐号下所有模板信息
     详情请见: http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1433751277&token=&lang=zh_CN
     接口地址格式：https://api.weixin.qq.com/cgi-bin/template/get_all_private_template?access_token=ACCESS_TOKEN
     * @return
     * @throws WxErrorException
     */
    @Override
    @GetMapping(value = "/getallprivatetemplate")
    public List<WxMpTemplate> getAllPrivateTemplate() throws WxErrorException {
        return wxMpService.getTemplateMsgService().getAllPrivateTemplate();
    }

    /**
     *  删除模板
     删除模板可在MP中完成，为方便第三方开发者，提供通过接口调用的方式来删除某帐号下的模板
     详情请见: http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1433751277&token=&lang=zh_CN
     接口地址格式：https://api.weixin.qq.com/cgi-bin/template/del_private_template?access_token=ACCESS_TOKEN
     * @param s
     * @return
     * @throws WxErrorException
     */
    @Override
    @PostMapping(value = "/delprivatetemplate")
    public boolean delPrivateTemplate(String s) throws WxErrorException {
        return wxMpService.getTemplateMsgService().delPrivateTemplate(s);
    }
}
