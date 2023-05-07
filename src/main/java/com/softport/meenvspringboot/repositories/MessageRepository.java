package com.softport.meenvspringboot.repositories;

import com.softport.meenvspringboot.dto.MessageDTO;
import com.softport.meenvspringboot.messages.Message;
import com.softport.meenvspringboot.messages.MessageCount;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MessageRepository extends CrudRepository<Message, Long> {
    /*
    * get rows grouped by unique message id.
    * */
    //@Query(value = "select m from Message m where m.userId = ?1 group by m.messageId order by m.date desc ")
    @Query(value = "select new com.softport.meenvspringboot.dto.MessageDTO(m.senderId, m.message, count(r), m.date) from Message m LEFT JOIN m.recipients r where m.userId = ?1 GROUP BY m.id ORDER BY m.id")
    List<?> getMessageByUserId(Long userId);

    @Query(value = "select m from Message m group by m.messageId")
    List<Message> getMessageByAllUsers();

    @Query(value = "select m.recipientCount from Message m group by m.messageId")
    Object getTotalMessageSent();
    // List<Message> getMessageByUserId(Long userId);

}
