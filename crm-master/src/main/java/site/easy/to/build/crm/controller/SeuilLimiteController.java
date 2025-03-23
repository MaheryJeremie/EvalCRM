package site.easy.to.build.crm.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.easy.to.build.crm.entity.SeuilLimite;
import site.easy.to.build.crm.service.seuil.SeuilLimiteService;

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
        SeuilLimite seuilLimite = seuilLimiteService.getAll().get(0);
        return ResponseEntity.ok(seuilLimite);
        }
        catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }


    @PutMapping
    public ResponseEntity<SeuilLimite> updateSeuil(@RequestBody SeuilLimite seuilLimite) {
        try{
        SeuilLimite updatedSeuilLimite = seuilLimiteService.update(seuilLimite);
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
}