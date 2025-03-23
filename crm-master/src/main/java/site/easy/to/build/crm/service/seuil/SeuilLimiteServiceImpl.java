package site.easy.to.build.crm.service.seuil;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import site.easy.to.build.crm.entity.SeuilLimite;
import site.easy.to.build.crm.repository.SeuilLimiteRepository;
import java.util.List;
import java.util.Optional;

@Service
public class SeuilLimiteServiceImpl implements SeuilLimiteService {

    @Autowired
    private SeuilLimiteRepository seuilLimiteRepository;

    @Override
    public List<SeuilLimite> getAll() {
        return seuilLimiteRepository.findAll();
    }

    @Override
    public SeuilLimite update(SeuilLimite seuilLimite) {
        return seuilLimiteRepository.save(seuilLimite);
    }

    @Override
    public SeuilLimite findById(int id) {
        Optional<SeuilLimite> optionalSeuilLimite = seuilLimiteRepository.findById(id);
        return optionalSeuilLimite.orElse(null);
    }
}
