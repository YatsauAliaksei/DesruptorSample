package by.mrj.disruptor.sample.domain;

import lombok.Setter;
import lombok.ToString;

@ToString
public class LongEvent {

    @Setter
    private long value;
    @Setter
    private String text;
}
