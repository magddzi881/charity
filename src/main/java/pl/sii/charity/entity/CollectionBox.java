package pl.sii.charity.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.*;

@Entity
@Table(name = "collection_box")
public class CollectionBox {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "assigned", nullable = false)
    private boolean assigned;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private FundraisingEvent fundraisingEvent;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "collection_box_money", joinColumns = @JoinColumn(name = "collection_box_id"))
    @MapKeyColumn(name = "currency")
    @MapKeyEnumerated(EnumType.STRING)
    @Column(name = "amount")
    private Map<CurrencyType, BigDecimal> money = new HashMap<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isAssigned() {
        return assigned;
    }

    public void setAssigned(boolean assigned) {
        this.assigned = assigned;
    }

    public FundraisingEvent getFundraisingEvent() {
        return fundraisingEvent;
    }

    public void setFundraisingEvent(FundraisingEvent fundraisingEvent) {
        this.fundraisingEvent = fundraisingEvent;
    }

    public Map<CurrencyType, BigDecimal> getMoney() {
        return money;
    }

    public void setMoney(Map<CurrencyType, BigDecimal> money) {
        this.money = money;
    }
}