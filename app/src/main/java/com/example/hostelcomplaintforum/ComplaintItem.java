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
    
    public String getSubject(){
        return subject;
    }
    
    public void setSubject(String subject){
        this.subject=subject;
    }
    
    public String getDesc(){
        return desc;
    }
    
    public void setDesc(String desc){
        this.desc=desc;
    }
    
    public String getMode(){
        return mode;
    }
    
    public void setMode(String mode){
        this.mode=mode;
    }
    
    public String getName(){
        return name;
    }
    
    public void setName(String name){
        this.name=name;
    }
    
    public String getEmail(){
        return email;
    }
    
    public void setEmail(String email){
        this.email=email;
    }
    
    public String getRoom(){
        return room;
    }
    
    public void setRoom(String room){
        this.room=room;
    }
    
    public String getHostel(){
        return hostel;
    }
    
    public void setHostel(String hostel){
        this.hostel=hostel;
    }
    
    public long getTime(){
        return time;
    }
    
    public void setTime(long time){
        this.time=time;
    }
    
    public String getState(){
        return state;
    }
    
    public void setState(String state){
        this.state=state;
    }
    
    public String getReply(){
        return reply;
    }
    
    public void setReply(String reply){
        this.reply=reply;
    }

}
