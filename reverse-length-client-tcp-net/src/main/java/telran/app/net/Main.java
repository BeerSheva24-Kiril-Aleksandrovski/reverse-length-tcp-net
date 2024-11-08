package telran.app.net;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;

import telran.net.TcpClient;
import telran.view.*;

public class Main {
    final static HashSet<String> operationsSet = new HashSet<String>(List.of("reverse", "length"));
    static TcpClient client;

    public static void main(String[] args) {

        Item[] items = {
                Item.of("Get started!", Main::startSession),
                Item.of("Exit", Main::exit, true)
        };
        Menu menu = new Menu("Take a Length or Reverse the String Application", items);
        menu.perform(new StandardInputOutput());
    }

    static void startSession(InputOutput io) {
        String host = io.readString("Enter hostname");
        int port = io.readNumberRange("Enter port", "Wrong port", 3000, 50000).intValue();
        if (client != null) {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        client = new TcpClient(host, port);
        String string = io.readString("Enter any string");
        String type = io.readStringOptions("Chose operation from list:" + operationsSet.toString(), "Wrong operation",
                operationsSet);
        String response = client.sendAndReceive(type, string);

        io.writeLine("\nConnected to " + host + ":" + port);
        io.writeLine("Sent: " + string);
        io.writeLine("Operation:" + type);
        io.writeLine("Received response: " + response);
    }

    static void exit(InputOutput io) {
        if(client != null) {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}