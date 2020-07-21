package sample;

public class AppThread extends Thread{
    Controller controller;

    public AppThread(Controller controller) {
        this.controller = controller;
        start();
    }

    public void run () {
        Thread current = Thread.currentThread();

        while (!controller.pause) {
            try {
                Thread.sleep(50);
                controller.simulation.step();
                controller.draw();
            } catch (Exception ignore) {}
        }
    }
}
