import java.io.*;
import java.util.*;
import java.time.*;

public class Client
{
    public static void main(String[] args) throws Exception
    {
        java.util.List<String> extraArgs = new java.util.ArrayList<>();

        try(com.zeroc.Ice.Communicator communicator = com.zeroc.Ice.Util.initialize(args,"config.client",extraArgs))
        {
            //com.zeroc.Ice.ObjectPrx base = communicator.propertyToProxy("Messenger.Proxy");
            Demo.PrinterPrx twoway = Demo.PrinterPrx
                    .checkedCast(communicator.propertyToProxy("Printer.Proxy"))
                    .ice_twoway()
                    .ice_secure(false);
            //System.out.println(communicator.propertyToProxy("Messenger.Proxy").toString());
            // Demo.MessengerPrx messenger = Demo.MessengerPrx
            //                     .checkedCast(base)
            //                     .ice_twoway()
            //                     .ice_secure(false)
            //                     .ice_twoway();
            Demo.PrinterPrx printer = twoway.ice_twoway();

            if(printer == null)
            {
                throw new Error("Invalid proxy");
            }
            int i = 0;
            while(i < 9)
            {
                
                /*System.out.print("Enter your message to the server: ");
                Scanner scanner = new Scanner(System.in);
                String message = scanner.nextLine();
                if(message.equals("exit"))
                {
                    break;
                }*/
                Random r = new Random();
                String message = Integer.toString(45 - r.nextInt(8)); //random number between 38 and 45 inclusive
                if(i == 8)
                {
                    message = "exit";
                }
                System.out.println("Message: " + message);
                
                LocalDateTime timeStart = LocalDateTime.now();
                String finalMessage;

                String username = executeLinuxCommand("whoami");
                String hostname = executeLinuxCommand("hostname");

                finalMessage = username + ":" + hostname + ":" + message;

                LocalDateTime methodCallStart = LocalDateTime.now();

                String[] response = printer.printString(finalMessage).split(":");
                long finalFibNumber = Long.parseLong(response[1]);
                if(response[0].startsWith("Error"))
                {
                    System.out.println("There was a server side error");
                    System.out.println("Final Fib number returned by server: " + finalFibNumber);
                    System.out.println();
                }
                else
                {
                    Duration serverExecutionTime = Duration.parse(response[0]);

                    LocalDateTime methodCallEnd = LocalDateTime.now();
                    Duration totalMethodCallTime = Duration.between(methodCallStart, methodCallEnd);

                    LocalDateTime timeEnd = LocalDateTime.now();
                    Duration endToEndTime = Duration.between(timeStart, timeEnd);

                    System.out.println("Server Service Execution Time: " + serverExecutionTime.toMillis());
                    System.out.println("Client Invocation & Response Reception Time: " + totalMethodCallTime.toMillis());
                    System.out.println("Network & Middleware Transmission Time: " + totalMethodCallTime.minus(serverExecutionTime).toMillis());
                    System.out.println("Total End-to-End Elapsed Time: " + endToEndTime.toMillis());
                    System.out.println("Final Fib number returned by server: " + finalFibNumber);
                    System.out.println();
                }

                i++;
            }
        }


    }

    public static String executeLinuxCommand(String command) throws Exception
    {
        Process p = Runtime.getRuntime().exec(command);
        BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        String output = "";

        while ((line = in.readLine()) != null) {
            output += line;
        }
        in.close();
        p.destroy();
        return output;
    }
}