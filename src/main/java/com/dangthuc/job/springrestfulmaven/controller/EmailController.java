package com.dangthuc.job.springrestfulmaven.controller;

import com.dangthuc.job.springrestfulmaven.service.EmailService;
import com.dangthuc.job.springrestfulmaven.service.SubscriberService;
import com.dangthuc.job.springrestfulmaven.util.annotation.ApiMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private SubscriberService subscriberService;

    @GetMapping("/email")
    @ApiMessage("send email")
    public String sendEmail() {
//        emailService.sendSimpleEmail();
//        emailService.sendEmailSync("trthucpro@gmail.com", "Test Send Email", "<h1>Xin cjao</h1>", false, true);
//        emailService.sendEmailFromTemplateSync("trthucpro@gmail.com", "Test Send Email", "job");

        this.subscriberService.sendSubscribersEmailJobs();
        return "send";

    }
}
