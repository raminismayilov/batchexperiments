package com.ramismayilov.batchone;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;


@Slf4j
public class PersonItemProcessor implements ItemProcessor<Person, Person> {

    @Override
    public Person process(Person person) throws Exception {
        Person bornAgainPerson =
                new Person(person.getFirstName().toUpperCase(), person.getLastName().toUpperCase());
        log.info("Converting (" + person + ") into (" + bornAgainPerson + ")");
        return bornAgainPerson;
    }
}
