package by.mrj.disruptor.sample.consumer;

import by.mrj.disruptor.sample.domain.LongEvent;
import com.lmax.disruptor.EventHandler;

public class LongEventHandler implements EventHandler<LongEvent> {

    @Override
    public void onEvent(LongEvent event, long sequence, boolean endOfBatch) throws Exception {
        if (endOfBatch) {
            System.out.println(Thread.currentThread().getName() + " - " + sequence + " - " + event);
        }
    }
}
