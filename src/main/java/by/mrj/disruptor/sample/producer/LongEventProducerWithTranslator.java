package by.mrj.disruptor.sample.producer;

import by.mrj.disruptor.sample.domain.LongEvent;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;

import java.util.UUID;

public class LongEventProducerWithTranslator {

    private final RingBuffer<LongEvent> ringBuffer;

    public LongEventProducerWithTranslator(RingBuffer<LongEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    private static final EventTranslatorOneArg<LongEvent, Long> TRANSLATOR =
            (event, sequence, l) -> {
                event.setValue(l);
                event.setText(UUID.randomUUID().toString());
            };

    public void onData(long l) {
        ringBuffer.publishEvent(TRANSLATOR, l);
    }
}
