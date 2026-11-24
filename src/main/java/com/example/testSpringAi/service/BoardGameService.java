package com.example.testSpringAi.service;

import com.example.testSpringAi.model.Answer;
import com.example.testSpringAi.model.Question;

public interface BoardGameService {
    Answer askQuestion(Question question);
}
