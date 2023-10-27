package gitlet;

import java.io.IOException;
import java.text.ParseException;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) throws IOException, ParseException {
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
                if (args.length != 2) {
                    System.out.println("Please enter a commit message.");
                    System.exit(0);
                }
                Repository.commitbuild(args[1]);
            }
            case "rm" -> {
                for (int i = 1; i < args.length; ++i) {
                    Repository.rmfiles(args[i]);
                }
            }
            case "log" -> {

                Repository.logcommits();
            }
            case "global-log" -> {

                Repository.global_log();
            }
            case "find" -> {
                if (args.length != 2) {
                    System.out.println("Please enter a command.");
                    System.exit(0);
                }
                Repository.find_mesg(args[1]);
            }
            case "status" -> {

                Repository.printstatus();
            }
            case "checkout" -> {

            }
            case "branch" -> {

            }
            case "rm-branch" -> {

            }
            case "reset" -> {

            }
            case "merge" -> {

            }
            default -> {
                System.out.println("No command with that name exists.");
                System.exit(0);
            }
        }

    }

}
