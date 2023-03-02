package com.kenzie.appserver.repositories.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.google.common.base.Strings;
import com.kenzie.appserver.service.model.Consoles;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;

@DynamoDBTable(tableName = "VideoGame")
public class VideoGameRecord {
    private String name;
    private String description;
    private Consoles[] consoles;

    @DynamoDBHashKey(attributeName = "name")
    public String getName() {
        return name;
    }
    @DynamoDBAttribute(attributeName = "Description")
    public String getDescription() {
        return description;
    }
    @DynamoDBAttribute(attributeName = "Consoles")
    public List<String> getConsoles() {
        List<String> listOfConsoles = new ArrayList<>();
        for(Consoles console : consoles){
            listOfConsoles.add(console.getName());
        }
        return listOfConsoles;
    }
}
