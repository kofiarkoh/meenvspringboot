package com.softport.meenvspringboot.dashboard;


public interface UsageStatisticsService {


    int getTotalSmsSent();
    int getNumberOfUsers();
    float calculateProfit(int smsSent);
    float calculateSmsSentCost(int smsSent);
    UsageStatistics getStatistics();
}
