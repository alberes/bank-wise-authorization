package io.github.alberes.bank.wise.authorization.domains;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity(name = "client_account")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"transactions"})
public class ClientAccount implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", length = 100, nullable = false)
    public String name;

    @Column(name = "legal_entity_number", length = 11, nullable = false, unique = true)
    public String legalEntityNumber;

    @Column(name = "login", length = 100, nullable = false, unique = true)
    private String login;

    @Column(name = "password", length = 200, nullable = false)
    private String password;

    @CreatedDate
    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "last_modified_date", nullable = false)
    private LocalDateTime lastModifiedDate;

    @ElementCollection
    @CollectionTable(name = "user_account_scope")
    private Set<String> scopes;

    @OneToMany(mappedBy = "clientAccount")
    private List<TransactionAccount> transactions;

}
