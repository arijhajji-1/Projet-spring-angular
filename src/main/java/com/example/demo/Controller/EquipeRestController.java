package com.example.demo.Controller;

import com.example.demo.Entities.DetailEquipe;
import com.example.demo.Entities.Equipe;
import com.example.demo.Repository.IEquipeRepository;
import com.example.demo.Service.IDetailEquipeService;
import com.example.demo.Service.IEquipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/equipe")
//@CrossOrigin(origins = "http://localhost:4200",allowedHeaders = "*")
@CrossOrigin(origins = "*")
public class EquipeRestController {
    private final IEquipeService iEquipeService;

    private final IEquipeRepository iEquipeRepository;



    private final IDetailEquipeService iDetailEquipeService;


    @GetMapping("/all")
    @ResponseBody
    public List<Equipe> getAllEquipes(){
        return  iEquipeService.retrieveAllEquipes();
    }
    @PostMapping("/add")
    public Equipe addEquipe(@RequestBody Equipe equipe){
        return  iEquipeService.addEquipe(equipe);
    }



    @PutMapping("/update")
    public Equipe updateEquipe(@RequestBody Equipe equipe){
        return iEquipeService.updateEquipe(equipe);
    }



    @PutMapping("/updateById/{ideq}")
    public Equipe updateByIdEquipe(@RequestBody Equipe equipe,
                                   @PathVariable("ideq") Integer ideq
                                   ){
        return iEquipeService.updateByIdEquipe(equipe,ideq);
    }








    @GetMapping("/getequipe/{id-equipe}")
    public Equipe getById(@PathVariable("id-equipe") Integer id){
        return iEquipeService.retrieveEquipe(id);
    }



    @DeleteMapping("/deleteEquipe/{id}")
    @ResponseBody
    public void removeDetailEquipe(@PathVariable("id") Integer idEquipe ){
       // detailEquipeService.removeDetailEquipe(idEquipe);
        iEquipeService.supprimer(idEquipe);
    }


    /**
     *
     * @param detailEquipe  ajouter et affecter details equipe to equipe
     * @param id
     */

    @PutMapping("addDetails/{id}")
    public void saveDetailsTeam(
            @Valid @RequestBody DetailEquipe detailEquipe, @PathVariable Integer id)
    {
        //detailEquipeService.create(detailEquipe);
        iDetailEquipeService.create(detailEquipe);

       // equipeService.assignEquipeToDetail(id,detailEquipe);
        iEquipeService.assignEquipeToDetail(id,detailEquipe);


    }




    /////////////////recherche /////////////////////////


    @GetMapping("/search/{nomEquipe}")
    public ResponseEntity<?> searchByNomEquipe(@PathVariable String nomEquipe, @PageableDefault(sort = "nomEquipe", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<Equipe> equipePage = iEquipeService.findAllByNomEquipeContaining(nomEquipe, pageable);

        return new ResponseEntity<>(equipePage, HttpStatus.OK);
    }




    /////////////////pagination /////////////////////////
    @GetMapping("/equipe")
    @ResponseBody
    public Page<Equipe> getAllEquipe(Pageable pageable) {
        return iEquipeService.lire(pageable);
    }



    ///////////////////exporter excel///////////////////////////

    @GetMapping("/export/excel")

    public ResponseEntity<InputStreamResource> exportExperiencesExcel() throws IOException {

        List<Equipe> equipes = (List<Equipe>) iEquipeService.retrieveAllEquipes();
        ByteArrayInputStream bais = iEquipeService.experienceExcelReport(equipes);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=experiences.xlsx");
        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(bais));
    }













}
