package by.mrj.disruptor.sample;

import by.mrj.disruptor.sample.consumer.LongEventHandler;
import by.mrj.disruptor.sample.domain.LongEvent;
import by.mrj.disruptor.sample.factory.LongEventFactory;
import by.mrj.disruptor.sample.producer.LongEventProducerWithTranslator;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WorkHandler;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;

public class LongEventMain {

    public static void main(String[] args) throws Exception {
        // Specify the size of the ring buffer, must be power of 2.
        int bufferSize = 1024;

        // Construct the Disruptor with a SingleProducerSequencer
        Disruptor<LongEvent> disruptor = new Disruptor<>(
                new LongEventFactory(), bufferSize, DaemonThreadFactory.INSTANCE, ProducerType.SINGLE, new BlockingWaitStrategy());

        // Common handler
        LongEventHandler commonEventHandler = new LongEventHandler();

        EventHandler<LongEvent> stepOne = (event, sequence, endOfBatch) -> commonEventHandler.onEvent(event, 0, false);
        disruptor.handleEventsWith(stepOne);

        // Workers for parallel processing Step 2.
        WorkHandler<LongEvent> step2worker3 = event -> commonEventHandler.onEvent(event, 3, false);
        WorkHandler<LongEvent> step2worker1 = event -> commonEventHandler.onEvent(event, 1, false);

        disruptor.after(stepOne)
                .handleEventsWithWorkerPool(step2worker3, step2worker1) // parallel processing
                .handleEventsWith( // final process
                        (EventHandler<LongEvent>) (event, sequence, endOfBatch) -> commonEventHandler.onEvent(event, 5, true));

        // Start the Disruptor, starts all threads running
        disruptor.start();

        // Get the ring buffer from the Disruptor to be used for publishing.
        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();

        LongEventProducerWithTranslator longEventProducerWithTranslator = new LongEventProducerWithTranslator(ringBuffer);

        for (long l = 0; true; l++) {
            longEventProducerWithTranslator.onData(l);

            Thread.sleep(100);
        }
    }
}
