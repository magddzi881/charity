package pl.sii.charity.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.sii.charity.dto.AddMoneyDTO;
import pl.sii.charity.dto.CreateFundraisingEventDTO;
import pl.sii.charity.dto.FinancialReportDTO;
import pl.sii.charity.service.CollectionBoxService;
import pl.sii.charity.service.FundraisingEventService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CollectionController {

    private final FundraisingEventService eventService;
    private final CollectionBoxService boxService;

    public CollectionController(FundraisingEventService eventService, CollectionBoxService boxService) {
        this.eventService = eventService;
        this.boxService = boxService;
    }

    /**
     * Creates a new fundraising event
     */
    @PostMapping("/events")
    public ResponseEntity<String> createEvent(@RequestBody CreateFundraisingEventDTO dto) {
        try {
            eventService.createEvent(dto);
            return ResponseEntity.ok("Fundraising event created successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * Registers a new collection box
     */
    @PostMapping("/boxes")
    public ResponseEntity<String> registerBox() {
        try {
            boxService.registerBox();
            return ResponseEntity.ok("Empty collection box registered successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * Lists all collection boxes
     */
    @GetMapping("/boxes")
    public ResponseEntity<?> listBoxes() {
        try {
            List<String> boxes = boxService.getAllBoxes().stream()
                    .map(b -> "Box ID: " + b.getId() + ", Assigned: " + b.isAssigned() + ", Empty: " + b.getMoney().isEmpty())
                    .toList();
            return ResponseEntity.ok(boxes);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve boxes.");
        }
    }

    /**
     * Removes (unregister) a collection box
     */
    @DeleteMapping("/boxes/{boxId}")
    public ResponseEntity<String> removeBox(@PathVariable Long boxId) {
        try {
            boxService.removeBox(boxId);
            return ResponseEntity.ok("Collection box removed successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * Assigns collection box to event
     */
    @PostMapping("/boxes/{boxId}/assign/{eventId}")
    public ResponseEntity<String> assignBox(@PathVariable Long boxId, @PathVariable Long eventId) {
        try {
            boxService.assignToEvent(boxId, eventId);
            return ResponseEntity.ok("Box assigned to fundraising event successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * Puts money in box
     */
    @PostMapping("/boxes/{boxId}/money")
    public ResponseEntity<String> putMoney(@PathVariable Long boxId, @RequestBody AddMoneyDTO dto) {
        try {
            boxService.addMoney(boxId, dto);
            return ResponseEntity.ok("Money added to collection box successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * Transfers money from box to fundraising event
     */
    @PostMapping("/boxes/{boxId}/transfer")
    public ResponseEntity<String> emptyBox(@PathVariable Long boxId) {
        try {
            boxService.transferMoney(boxId);
            return ResponseEntity.ok("Money transferred from box to fundraising event successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * Creates financial report of all events
     */
    @GetMapping("/report")
    public ResponseEntity<?> getReport() {
        try {
            List<FinancialReportDTO> report = eventService.getFinancialReport();
            return ResponseEntity.ok(report);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to generate report.");
        }
    }
}
