package com.example.springbootwebflux;

import com.example.springbootwebflux.models.dao.ProductoDao;
import com.example.springbootwebflux.models.documents.Producto;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.core.publisher.Flux;

import java.util.Date;
import java.util.logging.Logger;

@SpringBootApplication
public class SpringBootWebfluxApplication implements CommandLineRunner {

    @Autowired
    private ProductoDao dao;

    @Autowired
    private ReactiveMongoTemplate mongoTemplate;

    private static final org.slf4j.Logger log =  LoggerFactory.getLogger(SpringBootWebfluxApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SpringBootWebfluxApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        mongoTemplate.dropCollection("productos").subscribe();
        Flux.just(new Producto("TV",50.96),
                        new Producto("Licuadora",150.96),
                        new Producto("celular",850.96),
                new Producto("laptop",2050.96))
                .flatMap(producto -> {
                    producto.setCreateAt(new Date());
                    return dao.save(producto);
                })
                .subscribe(producto->log.info("Insert: "+producto.getId()));
    }
}
