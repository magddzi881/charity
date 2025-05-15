package pl.sii.charity.service;

import org.springframework.stereotype.Service;
import pl.sii.charity.dto.AddMoneyDTO;
import pl.sii.charity.entity.CollectionBox;
import pl.sii.charity.entity.CurrencyType;
import pl.sii.charity.entity.FundraisingEvent;
import pl.sii.charity.error.BoxNotFoundException;
import pl.sii.charity.repository.CollectionBoxRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class CollectionBoxService {

    private final CollectionBoxRepository repository;
    private final FundraisingEventService eventService;
    private final ExchangeService exchangeService;

    public CollectionBoxService(CollectionBoxRepository repository, FundraisingEventService eventService, ExchangeService exchangeService) {
        this.repository = repository;
        this.eventService = eventService;
        this.exchangeService = exchangeService;
    }

    /**
     * Creates a new, unassigned collection box
     */
    public void registerBox() {
        CollectionBox box = new CollectionBox();
        box.setAssigned(false);
        repository.save(box);
    }

    /**
     * Retrieves all existing collection boxes
     */
    public List<CollectionBox> getAllBoxes() {
        return repository.findAll();
    }

    /**
     * Assigns a collection box to a fundraising event
     */
    public void assignToEvent(Long boxId, Long eventId) {
        CollectionBox box = getBox(boxId);
        if (!box.getMoney().isEmpty()) {
            throw new RuntimeException("Box must be empty to assign.");
        }
        if (box.isAssigned()) {
            throw new RuntimeException("Box is already assigned to an event.");
        }
        FundraisingEvent event = eventService.getEvent(eventId);
        box.setAssigned(true);
        box.setFundraisingEvent(event);
        repository.save(box);
    }

    /**
     * Adds money to a collection box in a specified currency
     */
    public void addMoney(Long boxId, AddMoneyDTO dto) {
        CollectionBox box = getBox(boxId);
        box.getMoney().merge(dto.currency, dto.amount, BigDecimal::add);
        repository.save(box);
    }

    /**
     * Removes a collection box by ID
     */
    public void removeBox(Long boxId) {
        CollectionBox box = getBox(boxId);
        repository.delete(box);
    }

    /**
     * Unregisters a collection box by ID
     */
    public void unregisterBox(Long boxId) {
        CollectionBox box = getBox(boxId);
        if (!box.getMoney().isEmpty() && box.getMoney().values().stream().anyMatch(amount -> amount.compareTo(BigDecimal.ZERO) > 0)) {
            throw new RuntimeException("Cannot unregister a box that contains money.");
        }
        if (!box.isAssigned()) {
            throw new RuntimeException("Box is already unregistered.");
        }
        box.setAssigned(false);
        box.setFundraisingEvent(null);
        repository.save(box);
    }

    /**
     * Transfers all money from a collection box to its assigned fundraising event
     * All currencies are converted to the event's currency using the exchange service
     * After the transfer, the box is cleared
     */
    public void transferMoney(Long boxId) {
        CollectionBox box = getBox(boxId);
        if (!box.isAssigned() || box.getFundraisingEvent() == null) {
            throw new RuntimeException("Box is not assigned to any event.");
        }

        FundraisingEvent event = box.getFundraisingEvent();
        CurrencyType eventCurrency = event.getCurrency();
        BigDecimal total = BigDecimal.ZERO;

        for (Map.Entry<CurrencyType, BigDecimal> entry : box.getMoney().entrySet()) {
            CurrencyType currency = entry.getKey();
            BigDecimal amount = entry.getValue();
            BigDecimal converted = exchangeService.convert(amount, currency, eventCurrency);
            total = total.add(converted);
        }

        if (event.getAmount() == null) {
            event.setAmount(BigDecimal.ZERO);
        }
        event.setAmount(event.getAmount().add(total));

        eventService.save(event);
        box.getMoney().clear();
        repository.save(box);
    }

    /**
     * Retrieves a collection box by its ID
     */
    private CollectionBox getBox(Long boxId) {
        return repository.findById(boxId).orElseThrow(() -> new BoxNotFoundException(boxId));
    }
}
