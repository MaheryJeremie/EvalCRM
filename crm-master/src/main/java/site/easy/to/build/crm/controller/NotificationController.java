package site.easy.to.build.crm.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import site.easy.to.build.crm.entity.Notification;
import site.easy.to.build.crm.service.notification.NotificationService;
import site.easy.to.build.crm.util.AuthenticationUtils;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/notification")
public class NotificationController {
    private final NotificationService notificationService;
    private final AuthenticationUtils authenticationUtils;

    public NotificationController(NotificationService notificationService, AuthenticationUtils authenticationUtils) {
        this.notificationService = notificationService;
        this.authenticationUtils = authenticationUtils;
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id){
        Notification notification=notificationService.getById(id).orElse(null);
        notificationService.delete(notification);
        return "redirect:/notification";

    }

    @GetMapping
    public String getNotifUser(Authentication authentication, Model model){
        int userId = authenticationUtils.getLoggedInUserId(authentication);
        List<Notification> listes=notificationService.getNotifUser(userId);
        model.addAttribute("listes",listes);
        return "notification/liste";
    }


}
