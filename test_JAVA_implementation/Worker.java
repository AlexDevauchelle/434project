import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.List;

public class Worker {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Utilisation : java Worker <adresse IP du maître>");
            return;
        }

        String masterIP = args[0];
        int masterPort = 9999;

        try {
            Socket socket = new Socket(masterIP, masterPort);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            RegisterRequest request = new RegisterRequest();
            out.writeObject(request); // Envoie un message d'enregistrement
            boolean isDone = false;

            while (!isDone) {
                try {
                    ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                    Object receivedObject = in.readObject();
                    if (receivedObject instanceof RegisterReply) {
                        RegisterReply registerReply = (RegisterReply) receivedObject;
                        System.out.println("Message reçu : " + registerReply.getMessage());

                    } else if (receivedObject instanceof SamplingKeyRequest) {
                        List<String> myKeys = Arrays.asList("hello", "world", "red");
                        SamplingKeyReply reply = new SamplingKeyReply(myKeys);
                        ObjectOutputStream out2 = new ObjectOutputStream(socket.getOutputStream());
                        out2.writeObject(reply);

                        isDone = true;
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}