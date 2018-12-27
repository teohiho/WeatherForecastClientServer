public class Main {
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
            	Thread th = new Thread(new ui());
                th.start();
                Thread s = new Thread(new Server());
                s.start();
                
            }
        });
    }
}