package com.narendradama.ereceipts;

import io.realm.RealmObject;

public class ToDoList extends RealmObject {

    String ToDoListName = null;

    public String getToDoListName() {
        return ToDoListName;
    }

    public void setToDoListName(String toDoListName) {
        ToDoListName = toDoListName;
    }

    public ToDoList(String toDoListName) {
        ToDoListName = toDoListName;
    }

    public ToDoList(){
    }
}
