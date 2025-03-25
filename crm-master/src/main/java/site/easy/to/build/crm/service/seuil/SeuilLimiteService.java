package site.easy.to.build.crm.service.seuil;

import site.easy.to.build.crm.entity.SeuilLimite;
import java.util.List;

public interface SeuilLimiteService {
    List<SeuilLimite> getAllSorted();
    SeuilLimite update(SeuilLimite seuilLimite);
    SeuilLimite findById(int id);
}
