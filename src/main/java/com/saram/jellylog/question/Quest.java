package com.saram.jellylog.question;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Quest {
    @Id
    @GeneratedValue
    private Long quest_Id;

    private int quest_order;
    private String quest_content;
}
