package io.github.alberes.bank.wise.authorization.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum TransactionType {

    DEPOSIT(1, "DEPOSIT"),
    WITHDRAW(2, "WITHDRAW"),
    PAYMENT(3, "PAYMENT"),
    INTEREST(4, "INTEREST");

    private Integer id;

    private String description;

    private static Map<Integer, TransactionType> mapTransactionType;

    private TransactionType(Integer id, String description){
        this.id = id;
        this.description = description;
        TransactionType.getMapTransactionType().put(this.id, this);
    }

    public static TransactionType getTransactionType(Integer id){
        synchronized (mapTransactionType){
            return mapTransactionType.get(id);
        }
    }

    public static Map<Integer, TransactionType> getMapTransactionType(){
        if(mapTransactionType == null){
            mapTransactionType = new HashMap<Integer, TransactionType>();
        }
        return mapTransactionType;
    }
}
