package site.easy.to.build.crm.service.notification;

import org.aspectj.weaver.ast.Not;
import site.easy.to.build.crm.entity.Notification;

import java.util.List;
import java.util.Optional;

public interface NotificationService {
    public Notification save(Notification notification);
    public void delete(Notification notification);
    public List<Notification> getNotifUser(int idUser);
    public Optional<Notification> getById(int id);
}
