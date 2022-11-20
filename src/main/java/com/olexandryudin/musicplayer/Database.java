package com.olexandryudin.musicplayer;

import com.mongodb.*;
import javafx.scene.image.Image;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private final String uri="mongodb://mohan:mohan@cluster2-shard-00-00-ahl4u.mongodb.net:27017,cluster2-shard-00-01-ahl4u.mongodb.net:27017,cluster2-shard-00-02-ahl4u.mongodb.net:27017/test?ssl=true&replicaSet=Cluster2-shard-0&authSource=admin&retryWrites=true";
    private final MongoClientURI mongoClientURI=new MongoClientURI(uri);
    private final MongoClient mongoClient = new MongoClient(mongoClientURI);
    private final DB database = mongoClient.getDB("sampleMongoDB1");
    private final DBCollection collection = database.getCollectionFromString("first");
    private DBCursor cursor=collection.find();

    public List<String> list() {
        var arrayList=new ArrayList<String>();
        cursor = collection.find();

        while(cursor.hasNext()) {
            var document= cursor.next();
            var name=document.get("name").toString();
            var address=document.get("address").toString();
            arrayList.add(name + "77" + address);
        }

        return arrayList;
    }

    public Image imageFind(String songName) throws IOException {
        Image image1=null;

        var basicDBObject=new BasicDBObject();
        basicDBObject.put("name", songName);
        cursor = collection.find(basicDBObject);

        while (cursor.hasNext()) {
            var dbObject=cursor.next();

            if(dbObject.containsField("image")) {
                var bytes=(byte[])(dbObject.get("image"));
                var fileOutputStream=new FileOutputStream("D:/musicPlayer/system/data/musicImage.jpg");
                fileOutputStream.write(bytes);
                fileOutputStream.close();
                image1=new Image(new File("D:/musicPlayer/system/data/musicImage.jpg").toURI().toString());
            }
        }

        return image1;
    }
}
