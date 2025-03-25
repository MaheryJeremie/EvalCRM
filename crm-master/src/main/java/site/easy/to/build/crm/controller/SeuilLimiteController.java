package site.easy.to.build.crm.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.easy.to.build.crm.entity.SeuilLimite;
import site.easy.to.build.crm.service.seuil.SeuilLimiteService;

import java.util.List;

@RestController
@RequestMapping("/api/seuil")
public class SeuilLimiteController {

    private final SeuilLimiteService seuilLimiteService;

    public SeuilLimiteController(SeuilLimiteService seuilLimiteService) {
        this.seuilLimiteService = seuilLimiteService;
    }



    @GetMapping
    public ResponseEntity<SeuilLimite> getSeuil() {
        try{
        SeuilLimite seuilLimite = seuilLimiteService.getAllSorted().get(0);
        return ResponseEntity.ok(seuilLimite);
        }
        catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<SeuilLimite> updateSeuil(@RequestBody SeuilLimite seuilLimite2) {
        try{
            SeuilLimite updatedSeuilLimite = seuilLimiteService.update(seuilLimite2);
            return ResponseEntity.ok(updatedSeuilLimite);
        }
        catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<SeuilLimite> getById(@PathVariable int id){
        try{
            SeuilLimite seuilLimite=seuilLimiteService.findById(id);
            return ResponseEntity.ok(seuilLimite);
        }
        catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }
    @GetMapping("/all")
    public ResponseEntity<List<SeuilLimite>> getAll() {
        try {
            List<SeuilLimite> seuilLimites = seuilLimiteService.getAllSorted();
            return ResponseEntity.ok(seuilLimites);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}