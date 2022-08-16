package com.aehliglucas.stwsimrpc;

import js.java.stspluginlib.PluginClient;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class connection extends PluginClient{

    public String name;
    public boolean online = true;


    private final ActionListener refreshListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            request_anlageninfo();
        }
    };
    private final javax.swing.Timer refreshTimer = new javax.swing.Timer(1000 * 30, refreshListener);

    connection(String name, String author, String version, String text) {
        super(name, author, version, text);
    }

    @Override
    protected void connected() {
        this.request_setdebug(true);
        this.request_anlageninfo();
        refreshTimer.setInitialDelay(1000);
        refreshTimer.start();
    }

    public ArrayList<String> getData() {
        ArrayList<String> re = new ArrayList<String>();
        re.add(this.name);
        if (this.online) {
            re.add("Online Session");
        } else {
            re.add("Practice Session");
        }
        return re;
    }

    @Override
    protected void closed()
    {
        System.out.println("Connection to StellwerkSim closed! Exiting...");
    }

    @Override
    protected void response_anlageninfo(AnlagenInfo anlagenInfo) {
        this.name = anlagenInfo.name;
        this.online = anlagenInfo.online;
    }

    // Just some useless methods that have to be implemented

    @Override
    protected void response_bahnsteigliste(HashMap<String, BahnsteigInfo> hashMap) {

    }

    @Override
    protected void response_zugliste(HashMap<Integer, String> hashMap) {

    }

    @Override
    protected void response_zugdetails(int i, ZugDetails zugDetails) {

    }

    @Override
    protected void response_zugfahrplan(int i, LinkedList<ZugFahrplanZeile> linkedList) {

    }
}
