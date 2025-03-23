package site.easy.to.build.crm.service.seuil;

import site.easy.to.build.crm.entity.SeuilLimite;
import java.util.List;

public interface SeuilLimiteService {
    List<SeuilLimite> getAll();
    SeuilLimite update(SeuilLimite seuilLimite);
    SeuilLimite findById(int id);
}
