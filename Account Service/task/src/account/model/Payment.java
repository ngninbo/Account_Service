package account.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "sequencePayment", sequenceName = "PaymentSeq")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "sequencePayment")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User employee;
    private String period;
    private Long salary;
}
