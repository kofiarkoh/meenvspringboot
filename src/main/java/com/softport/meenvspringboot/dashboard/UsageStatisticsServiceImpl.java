package com.softport.meenvspringboot.dashboard;

import com.softport.meenvspringboot.messages.Message;
import com.softport.meenvspringboot.repositories.MessageRepository;
import com.softport.meenvspringboot.repositories.UserRepository;
import com.softport.meenvspringboot.user.MiscData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;


@Service
@RequiredArgsConstructor
public class UsageStatisticsServiceImpl implements  UsageStatisticsService{
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;


    @Override
    public int getTotalSmsSent() {

        List<Message> messages = messageRepository.getMessageByAllUsers();
        AtomicInteger sum = new AtomicInteger(0);

        messages.stream().forEach(m -> sum.addAndGet(m.getRecipientCount()) );

        return sum.get();
    }

    @Override
    public int getNumberOfUsers() {
        return userRepository.userCount();
    }

    @Override
    public float calculateProfit(int smsSent) {
        // profit per SMS is 0.005
        return 0.005f * (float) smsSent;
    }

    @Override
    public float calculateSmsSentCost(int smsSent) {
        MiscData miscData = new MiscData();
        return miscData.getPricePerSMS()* (float) smsSent;
    }

    public UsageStatistics getStatistics(){
        int smsSent = getTotalSmsSent();
        UsageStatistics usageStatistics = new UsageStatistics();

        usageStatistics.setProfit(calculateProfit(smsSent));
        usageStatistics.setNumOfUsers(getNumberOfUsers());
        usageStatistics.setTotalSMSSentCost(calculateSmsSentCost(smsSent));
        usageStatistics.setTotalSMSSent(getTotalSmsSent());

        return  usageStatistics;
    }


}
