import edu.pk.poc.orm.core.Configuration;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

/**
 * Created by Dipti on 1/25/2016.
 */
public class BlockingQueueDemo {
    public static void main(String[] args) {
        BlockingQueue queue = new BlockingQueue(3);
        ProducerWorker producerWorker = new ProducerWorker(queue);
        CosumerWorker conRunnable = new CosumerWorker(queue);
        Thread t1 = new Thread(producerWorker, "Producer");
        Thread t2 = new Thread(conRunnable, "Consumer");
        t1.start();
        t2.start();
        try {
            Thread.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        producerWorker.setFlag(false);
        conRunnable.setFlag(false);
    }
}

class ProducerWorker implements Runnable {
    BlockingQueue queue;
    boolean flag = true;

    public ProducerWorker(BlockingQueue queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        int i = 1;
        while (flag) {
            System.out.println("Added :" + i);
            queue.add(i++);
        }
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}

class CosumerWorker implements Runnable {
    BlockingQueue queue;
    boolean flag = true;

    public CosumerWorker(BlockingQueue queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while (flag) {
            System.out.println("Removed :" + queue.remove());
        }
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}

class BlockingQueue {
    ArrayList<Integer> list = new ArrayList<>();
    int MAX;

    public BlockingQueue(int i) {
        this.MAX = i;
    }

    public synchronized void add(Integer i) {
        if (list.size() >= MAX) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        list.add(i);
        notifyAll();
    }

    public synchronized Integer remove() {
        if (list.size() == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Integer remove = list.remove(0);
        notifyAll();
        return remove;
    }
}