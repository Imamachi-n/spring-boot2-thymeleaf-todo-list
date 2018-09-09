package com.example.todotask.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "task")
@Data
@AllArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String subject;

    @JsonProperty("dead-line")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate deadLine;

    @JsonProperty("done")
    private Boolean hasDone;

    public Task(){}

    public Task(String subject, LocalDate deadLine, Boolean hasDone) {
        this.subject = subject;
        this.deadLine = deadLine;
        this.hasDone = hasDone;
    }
}
