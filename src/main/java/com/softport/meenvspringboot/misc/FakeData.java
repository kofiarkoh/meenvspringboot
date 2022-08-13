package com.softport.meenvspringboot.misc;

import com.github.javafaker.Faker;
import com.softport.meenvspringboot.topup.TopUp;
import com.softport.meenvspringboot.topup.TopupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FakeData {


    private  final TopupRepository topupRepository;
    public void generateFakeTopUps(long userId,String transStatus, String[] transIds){
        Faker faker = new Faker();
        List<TopUp> topUps = new ArrayList<>();
        try{
            for (int i=0 ; i< 5; i++){
                TopUp topUp = new TopUp();

                topUp.setPhoneNumber(faker.phoneNumber().subscriberNumber(10));
                topUp.setOtp(Long.parseLong(faker.number().digits(5)));
                topUp.setSmsQuantity(faker.number().numberBetween(20,500));
                topUp.setAmount((float) faker.number().numberBetween(1, 50) );
                topUp.setDate(faker.date().birthday());
                topUp.setStatus(transStatus);
                topUp.setTransactionId(transIds[i]);
                topUp.setNetwork(faker.options().nextElement(List.of("MTN","AIRTIG","VOD")));
                topUp.setUserId(userId);

                //faker.options().nextElement(List.of("MTN","AIRTIG","VOD"))

                topUps.add(topUp);
            }
            topupRepository.saveAll(topUps);

            log.info("topup after save is {}",topupRepository.findAll());
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

    }
}
