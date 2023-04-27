package com.softport.meenvspringboot.dashboard;

import lombok.Data;

@Data
public class UsageStatistics {
    private int numOfUsers;
    private int totalSMSSent;
    private float totalSMSSentCost;
    private float profit;
}
