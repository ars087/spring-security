package TestJavaCode.AppLocalDateTimeTask.controller;

import TestJavaCode.AppLocalDateTimeTask.model.EventRequestDto;
import TestJavaCode.AppLocalDateTimeTask.model.EventResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1")
public class EventController {

    @PostMapping(value = "/events")
    public ResponseEntity<?> getFormatDate(@RequestBody EventRequestDto event) {

        return ResponseEntity.ok(new EventResponseDto(event.getEventDateTime()));

    }
}
