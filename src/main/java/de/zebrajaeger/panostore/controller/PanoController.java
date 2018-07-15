package de.zebrajaeger.panostore.controller;

import de.zebrajaeger.panostore.data.ApplicationUser;
import de.zebrajaeger.panostore.data.Pano;
import de.zebrajaeger.panostore.repo.PanoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("api/pano")
public class PanoController {
    @Autowired
    private PanoRepository panoRepository;

    @GetMapping(produces ={ "application/json"})
    public List<PanoResult> getPanos() {
        List<PanoResult> result = new LinkedList<>();
        panoRepository.findAll().forEach(p -> result.add(new PanoResult(p)));
        return result;
    }

    @GetMapping(path="yo1",produces ={ "application/json"})
    public String yo1(){
        return "Yo1!!";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(path="yo2",produces ={ "application/json"})
    public List<PanoResult>  yo2(@AuthenticationPrincipal User o){
        List<PanoResult> result = new LinkedList<>();
        panoRepository.findAll().forEach(p -> result.add(new PanoResult(p)));
        return result;
    }

    public class PanoResult {
        private long id;
        private long name;

        public PanoResult(Pano pano) {
            id = pano.getId();
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public long getName() {
            return name;
        }

        public void setName(long name) {
            this.name = name;
        }
    }
}