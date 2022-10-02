package com.example.hostelcomplaintforum;

public class ComplaintItem {
    String subject;
    String desc;
    String mode;
    String name;
    String email;
    String room;
    String hostel;
    String state;


    String reply;
    long time;

    public ComplaintItem(String subject, String desc, String mode, String name, String email, String room, String hostel, long time, String state, String reply) {
        this.subject = subject;
        this.desc = desc;
        this.mode = mode;
        this.name = name;
        this.email = email;
        this.room = room;
        this.hostel = hostel;
        this.state = state;
        this.reply = reply;
        this.time = time;
    }

}
