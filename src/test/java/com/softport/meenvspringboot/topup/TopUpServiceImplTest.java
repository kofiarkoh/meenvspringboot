package com.softport.meenvspringboot.topup;

import com.softport.meenvspringboot.misc.FakeData;
import com.softport.meenvspringboot.repositories.UserRepository;
import com.softport.meenvspringboot.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
class TopUpServiceImplTest {

    @Mock
   private  TopupRepository topupRepository;
    @Mock
    private  UserRepository userRepository;
   @InjectMocks
   private    TopUpServiceImpl topUpService;

  // private final FakeData fakeData;
   private final String[] transactionIds = {"4232h","sf223","882373","998823","8123"};

   private ChargeResult chargeResult;
   private User user;
   private TopUp topUpData;

   @BeforeEach
   void setUp(){
       chargeResult = new ChargeResult();
       chargeResult.setData(new ChargeData());
       chargeResult.setEvent("charge.success");
       chargeResult.getData().setReference("sf223");

        user = new User();
       user.setId(22222);
       user.setSmsBalance(200);
       user.setSmsSent(150);

       topUpData = new TopUp();
       topUpData.setNetwork("MTN");
       topUpData.setStatus("pending");
       topUpData.setTransactionId("sf233");
       topUpData.setUserId(user.getId());
   }

    @Test
    void shouldUpdatePopupStatusToSuccessWhenTransactionIsSuccessfull(){
        chargeResult.getData().setStatus("success");
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(topupRepository.findByTransactionId("sf223")).thenReturn(topUpData);
        //log.info(userRepository.findById(22222L).toString());

        topUpService.verifyPaymentWebhookResponse(chargeResult);

        ArgumentCaptor<TopUp> topUpArgumentCaptor = ArgumentCaptor.forClass(TopUp.class);
        verify(topupRepository).save(topUpArgumentCaptor.capture());
        TopUp capturedTopup = topUpArgumentCaptor.getValue();
        System.out.println(capturedTopup);
        assertThat(capturedTopup.getStatus()).isEqualTo("SUCCESS");


    }

    @Test
    void user_balance_correctly_updated_for_successful_topup(){
       chargeResult.getData().setStatus("success");

       user.setSmsSent(100L);
       user.setSmsBalance(90L);

       topUpData.setSmsQuantity(300L);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(topupRepository.findByTransactionId("sf223")).thenReturn(topUpData);

        topUpService.verifyPaymentWebhookResponse(chargeResult);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());
        User userInfoUpdated = userArgumentCaptor.getValue();

        /*System.out.println(user.getSmsBalance());
        System.out.println(userInfoUpdated.getSmsBalance());*/
        assertThat(userInfoUpdated.getSmsBalance()).isEqualTo(390);
        assertThat(userInfoUpdated.getSmsSent()).isEqualTo(100);



    }
}