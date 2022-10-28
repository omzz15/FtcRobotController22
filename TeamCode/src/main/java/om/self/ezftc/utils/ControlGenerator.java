package om.self.ezftc.utils;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ControlGenerator {
    Future<Integer> integerFuture = new Future<Integer>() {
        @Override
        public boolean cancel(boolean b) {
            return false;
        }

        @Override
        public boolean isCancelled() {
            return false;
        }

        @Override
        public boolean isDone() {
            return false;
        }

        @Override
        public Integer get() throws ExecutionException, InterruptedException {
            return null;
        }

        @Override
        public Integer get(long l, TimeUnit timeUnit) throws ExecutionException, InterruptedException, TimeoutException {
            return null;
        }
    }
}
