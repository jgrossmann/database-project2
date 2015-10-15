
public class App {

    public static void main(String[] args) {
        if(args.length != 3) {
            System.out.println("Usage: please run ./run.sh UPDATE USAGE");
            return;
        }
        
        double sThresh = 0.0;
        int cThresh = 0;
        
        try {
            sThresh = Double.parseDouble(args[0]);
            cThresh = Integer.parseInt(args[1]);
        } catch(NumberFormatException e) {
            e.printStackTrace();
            return;
        }
        String site = args[2];
        
        QProber prober = new QProber(cThresh, sThresh, site);
    }
}
