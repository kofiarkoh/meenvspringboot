package com.softport.meenvspringboot.dashboard;

import com.softport.meenvspringboot.messages.Message;
import com.softport.meenvspringboot.repositories.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequiredArgsConstructor
public class DashboardController {

    private final UsageStatisticsService usageStatisticsService;
    private final MessageRepository messageRepository;
    @GetMapping("stats")
    public ResponseEntity<?> getStats(){


        return new ResponseEntity<>(usageStatisticsService.getStatistics(), HttpStatus.OK);
    }
}
