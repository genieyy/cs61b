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
                validArgs(args,1);
                Repository.setup();
            }
            case "add" -> {

                for (int i = 1; i < args.length; ++i) {
                    Repository.addBlobs(args[i]);
                }
            }
            case "commit" -> {
                validArgs(args,2);
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
                validArgs(args,2);
                Repository.find_mesg(args[1]);
            }
            case "status" -> {
                Repository.printstatus();
            }
            case "checkout" -> {
                if(args.length==3) Repository.checkoutheadfile(args[2]);
                if(args.length==4) Repository.checkoutcommitfile(args[1],args[3]);
                if(args.length==2) Repository.checkoutbranchfile(args[1]);
            }
            case "branch" -> {
                validArgs(args,2);
                Repository.create_branch(args[1]);
            }
            case "rm-branch" -> {
                validArgs(args,2);
                Repository.rm_branch(args[1]);
            }
            case "reset" -> {
                validArgs(args,2);
                Repository.reset(args[1]);
            }
            case "merge" -> {
                validArgs(args,2);
                Repository.Merge(args[1]);
            }
            default -> {
                System.out.println("No command with that name exists.");
                System.exit(0);
            }
        }


    }
    public static void validArgs(String[] args,int num){
        if(args.length!=num){
            System.out.println("Incorrect operands.");
            return;
        }
    }

}
