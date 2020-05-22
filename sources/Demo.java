import com.pranay.realtimedatabase.DatabaseEventListener;
import com.pranay.realtimedatabase.ServerConnect;

public class Demo {
    public static void main(String[] args) {
        try {
            ServerConnect conn = new ServerConnect();
            conn.setDatabaseChangeEventListener(new DatabaseEventListener() {

                @Override
                public void onUpdate(String query) {
                    System.out.println("Updated " + query);
                }

                @Override
                public void onSelect(String query) {
                    System.out.println("Seelcted " + query);

                }

                @Override
                public void onDelete(String query) {
                    System.out.println("deleted " + query);
                }

                @Override
                public void onAlter(String query) {
                    System.out.println("altered " + query);

                }
            });
            conn.startDatabaseChangeListenerService();
        } catch (Exception e) {
        }
    }

}