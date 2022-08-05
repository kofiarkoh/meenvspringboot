package com.softport.meenvspringboot.repositories;

import com.softport.meenvspringboot.messages.Message;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MessageRepository extends CrudRepository<Message, Long> {
    /*
    * get rows grouped by unique message id.
    * */
    @Query(value = "select m from Message m where m.userId = ?1 group by m.messageId")
    List<Message> getMessageByUserId(Long userId);

    @Query(value = "select m from Message m group by m.messageId")
    List<Message> getMessageByAllUsers();
   // List<Message> getMessageByUserId(Long userId);


}
