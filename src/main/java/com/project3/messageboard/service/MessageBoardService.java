package com.project3.messageboard.service;

import com.project3.messageboard.entity.Messageboard;
import com.project3.messageboard.repository.MessageBoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageBoardService {

    @Autowired
    private MessageBoardRepository messageBoardRepository;

    public void write(Messageboard messageboard) {
        messageBoardRepository.save(messageboard);
    }
}

