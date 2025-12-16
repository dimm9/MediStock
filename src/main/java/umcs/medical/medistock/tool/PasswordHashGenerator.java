package umcs.medical.medistock.tool;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class PasswordHashGenerator {


    //gowno do sprawdzenia hasha hasla, nie dotykac nie przejmowac sie
    @Bean
    CommandLineRunner generatePasswordHash(BCryptPasswordEncoder encoder) {
        return args -> {
            String raw = "hashed_password_1";
            String hash = encoder.encode(raw);

            System.out.println("===================================");
            System.out.println("RAW  : " + raw);
            System.out.println("HASH : " + hash);
            System.out.println("===================================");
        };
    }
}