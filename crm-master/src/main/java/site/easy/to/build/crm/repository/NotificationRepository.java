package site.easy.to.build.crm.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import site.easy.to.build.crm.entity.Notification;

import java.util.List;


public interface NotificationRepository extends JpaRepository<Notification,Integer> {
    public List<Notification> getByCustomerCustomerIdOrderByDateNotifDesc(int id);
}
