package com.softport.meenvspringboot.repositories;

import com.softport.meenvspringboot.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MessageRepository extends CrudRepository<Message, Long> {
    /*
    * get rows grouped by unique message id.
    * */
    @Query(value = "select m from Message m where m.userId = ?1 group by m.messageId")
    List<Message> getMessageByUserId(Long userId);
   // List<Message> getMessageByUserId(Long userId);
}
