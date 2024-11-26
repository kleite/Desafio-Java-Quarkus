package model;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Panache  implements PanacheRepository<Pagamentos> {
}
