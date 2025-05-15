package pl.sii.charity.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.sii.charity.dto.CreateFundraisingEventDTO;
import pl.sii.charity.entity.CurrencyType;
import pl.sii.charity.service.CollectionBoxService;
import pl.sii.charity.service.FundraisingEventService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CollectionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FundraisingEventService eventService;

    @Autowired
    private CollectionBoxService boxService;

    @BeforeEach
    public void setup() {
        CreateFundraisingEventDTO eventDto1 = new CreateFundraisingEventDTO("Charity One", CurrencyType.EUR);
        CreateFundraisingEventDTO eventDto2 = new CreateFundraisingEventDTO("Charity Two", CurrencyType.USD);
        CreateFundraisingEventDTO eventDto3 = new CreateFundraisingEventDTO("Charity Three", CurrencyType.PLN);

        eventService.createEvent(eventDto1);
        eventService.createEvent(eventDto2);
        eventService.createEvent(eventDto3);

        boxService.registerBox();
        boxService.registerBox();
        boxService.registerBox();
        boxService.registerBox();
        boxService.registerBox();
    }

    @Test
    public void testCreateEvent() throws Exception {
        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"New Charity Event\",\"currency\":\"USD\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Fundraising event created successfully."));
    }

    @Test
    public void testCreateEventFailed() throws Exception {
        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"New Charity Event\",\"currency\":\"9\"}"))
                .andExpect(status().is(400));
    }

    @Test
    public void testRegisterBox() throws Exception {
        mockMvc.perform(post("/api/boxes"))
                .andExpect(status().isOk())
                .andExpect(content().string("Empty collection box registered successfully."));
    }

    @Test
    public void testRemoveBox() throws Exception {
        mockMvc.perform(post("/api/boxes"));

        mockMvc.perform(delete("/api/boxes/5"))
                .andExpect(status().isOk())
                .andExpect(content().string("Collection box removed successfully."));
    }

    @Test
    public void testRemoveBoxFailed() throws Exception {
        mockMvc.perform(delete("/api/boxes/0"))
                .andExpect(status().is(400))
                .andExpect(content().string("Box with ID 0 not found."));
    }

    @Test
    public void testListBoxes() throws Exception {
        mockMvc.perform(get("/api/boxes"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Box ID:")));
    }

    @Test
    public void testAssignBoxToEvent() throws Exception {
        mockMvc.perform(post("/api/boxes/6/assign/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Box assigned to fundraising event successfully."));
    }

    @Test
    public void testAssignBoxToEventFailed() throws Exception {
        mockMvc.perform(post("/api/boxes/5/assign/1"));

        mockMvc.perform(post("/api/boxes/5/assign/2"))
                .andExpect(status().is(400))
                .andExpect(content().string("Box is already assigned to an event."));
    }

    @Test
    public void testAssignBoxToEventFailed2() throws Exception {
        mockMvc.perform(post("/api/boxes/7/assign/0"))
                .andExpect(status().is(400))
                .andExpect(content().string("Event with ID 0 not found."));
    }

    @Test
    public void testPutMoneyInBox() throws Exception {
        mockMvc.perform(post("/api/boxes/3/money")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"currency\":\"EUR\",\"amount\":100.00}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Money added to collection box successfully."));
    }

    @Test
    public void testPutMoneyInBoxFailed() throws Exception {
        mockMvc.perform(post("/api/boxes/0/money")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"currency\":\"EUR\",\"amount\":100.00}"))
                .andExpect(status().is(400))
                .andExpect(content().string("Box with ID 0 not found."));
    }

    @Test
    public void testTransferMoneyFromBox() throws Exception {
        mockMvc.perform(post("/api/boxes/2/assign/2"));

        mockMvc.perform(post("/api/boxes/2/money")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"currency\":\"EUR\",\"amount\":100.00}"));

        mockMvc.perform(post("/api/boxes/2/transfer"))
                .andExpect(status().isOk())
                .andExpect(content().string("Money transferred from box to fundraising event successfully."));
    }

    @Test
    public void testTransferMoneyFromBoxFailed() throws Exception {
        mockMvc.perform(post("/api/boxes/5/assign/2"));

        mockMvc.perform(post("/api/boxes/5/money")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"currency\":\"EUR\",\"amount\":100.00}"));

        mockMvc.perform(post("/api/boxes/0/transfer"))
                .andExpect(status().is(400))
                .andExpect(content().string("Box with ID 0 not found."));
    }

}
