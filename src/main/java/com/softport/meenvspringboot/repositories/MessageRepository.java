package com.softport.meenvspringboot.repositories;

import com.softport.meenvspringboot.messages.Message;
import com.softport.meenvspringboot.messages.MessageCount;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MessageRepository extends CrudRepository<Message, Long> {
    /*
    * get rows grouped by unique message id.
    * */
    @Query(value = "select m from Message m where m.userId = ?1 group by m.messageId order by m.date desc ")
    List<Message> getMessageByUserId(Long userId);

    @Query(value = "select m from Message m group by m.messageId")
    List<Message> getMessageByAllUsers();

    @Query(value = "select m.recipientCount from Message m group by m.messageId")
    Object getTotalMessageSent();
   // List<Message> getMessageByUserId(Long userId);


}
