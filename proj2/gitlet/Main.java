package gitlet;

import java.io.IOException;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) throws IOException {
        // TODO: what if args is empty?
        if(args.length==0){
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        String firstArg = args[0];
        switch (firstArg) {
            case "init" -> {
                if (args.length != 1) {
                    System.out.println("Please enter a command.");
                    System.exit(0);
                }
                Repository.setup();
            }
            case "add" -> {
                for (int i = 1; i < args.length; ++i) {
                    Repository.addBlobs(args[i]);
                }
            }
            case "commit" -> {
                if (args.length != 3) {
                    System.out.println("Please enter a command.");
                    System.exit(0);
                }
                if (args[1] != "-m") {
                    System.out.println("");
                }
                Repository.commitbuild(args[2]);
            }
            case "rm" -> {
                for (int i = 1; i < args.length; ++i) {
                    Repository.rmfiles(args[i]);
                }
            }
            case "log" -> {
                if (args.length != 3) {
                    System.out.println("Please enter a command.");
                    System.exit(0);
                }
                Repository.logcommits();
            }
            case "global-log" -> {
                if (args.length != 3) {
                    System.out.println("Please enter a command.");
                    System.exit(0);
                }
            }
            case "find" -> {
                if (args.length != 3) {
                    System.out.println("Please enter a command.");
                    System.exit(0);
                }
            }
            case "status" -> {
                if (args.length != 3) {
                    System.out.println("Please enter a command.");
                    System.exit(0);
                }
            }
            case "checkout" -> {
                if (args.length != 3) {
                    System.out.println("Please enter a command.");
                    System.exit(0);
                }
            }
            case "branch" -> {
                if (args.length != 3) {
                    System.out.println("Please enter a command.");
                    System.exit(0);
                }
            }
            case "rm-branch" -> {
                if (args.length != 3) {
                    System.out.println("Please enter a command.");
                    System.exit(0);
                }
            }
            case "reset" -> {
                if (args.length != 3) {
                    System.out.println("Please enter a command.");
                    System.exit(0);
                }
            }
            case "merge" -> {
                if (args.length != 3) {
                    System.out.println("Please enter a command.");
                    System.exit(0);
                }
            }
            default -> {
                System.out.println("No command with that name exists.");
                System.exit(0);
            }
        }

    }

}
