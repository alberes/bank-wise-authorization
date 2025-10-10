package io.github.alberes.bank.wise.authorization.domains;

import io.github.alberes.bank.wise.authorization.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "transaction_account")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionAccount implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "transaction_type", nullable = false)
    private Integer transactionType;

    @Column(name = "transaction_value", nullable = false)
    private BigDecimal transactionValue;

    @CreatedDate
    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @ManyToOne
    @JoinColumn(name = "client_account_id")
    private ClientAccount clientAccount;

    public TransactionType getMapTransactionType(){
        return TransactionType.getTransactionType(this.transactionType);
    }
}
