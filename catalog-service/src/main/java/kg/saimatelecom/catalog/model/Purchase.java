package kg.saimatelecom.catalog.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@Table(name = "purchases")
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Purchase extends BaseEntity {

    String username;

    @ManyToOne
    Product product;

    @Column(nullable = false)
    BigDecimal cost;

    @Column(nullable = false)
    BigDecimal realCost;

    @Column(nullable = false)
    Integer quantity;
}
