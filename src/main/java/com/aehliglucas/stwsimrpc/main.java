package com.aehliglucas.stwsimrpc;

import club.minnced.discord.rpc.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;

public class main {

    private final connection conn;
    private ArrayList<String> data;
    private final long startTimestamp = Instant.now().getEpochSecond();

    private ActionListener refreshListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            data = conn.getData();
            createPresence(data);
        }
    };
    private javax.swing.Timer refreshTimer = new javax.swing.Timer(1000 * 30, refreshListener);

    public main() {
        conn = new connection("StellwerkSim-RichPresence", "alucas", "0.3", "Discord RichPresence for StellwerkSim");
        try {
            conn.connect("localhost");
            refreshTimer.setInitialDelay(1000);
            refreshTimer.start();

        } catch (IOException e) {
            System.out.println("Network error while connecting to StellwerkSim! " + e.getMessage());
            System.exit(100);
        }
    }

    public void createPresence(ArrayList<String> rpc_data) {
        DiscordRPC lib = DiscordRPC.INSTANCE;
        String application_id = "";
        DiscordEventHandlers handlers = new DiscordEventHandlers();
        handlers.ready = (user) -> System.out.println("Ready! You can minimize this window now.");
        lib.Discord_Initialize(application_id, handlers, true, null);
        DiscordRichPresence rpc = new DiscordRichPresence();
        rpc.largeImageKey = "img_3314-kopie";
        if (rpc_data.get(1).equals("Practice Session")) {
            rpc.smallImageKey = "practice";
        }
        else {
            rpc.smallImageKey = "group";
        }
        rpc.state = "Controlling " + rpc_data.get(0);
        rpc.details = rpc_data.get(1);
        rpc.startTimestamp = this.startTimestamp;
        lib.Discord_UpdatePresence(rpc);
        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                lib.Discord_RunCallbacks();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ignored) {}
            }
        }, "RPC-Callback-Handler").start();
    }

    public static void main(String args[]) {
        new main();
    }

}
