package de.zebrajaeger.panostore;

import de.zebrajaeger.panostore.data.PanoFile;
import de.zebrajaeger.panostore.service.PanoService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class PanostoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(PanostoreApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(PanoService panoService) {
        return (args) -> {
            //panoService.importPano(new File("D:\\!pano\\2011\\2011-07-16\\yo.zip.zip"));
            PanoFile f = panoService.findFile(1L, "pano.tiles/mres_r/l7/9/l7_r_9_6.jpg");
            System.out.println("##### " + (f != null));
        };
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
