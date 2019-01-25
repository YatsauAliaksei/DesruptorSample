package by.mrj.disruptor.sample.factory;

import by.mrj.disruptor.sample.domain.LongEvent;
import com.lmax.disruptor.EventFactory;

public class LongEventFactory implements EventFactory<LongEvent> {

    @Override
    public LongEvent newInstance() {
        return new LongEvent();
    }
}
