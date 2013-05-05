package com.HuskySoft.metrobike.ui.utility;

public class HistoryItem {
    public int index;
    public String from;
    public String to;
    public HistoryItem(){
        super();
    }
    
    //public HistoryItem(int icon, String title, String to) {
    public HistoryItem(int index, String from, String to) {
        super();
        this.index = index;
        this.from = from;
        this.to = to;
    }
}
