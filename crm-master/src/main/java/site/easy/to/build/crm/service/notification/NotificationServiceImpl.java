package site.easy.to.build.crm.service.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.easy.to.build.crm.entity.Notification;
import site.easy.to.build.crm.repository.NotificationRepository;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public Notification save(Notification notification) {
        return notificationRepository.save(notification);
    }

    @Override
    public void delete(Notification notification) {
        notificationRepository.delete(notification);
    }
    public List<Notification> getNotifUser(int idUser){
        return notificationRepository.getByCustomerCustomerIdOrderByDateNotifDesc(idUser);
    }
    @Override
    public Optional<Notification>getById(int id){
        return notificationRepository.findById(id);
    }
}
