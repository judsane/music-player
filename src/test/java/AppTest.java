import com.mongodb.*;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class AppTest {

    @Test
    public void testSomething() {
        String filename1 = "F:\\nnn\\song.mp3";
        File file = new File(filename1);
        FileInputStream fis;
        FileOutputStream fos;
        long filesize = file.length();
        long filesizeActual = 0L;
        int splitval = 3;
        int splitsize = (int) (filesize / splitval) + (int) (filesize % splitval);
        byte[] b = new byte[splitsize];
        System.out.println(filename1 + "            " + filesize + " bytes");
        try {
            fis = new FileInputStream(file);
            String name1 = filename1.replaceAll(".mp3", "");
            for (int j = 1; j <= splitval; j++)
            {
                String filecalled = name1 + "_split_" + j + ".mp3";
                fos = new FileOutputStream(filecalled);
                int i = fis.read(b);
                fos.write(b, 0, i);
                fos.close();
                fos = null;
                System.out.println(filecalled + "    " + i + " bytes");
                filesizeActual += i;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Test
    public void main() throws IOException {
        String uri="mongodb://mohan:mohan@cluster2-shard-00-00-ahl4u.mongodb.net:27017,cluster2-shard-00-01-ahl4u.mongodb.net:27017,cluster2-shard-00-02-ahl4u.mongodb.net:27017/test?ssl=true&replicaSet=Cluster2-shard-0&authSource=admin&retryWrites=true";
        MongoClientURI mongoClientURI=new MongoClientURI(uri);
        MongoClient mongoClient=new MongoClient(mongoClientURI);

        DB db=mongoClient.getDB("sampleMongoDB1");
        DBCollection dbCollection=db.getCollection("first");

        var object=new BasicDBObject();
        var fileInputStream=new FileInputStream("C:\\Users\\Mohan Bera\\Downloads\\songImage.jpg");
        byte[] bytes=new byte[fileInputStream.available()];
        fileInputStream.read(bytes);
        object.append("name","Epic Music-When you will save us");
        object.append("address","https://ytww.xyz/@download/192-5c87628805f07-10368000-432/mp3/K1oLThzQ9zo/WHEN%2BWILL%2BYOU%2BSAVE%2BME%2B%257C%2Bby%2BEnd%2BOf%2BSilence%2B%2528Ft.%2BJulie%2BElven%2529.mp3");
        object.append("image",bytes);
        dbCollection.insert(object);
        /*
        BasicDBObject whereQuery = new BasicDBObject();
        whereQuery.put("name", "Tum Hi Ho");
        DBCursor cursor = dbCollection.find(whereQuery);
        while(cursor.hasNext()) {
            System.out.println(cursor.next());
        }
        */
    }
}