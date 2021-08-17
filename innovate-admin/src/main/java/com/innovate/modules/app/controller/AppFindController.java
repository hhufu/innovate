package com.innovate.modules.app.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;

import com.innovate.common.utils.DateUtils;
import com.innovate.common.utils.R;

import com.innovate.common.utils.VerCodeGenerateUtil;

import com.innovate.modules.sys.entity.SysMailCodeEntity;
import com.innovate.modules.sys.entity.SysUserEntity;

import com.innovate.modules.sys.service.SysMailCodeService;
import com.innovate.modules.sys.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.*;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.*;
import java.util.concurrent.*;

/**
 * @author luo
 * @Description 找回密码接口
 * @email 1275986528@qq.com
 */
@EnableAsync
@RestController
@RequestMapping("/app")
@Api("APP找回密码接口")
public class AppFindController {
    /**
     * 创建线程池对象
     */
    ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(10);
    /**
     * 这个是邮件类，必须要导入
     */
    @Autowired
    JavaMailSenderImpl mailSender;
    @Autowired
    SysUserService sysUserService;
    @Autowired
    private SysMailCodeService sysMailCodeService;

    @Async("taskExecutor")
    @GetMapping("sendEmail")
    @ApiOperation("发送验证码")
    public R sendEmail(@RequestParam("email") String email) {
        SysUserEntity sysUserEntity = sysUserService.queryByEmail(email);
        if (sysUserEntity == null) {
            return R.error().put("user",null);
        } else {
            //验证码
            String verCode = VerCodeGenerateUtil.getVerCode();
            SysMailCodeEntity sysMailCodeEntity = new SysMailCodeEntity();
            sysMailCodeEntity.setMailCode(verCode);
            sysMailCodeEntity.setUserId(sysUserEntity.getUserId());
            sysMailCodeService.insert(sysMailCodeEntity);
            //发送时间格式转换，用于邮件发送
            String time = DateUtils.format(new Date(), "yyyy-MM-dd hh:mm:ss");
            //创建计时任务，到时间设置验证码无效
            scheduledExecutorService.schedule(new Runnable() {
                @Override
                public void run() {
                    sysMailCodeEntity.setInvalid(1);
                    sysMailCodeService.update(sysMailCodeEntity,new EntityWrapper<SysMailCodeEntity>().eq("mail_code",verCode).eq("user_id",sysUserEntity.getUserId()));
                }}, 5 * 60, TimeUnit.SECONDS);
            //以下为发送邮件部分
            MimeMessage mimeMessage = null;
            MimeMessageHelper helper = null;
            try {
                //发送复杂的邮件
                mimeMessage = mailSender.createMimeMessage();
                //组装
                helper = new MimeMessageHelper(mimeMessage, true);
                //邮件标题
                helper.setSubject("【梧院双创平台】 找回登录密码");
                //因为设置了邮件格式所以html标签有点多，后面的true为支持识别html标签
                //想要不一样的邮件格式，百度搜索一个html编译器，自我定制。
                helper.setText("<h3>\n" +
                        "\t<span style=\"font-size:16px;\">亲爱的用户：</span> \n" +
                        "</h3>\n" +
                        "<p>\n" +
                        "\t<span style=\"font-size:14px;\">&nbsp;&nbsp;&nbsp;&nbsp;</span><span style=\"font-size:14px;\">&nbsp; <span style=\"font-size:16px;\">&nbsp;&nbsp;您好！您正在进行邮箱验证，本次请求的验证码为：<span style=\"font-size:24px;color:#FFE500;\"> " + verCode + "</span>,本验证码5分钟内有效，请在5分钟内完成验证。（请勿泄露此验证码）如非本人操作，请忽略该邮件。(这是一封自动发送的邮件，请不要直接回复）</span></span>\n" +
                        "</p>\n" +
                        "<p style=\"text-align:right;\">\n" +
                        "\t<span style=\"background-color:#FFFFFF;font-size:16px;color:#000000;\"><span style=\"color:#000000;font-size:16px;background-color:#FFFFFF;\"><span class=\"token string\" style=\"font-family:&quot;font-size:16px;color:#000000;line-height:normal !important;background-color:#FFFFFF;\">梧院双创平台</span></span></span> \n" +
                        "</p>\n" +
                        "<p style=\"text-align:right;\">\n" +
                        "\t<span style=\"background-color:#FFFFFF;font-size:14px;\"><span style=\"color:#FF9900;font-size:18px;\"><span class=\"token string\" style=\"font-family:&quot;font-size:16px;color:#000000;line-height:normal !important;\"><span style=\"font-size:16px;color:#000000;background-color:#FFFFFF;\">" + time + "</span><span style=\"font-size:18px;color:#000000;background-color:#FFFFFF;\"></span></span></span></span> \n" +
                        "</p>", true);
                //收件人
                helper.setTo(email);
                //发送方
                helper.setFrom("gxuwz_edu@163.com");
                try {
                    //发送邮件
                    mailSender.send(mimeMessage);
                } catch (MailException e) {
                    return R.error("邮箱是无效的，发送失败");
                }
            } catch (MessagingException e) {
                return R.error("发送失败--服务器繁忙");
            }
            //发送验证码成功
            return R.ok();
        }
    }

    @PostMapping(value = {"/change"})
    @ApiOperation("修改密码")
    public R forget(@RequestBody SysUserEntity user) {
        //接收前端传来的值
        String captcha = user.getCaptcha();
        String newPassword = user.getPassword();
        //根据email查询用户
        user = sysUserService.queryByEmail(user.getEmail());
        SysMailCodeEntity sysMailCodeEntity = sysMailCodeService.queryMailCode(new SysMailCodeEntity(user.getUserId()));
        //验证码判断
        if(sysMailCodeEntity == null) {
            return R.error("邮箱验证码无效，请重新获取~");
        }else if (!captcha.toUpperCase().equals(sysMailCodeEntity.getMailCode().toUpperCase())) {
            return R.error("邮箱验证码错误~");
        }else{
            if(StringUtils.isBlank(newPassword)){
                user.setPassword(null);
            }else{
                user.setPassword(new Sha256Hash(newPassword, user.getSalt()).toHex());
            }
            sysUserService.updateById(user);
            return R.ok("密码修改成功！");
        }
    }
}
